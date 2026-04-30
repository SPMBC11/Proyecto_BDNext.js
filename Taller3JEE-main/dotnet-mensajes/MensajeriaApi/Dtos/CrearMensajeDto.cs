using System.ComponentModel.DataAnnotations;
using MensajeriaApi.Models;

namespace MensajeriaApi.Dtos;

public class CrearMensajeDto
{
    [Required]
    [MaxLength(120)]
    public string Remitente { get; set; } = string.Empty;

    [Required]
    [MaxLength(120)]
    public string Destinatario { get; set; } = string.Empty;

    [Required]
    [MaxLength(200)]
    public string Asunto { get; set; } = string.Empty;

    [Required]
    [MaxLength(2000)]
    public string Cuerpo { get; set; } = string.Empty;

    [Required]
    public TipoMensaje Tipo { get; set; }
}
