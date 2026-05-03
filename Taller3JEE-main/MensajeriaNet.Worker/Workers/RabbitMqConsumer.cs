using System;
using System.Text;
using System.Text.Json;
using System.Threading;
using System.Threading.Tasks;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.DependencyInjection;
using MensajeriaNet.Core.DTOs;
using MensajeriaNet.Core.Entities;
using MensajeriaNet.Core.Enums;
using MensajeriaNet.Core.Interfaces;
using MensajeriaNet.Infrastructure.Repositories;
using RabbitMQ.Client;
using RabbitMQ.Client.Events;

namespace MensajeriaNet.Worker.Workers
{
    public class RabbitMqConsumer : BackgroundService
    {
        private readonly ILogger<RabbitMqConsumer> _logger;
        private readonly IConfiguration _config;
        private readonly IServiceScopeFactory _scopeFactory;
        private IConnection? _connection;
        private IModel? _channel;

        public RabbitMqConsumer(ILogger<RabbitMqConsumer> logger, IConfiguration config, IServiceScopeFactory scopeFactory)
        {
            _logger = logger;
            _config = config;
            _scopeFactory = scopeFactory;
        }

        protected override async Task ExecuteAsync(CancellationToken stoppingToken)
        {
            // Initialize RabbitMQ connection and channel with retries so the worker can start
            // even if the broker is still booting.
            var factory = new ConnectionFactory()
            {
                HostName = _config.GetValue<string>("RabbitMq:Host"),
                Port = _config.GetValue<int>("RabbitMq:Port"),
                UserName = _config.GetValue<string>("RabbitMq:Username"),
                Password = _config.GetValue<string>("RabbitMq:Password"),
                DispatchConsumersAsync = true
            };

            var queue = _config.GetValue<string>("RabbitMq:QueueName");
            while (!stoppingToken.IsCancellationRequested)
            {
                try
                {
                    _connection = factory.CreateConnection();
                    _channel = _connection.CreateModel();
                    _channel.QueueDeclare(queue: queue, durable: true, exclusive: false, autoDelete: false, arguments: null);
                    _channel.BasicQos(0, 1, false);
                    break;
                }
                catch (Exception ex)
                {
                    _logger.LogWarning(ex, "RabbitMQ not available yet, retrying in 5 seconds");
                    await Task.Delay(TimeSpan.FromSeconds(5), stoppingToken);
                }
            }

            if (_channel == null)
            {
                return;
            }

            _logger.LogInformation("RabbitMqConsumer started");
            var consumer = new AsyncEventingBasicConsumer(_channel);

            consumer.Received += async (sender, ea) =>
            {
                var body = ea.Body.ToArray();
                var json = Encoding.UTF8.GetString(body);
                int? mensajePersistidoId = null;
                try
                {
                    using var scope = _scopeFactory.CreateScope();
                    var repository = scope.ServiceProvider.GetRequiredService<MensajeRepository>();
                    var emailService = scope.ServiceProvider.GetRequiredService<IEmailService>();

                    var incoming = JsonSerializer.Deserialize<MensajeIncomingDto>(json, new JsonSerializerOptions { PropertyNameCaseInsensitive = true });
                    if (incoming == null) throw new Exception("Payload inválido");

                    var entidad = new Mensaje
                    {
                        Destinatario = incoming.Destinatario,
                        Asunto = incoming.Asunto,
                        Cuerpo = incoming.Cuerpo,
                        TipoMensaje = Enum.Parse<TipoMensaje>(incoming.TipoMensaje, ignoreCase: true),
                        FechaRecibido = DateTime.UtcNow,
                        EstadoEnvio = EstadoEnvio.PENDIENTE,
                        IntentoEnvio = 0
                    };

                    await repository.AddAsync(entidad);
                    await repository.SaveChangesAsync();
                    mensajePersistidoId = entidad.Id;

                    await emailService.SendAsync(entidad);

                    entidad.EstadoEnvio = EstadoEnvio.ENVIADO;
                    entidad.IntentoEnvio = 1;
                    await repository.SaveChangesAsync();
                    _channel!.BasicAck(ea.DeliveryTag, false);
                }
                catch (Exception ex)
                {
                    _logger.LogError(ex, "Error procesando mensaje: {msg}", ex.Message);
                    if (mensajePersistidoId.HasValue)
                    {
                        try
                        {
                            using var scope = _scopeFactory.CreateScope();
                            var repository = scope.ServiceProvider.GetRequiredService<MensajeRepository>();
                            var m = await repository.GetByIdAsync(mensajePersistidoId.Value);
                            if (m != null && m.EstadoEnvio == EstadoEnvio.PENDIENTE)
                            {
                                m.EstadoEnvio = EstadoEnvio.FALLIDO;
                                m.IntentoEnvio = Math.Max(1, m.IntentoEnvio);
                                var detalle = ex.Message ?? "";
                                m.ErrorDetalle = detalle.Length > 500 ? detalle[..500] : detalle;
                                await repository.SaveChangesAsync();
                            }
                        }
                        catch (Exception inner)
                        {
                            _logger.LogWarning(inner, "No se pudo marcar mensaje {Id} como FALLIDO", mensajePersistidoId);
                        }
                    }

                    _channel!.BasicAck(ea.DeliveryTag, false);
                }
            };

            _channel.BasicConsume(queue, false, consumer);
            await Task.Delay(Timeout.Infinite, stoppingToken);
        }

        public override void Dispose()
        {
            _channel?.Close();
            _connection?.Close();
            base.Dispose();
        }
    }
}
