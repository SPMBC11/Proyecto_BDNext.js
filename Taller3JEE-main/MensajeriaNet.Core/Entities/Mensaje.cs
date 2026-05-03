using System;
using MensajeriaNet.Core.Enums;

namespace MensajeriaNet.Core.Entities
{
    public class Mensaje
    {
        public int Id { get; set; }
        public string Destinatario { get; set; } = null!;
        public string Asunto { get; set; } = null!;
        public string Cuerpo { get; set; } = null!;
        public TipoMensaje TipoMensaje { get; set; }
        public DateTime FechaRecibido { get; set; } = DateTime.UtcNow;
        public EstadoEnvio EstadoEnvio { get; set; }
        public int IntentoEnvio { get; set; }
        public string? ErrorDetalle { get; set; }
    }
}
