using MensajeriaApi.Models;
using Microsoft.EntityFrameworkCore;

namespace MensajeriaApi.Data;

public class AppDbContext(DbContextOptions<AppDbContext> options) : DbContext(options)
{
    public DbSet<Mensaje> Mensajes => Set<Mensaje>();
    public DbSet<UsuarioSimulado> UsuariosSimulados => Set<UsuarioSimulado>();
}
