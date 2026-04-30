using System.ComponentModel.DataAnnotations;

namespace MensajeriaApi.Models;

public class UsuarioSimulado
{
    public int Id { get; set; }

    [MaxLength(120)]
    public string Nombre { get; set; } = string.Empty;

    [MaxLength(120)]
    public string Email { get; set; } = string.Empty;

    public DateTime FechaCreacion { get; set; } = DateTime.UtcNow;
}
