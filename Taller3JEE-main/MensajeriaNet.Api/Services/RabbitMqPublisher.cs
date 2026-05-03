using System.Text;
using System.Text.Json;
using MensajeriaNet.Core.DTOs;
using Microsoft.Extensions.Options;
using RabbitMQ.Client;

namespace MensajeriaNet.Api.Services;

public sealed class RabbitMqPublisher : IDisposable
{
    private readonly RabbitMqOptions _opt;
    private readonly ILogger<RabbitMqPublisher> _logger;
    private IConnection? _connection;
    private IModel? _channel;
    private readonly object _gate = new();

    public RabbitMqPublisher(IOptions<RabbitMqOptions> options, ILogger<RabbitMqPublisher> logger)
    {
        _opt = options.Value;
        _logger = logger;
    }

    public void Publish(MensajeIncomingDto dto)
    {
        var json = JsonSerializer.Serialize(dto, new JsonSerializerOptions
        {
            PropertyNamingPolicy = JsonNamingPolicy.CamelCase
        });
        var bytes = Encoding.UTF8.GetBytes(json);

        lock (_gate)
        {
            EnsureChannel();
            var props = _channel!.CreateBasicProperties();
            props.Persistent = true;
            _channel.BasicPublish(exchange: "", routingKey: _opt.QueueName, basicProperties: props, body: bytes);
        }
    }

    private void EnsureChannel()
    {
        if (_channel?.IsOpen == true && _connection?.IsOpen == true)
            return;

        _channel?.Dispose();
        _channel = null;

        if (_connection?.IsOpen != true)
        {
            _connection?.Dispose();
            var factory = new ConnectionFactory
            {
                HostName = _opt.Host,
                Port = _opt.Port,
                UserName = _opt.Username,
                Password = _opt.Password
            };
            _connection = factory.CreateConnection();
            _logger.LogInformation("RabbitMQ conexion a {Host}:{Port} (cola {Queue})", _opt.Host, _opt.Port, _opt.QueueName);
        }

        _channel = _connection!.CreateModel();
        _channel.QueueDeclare(_opt.QueueName, durable: true, exclusive: false, autoDelete: false);
    }

    public void Dispose()
    {
        lock (_gate)
        {
            try { _channel?.Close(); } catch { /* ignore */ }
            _channel?.Dispose();
            _channel = null;
            try { _connection?.Close(); } catch { /* ignore */ }
            _connection?.Dispose();
            _connection = null;
        }
    }
}
