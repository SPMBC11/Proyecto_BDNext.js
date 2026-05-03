using System.Collections.Generic;

namespace MensajeriaNet.Core.DTOs
{
    public class EstadisticasDto
    {
        public int Total { get; set; }
        public Dictionary<string, int> PorTipo { get; set; } = new();
        public Dictionary<string, int> PorEstado { get; set; } = new();
        public double TasaExito { get; set; }
    }
}
