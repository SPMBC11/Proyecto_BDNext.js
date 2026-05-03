using System;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using MensajeriaNet.Core.DTOs;
using MensajeriaNet.Core.Enums;
using MensajeriaNet.Api.Services;

namespace MensajeriaNet.Api.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class SimulacionController : ControllerBase
    {
        private static readonly string[] Nombres =
        {
            "Ana", "Luis", "Camila", "Diego", "Sofia", "Mario", "Laura", "Carlos"
        };

        private static readonly string[] Asuntos =
        {
            "Bienvenido al curso",
            "Recordatorio de actividad",
            "Alerta de avance",
            "Resultado de simulacion",
            "Novedades del sistema"
        };

        private static readonly string[] Cuerpos =
        {
            "Tu cuenta fue creada correctamente.",
            "Tienes una actividad pendiente para hoy.",
            "Se detecto bajo progreso y se sugiere refuerzo.",
            "Se actualizo tu estado con base en la simulacion.",
            "Revisa las recomendaciones para avanzar en el curso."
        };

        private readonly RabbitMqPublisher _rabbit;
        private readonly ILogger<SimulacionController> _log;

        public SimulacionController(RabbitMqPublisher rabbit, ILogger<SimulacionController> log)
        {
            _rabbit = rabbit;
            _log = log;
        }

        [HttpPost("ejecutar")]
        public IActionResult Ejecutar([FromQuery] int? cantidadMensajes)
        {
            var cantidad = Math.Clamp(cantidadMensajes ?? 3, 1, 20);
            var random = new Random();
            var tipos = Enum.GetValues<TipoMensaje>();

            try
            {
                for (var i = 0; i < cantidad; i++)
                {
                    var nombre = Nombres[random.Next(Nombres.Length)];
                    var numero = random.Next(100, 999);
                    var tipo = tipos[random.Next(tipos.Length)];
                    var dto = new MensajeIncomingDto
                    {
                        Destinatario = $"{nombre.ToLowerInvariant()}.{numero}@demo.local",
                        Asunto = Asuntos[random.Next(Asuntos.Length)],
                        Cuerpo = Cuerpos[random.Next(Cuerpos.Length)],
                        TipoMensaje = tipo.ToString()
                    };
                    _rabbit.Publish(dto);
                }
            }
            catch (Exception ex)
            {
                _log.LogError(ex, "Fallo al publicar en RabbitMQ");
                return StatusCode(503, new { error = "No se pudo publicar en RabbitMQ", detail = ex.Message });
            }

            _log.LogInformation("Simulacion: {N} mensajes encolados en RabbitMQ para el worker", cantidad);
            return Ok(new { mensajesGenerados = cantidad, encolados = true });
        }
    }
}
