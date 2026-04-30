namespace MensajeriaApi.Dtos;

public class EstadisticaTipoDto
{
    public string Tipo { get; set; } = string.Empty;
    public int Cantidad { get; set; }
    public decimal Porcentaje { get; set; }
}
