using Microsoft.AspNetCore.Mvc;
using MensajeriaNet.Infrastructure.Repositories;
using System.Threading.Tasks;
using System.Linq;
using MensajeriaNet.Core.DTOs;

namespace MensajeriaNet.Api.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class MensajesController : ControllerBase
    {
        private readonly MensajeRepository _repo;
        public MensajesController(MensajeRepository repo) { _repo = repo; }

        [HttpGet]
        public async Task<IActionResult> Get([FromQuery] int page = 1, [FromQuery] int pageSize = 10)
        {
            var items = await _repo.GetPagedAsync(page, pageSize);
            var total = await _repo.CountAsync();
            var dtos = items.Select(m => new MensajeResponseDto {
                Id = m.Id,
                Destinatario = m.Destinatario,
                Asunto = m.Asunto,
                Cuerpo = m.Cuerpo,
                TipoMensaje = m.TipoMensaje,
                FechaRecibido = m.FechaRecibido,
                EstadoEnvio = m.EstadoEnvio,
                IntentoEnvio = m.IntentoEnvio,
                ErrorDetalle = m.ErrorDetalle
            }).ToArray();

            return Ok(new { items = dtos, total, page, pageSize });
        }

        [HttpGet("{id:int}")]
        public async Task<IActionResult> GetById(int id)
        {
            var m = await _repo.GetByIdAsync(id);
            if (m == null) return NotFound();
            var dto = new MensajeResponseDto {
                Id = m.Id,
                Destinatario = m.Destinatario,
                Asunto = m.Asunto,
                Cuerpo = m.Cuerpo,
                TipoMensaje = m.TipoMensaje,
                FechaRecibido = m.FechaRecibido,
                EstadoEnvio = m.EstadoEnvio,
                IntentoEnvio = m.IntentoEnvio,
                ErrorDetalle = m.ErrorDetalle
            };
            return Ok(dto);
        }

        [HttpGet("por-tipo/{tipo}")]
        public async Task<IActionResult> GetByTipo(string tipo)
        {
            if (!Enum.TryParse<MensajeriaNet.Core.Enums.TipoMensaje>(tipo, ignoreCase: true, out _))
            {
                return BadRequest(new { error = "tipo inválido" });
            }

            var list = await _repo.GetByTipoAsync(tipo);
            var dtos = list.Select(m => new MensajeResponseDto {
                Id = m.Id,
                Destinatario = m.Destinatario,
                Asunto = m.Asunto,
                Cuerpo = m.Cuerpo,
                TipoMensaje = m.TipoMensaje,
                FechaRecibido = m.FechaRecibido,
                EstadoEnvio = m.EstadoEnvio,
                IntentoEnvio = m.IntentoEnvio,
                ErrorDetalle = m.ErrorDetalle
            }).ToArray();
            return Ok(dtos);
        }
    }
}
