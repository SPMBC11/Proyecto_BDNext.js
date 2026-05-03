using Microsoft.AspNetCore.Mvc;
using MensajeriaNet.Infrastructure.Repositories;
using System.Linq;
using MensajeriaNet.Core.DTOs;
using MensajeriaNet.Core.Enums;

namespace MensajeriaNet.Api.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class EstadisticasController : ControllerBase
    {
        private readonly MensajeRepository _repo;
        public EstadisticasController(MensajeRepository repo) { _repo = repo; }

        private static Dictionary<string, int> BuildCompleta(IEnumerable<string> keys, Func<string, int> valueFactory)
        {
            return keys.ToDictionary(key => key, valueFactory);
        }

        [HttpGet]
        public async Task<IActionResult> Get()
        {
            var all = await _repo.GetAllAsync();
            var total = all.Count;
            var tipos = Enum.GetNames<TipoMensaje>();
            var estados = Enum.GetNames<EstadoEnvio>();

            var porTipo = BuildCompleta(tipos, tipo => all.Count(m => m.TipoMensaje.ToString() == tipo));
            var porEstado = BuildCompleta(estados, estado => all.Count(m => m.EstadoEnvio.ToString() == estado));
            var enviados = porEstado.ContainsKey("ENVIADO") ? porEstado["ENVIADO"] : 0;
            var tasa = total == 0 ? 0 : Math.Round((double)enviados / total * 100, 2);

            var dto = new EstadisticasDto { Total = total, PorTipo = porTipo, PorEstado = porEstado, TasaExito = tasa };
            return Ok(dto);
        }

        [HttpGet("por-tipo/{tipo}")]
        public async Task<IActionResult> GetPorTipo(string tipo)
        {
            if (!Enum.TryParse<TipoMensaje>(tipo, ignoreCase: true, out var parsedTipo))
            {
                return BadRequest(new { error = "tipo inválido" });
            }

            var list = await _repo.GetByTipoAsync(tipo);
            var total = list.Count;
            var porEstado = Enum.GetNames<EstadoEnvio>().ToDictionary(estado => estado, estado => list.Count(m => m.EstadoEnvio.ToString() == estado));
            var enviados = porEstado.ContainsKey("ENVIADO") ? porEstado["ENVIADO"] : 0;
            var tasa = total == 0 ? 0 : Math.Round((double)enviados / total * 100, 2);
            var dto = new EstadisticasDto { Total = total, PorTipo = new System.Collections.Generic.Dictionary<string,int> { { parsedTipo.ToString(), total } }, PorEstado = porEstado, TasaExito = tasa };
            return Ok(dto);
        }
    }
}
