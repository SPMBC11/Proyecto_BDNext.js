using System.ComponentModel.DataAnnotations;

namespace MensajeriaApi.Models;

public class Mensaje
{
    public int Id { get; set; }
    public int? UsuarioSimuladoId { get; set; }

    [MaxLength(120)]
    public string Remitente { get; set; } = string.Empty;

    [MaxLength(120)]
    public string Destinatario { get; set; } = string.Empty;

    [MaxLength(200)]
    public string Asunto { get; set; } = string.Empty;

    [MaxLength(2000)]
    public string Cuerpo { get; set; } = string.Empty;

    public TipoMensaje Tipo { get; set; }

    public DateTime FechaEnvio { get; set; } = DateTime.UtcNow;
}
