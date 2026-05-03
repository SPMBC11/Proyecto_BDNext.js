using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using MensajeriaNet.Core.Entities;
using MensajeriaNet.Core.Enums;
using MensajeriaNet.Infrastructure.Data;

namespace MensajeriaNet.Infrastructure.Repositories
{
    public class MensajeRepository
    {
        private readonly AppDbContext _db;
        public MensajeRepository(AppDbContext db) { _db = db; }

        public async Task AddAsync(Mensaje mensaje)
        {
            await _db.Mensajes.AddAsync(mensaje);
        }

        public async Task SaveChangesAsync() => await _db.SaveChangesAsync();

        public async Task<Mensaje?> GetByIdAsync(int id) => await _db.Mensajes.FindAsync(id);

        public async Task<List<Mensaje>> GetPagedAsync(int page, int pageSize) =>
            await _db.Mensajes.OrderByDescending(m => m.FechaRecibido)
                .Skip((page - 1) * pageSize).Take(pageSize).ToListAsync();

        public async Task<int> CountAsync() => await _db.Mensajes.CountAsync();

        public async Task<List<Mensaje>> GetByTipoAsync(string tipo)
        {
            if (!Enum.TryParse<TipoMensaje>(tipo, ignoreCase: true, out var parsedTipo))
            {
                return new List<Mensaje>();
            }

            return await _db.Mensajes.Where(m => m.TipoMensaje == parsedTipo).ToListAsync();
        }

        public async Task<List<Mensaje>> GetAllAsync() => await _db.Mensajes.ToListAsync();
    }
}
