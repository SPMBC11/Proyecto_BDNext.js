namespace MensajeriaNet.Core.DTOs
{
    public class MensajeIncomingDto
    {
        public string Destinatario { get; set; } = null!;
        public string Asunto { get; set; } = null!;
        public string Cuerpo { get; set; } = null!;
        public string TipoMensaje { get; set; } = null!;
    }
}
