using System;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Infrastructure;
using Microsoft.EntityFrameworkCore.Storage.ValueConversion;
using MensajeriaNet.Infrastructure.Data;

#nullable disable

namespace MensajeriaNet.Infrastructure.Migrations
{
    [DbContext(typeof(AppDbContext))]
    partial class AppDbContextModelSnapshot : ModelSnapshot
    {
        protected override void BuildModel(ModelBuilder modelBuilder)
        {
            modelBuilder.HasAnnotation("ProductVersion", "8.0.0");

            modelBuilder.Entity("MensajeriaNet.Core.Entities.Mensaje", b =>
            {
                b.Property<int>("Id")
                    .ValueGeneratedOnAdd()
                    .HasColumnType("integer")
                    .HasAnnotation("Npgsql:ValueGenerationStrategy", Npgsql.EntityFrameworkCore.PostgreSQL.Metadata.NpgsqlValueGenerationStrategy.IdentityByDefaultColumn);

                b.Property<string>("Asunto")
                    .IsRequired()
                    .HasMaxLength(500)
                    .HasColumnType("character varying(500)");

                b.Property<string>("Cuerpo")
                    .IsRequired()
                    .HasMaxLength(4000)
                    .HasColumnType("character varying(4000)");

                b.Property<string>("Destinatario")
                    .IsRequired()
                    .HasMaxLength(255)
                    .HasColumnType("character varying(255)");

                b.Property<string>("ErrorDetalle")
                    .HasColumnType("text");

                b.Property<DateTime>("FechaRecibido")
                    .HasColumnType("timestamp with time zone");

                b.Property<int>("IntentoEnvio")
                    .HasColumnType("integer");

                b.Property<string>("EstadoEnvio")
                    .IsRequired()
                    .HasMaxLength(50)
                    .HasColumnType("character varying(50)");

                b.Property<string>("TipoMensaje")
                    .IsRequired()
                    .HasMaxLength(50)
                    .HasColumnType("character varying(50)");

                b.HasKey("Id");

                b.HasIndex("EstadoEnvio");
                b.HasIndex("TipoMensaje");

                b.ToTable("Mensajes");

                b.HasData(
                    new
                    {
                        Id = 1,
                        Asunto = "Bienvenida",
                        Cuerpo = "Contenido 1",
                        Destinatario = "user1@example.com",
                        ErrorDetalle = (string)null,
                        EstadoEnvio = "ENVIADO",
                        FechaRecibido = new DateTime(2026, 5, 2, 10, 0, 0, DateTimeKind.Utc),
                        IntentoEnvio = 1,
                        TipoMensaje = "NOTIFICACION"
                    },
                    new
                    {
                        Id = 2,
                        Asunto = "Alerta sistema",
                        Cuerpo = "Contenido 2",
                        Destinatario = "user2@example.com",
                        ErrorDetalle = "SMTP timeout",
                        EstadoEnvio = "FALLIDO",
                        FechaRecibido = new DateTime(2026, 5, 2, 10, 0, 0, DateTimeKind.Utc),
                        IntentoEnvio = 2,
                        TipoMensaje = "ALERTA"
                    },
                    new
                    {
                        Id = 3,
                        Asunto = "Reporte mensual",
                        Cuerpo = "Contenido 3",
                        Destinatario = "user3@example.com",
                        ErrorDetalle = (string)null,
                        EstadoEnvio = "PENDIENTE",
                        FechaRecibido = new DateTime(2026, 5, 2, 10, 0, 0, DateTimeKind.Utc),
                        IntentoEnvio = 0,
                        TipoMensaje = "REPORTE"
                    });
            });
        }
    }
}
