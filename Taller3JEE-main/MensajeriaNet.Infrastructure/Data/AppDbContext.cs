using System;
using Microsoft.EntityFrameworkCore;
using MensajeriaNet.Core.Entities;
using MensajeriaNet.Core.Enums;

namespace MensajeriaNet.Infrastructure.Data
{
    public class AppDbContext : DbContext
    {
        public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) { }

        public DbSet<Mensaje> Mensajes { get; set; } = null!;

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);

            // Configure sizes and required
            modelBuilder.Entity<Mensaje>(b =>
            {
                b.Property(p => p.Destinatario).HasMaxLength(255).IsRequired();
                b.Property(p => p.Asunto).HasMaxLength(500).IsRequired();
                b.Property(p => p.Cuerpo).HasMaxLength(4000).IsRequired();

                // Enums stored as strings
                b.Property(p => p.TipoMensaje)
                 .HasConversion<string>()
                 .HasMaxLength(50);

                b.Property(p => p.EstadoEnvio)
                 .HasConversion<string>()
                 .HasMaxLength(50);

                // Indexes to optimize statistics queries
                b.HasIndex(p => p.TipoMensaje);
                b.HasIndex(p => p.EstadoEnvio);

                // Seed data for testing
                var seedDate = new DateTime(2026, 5, 2, 10, 0, 0, DateTimeKind.Utc);

                b.HasData(
                    new Mensaje { Id = 1, Destinatario = "user1@example.com", Asunto = "Bienvenida", Cuerpo = "Contenido 1", TipoMensaje = TipoMensaje.NOTIFICACION, FechaRecibido = seedDate, EstadoEnvio = EstadoEnvio.ENVIADO, IntentoEnvio = 1 },
                    new Mensaje { Id = 2, Destinatario = "user2@example.com", Asunto = "Alerta sistema", Cuerpo = "Contenido 2", TipoMensaje = TipoMensaje.ALERTA, FechaRecibido = seedDate, EstadoEnvio = EstadoEnvio.FALLIDO, IntentoEnvio = 2, ErrorDetalle = "SMTP timeout" },
                    new Mensaje { Id = 3, Destinatario = "user3@example.com", Asunto = "Reporte mensual", Cuerpo = "Contenido 3", TipoMensaje = TipoMensaje.REPORTE, FechaRecibido = seedDate, EstadoEnvio = EstadoEnvio.PENDIENTE, IntentoEnvio = 0 }
                );
            });
        }
    }
}

/*
 EF Commands (include in project comments / README):
 dotnet ef migrations add InitialCreate \
   --project MensajeriaNet.Infrastructure \
   --startup-project MensajeriaNet.Worker
 dotnet ef database update \
   --project MensajeriaNet.Infrastructure \
   --startup-project MensajeriaNet.Worker

 Design notes:
 - Code First was chosen to allow the model to drive schema and seed data easily.
 - Enums persisted as strings for readability and resilience against enum reordering.
*/
