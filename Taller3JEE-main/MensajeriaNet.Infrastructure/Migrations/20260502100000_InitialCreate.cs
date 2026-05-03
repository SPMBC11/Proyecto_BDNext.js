using System;
using Microsoft.EntityFrameworkCore.Migrations;
using Npgsql.EntityFrameworkCore.PostgreSQL.Metadata;

#nullable disable

namespace MensajeriaNet.Infrastructure.Migrations
{
    public partial class InitialCreate : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "Mensajes",
                columns: table => new
                {
                    Id = table.Column<int>(nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    Destinatario = table.Column<string>(type: "character varying(255)", maxLength: 255, nullable: false),
                    Asunto = table.Column<string>(type: "character varying(500)", maxLength: 500, nullable: false),
                    Cuerpo = table.Column<string>(type: "character varying(4000)", maxLength: 4000, nullable: false),
                    TipoMensaje = table.Column<string>(type: "character varying(50)", maxLength: 50, nullable: false),
                    FechaRecibido = table.Column<DateTime>(type: "timestamp with time zone", nullable: false),
                    EstadoEnvio = table.Column<string>(type: "character varying(50)", maxLength: 50, nullable: false),
                    IntentoEnvio = table.Column<int>(type: "integer", nullable: false),
                    ErrorDetalle = table.Column<string>(type: "text", nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Mensajes", x => x.Id);
                });

            migrationBuilder.CreateIndex(
                name: "IX_Mensajes_EstadoEnvio",
                table: "Mensajes",
                column: "EstadoEnvio");

            migrationBuilder.CreateIndex(
                name: "IX_Mensajes_TipoMensaje",
                table: "Mensajes",
                column: "TipoMensaje");

            migrationBuilder.InsertData(
                table: "Mensajes",
                columns: new[] { "Id", "Asunto", "Cuerpo", "Destinatario", "ErrorDetalle", "EstadoEnvio", "FechaRecibido", "IntentoEnvio", "TipoMensaje" },
                values: new object[,]
                {
                    { 1, "Bienvenida", "Contenido 1", "user1@example.com", null, "ENVIADO", new DateTime(2026, 5, 2, 10, 0, 0, DateTimeKind.Utc), 1, "NOTIFICACION" },
                    { 2, "Alerta sistema", "Contenido 2", "user2@example.com", "SMTP timeout", "FALLIDO", new DateTime(2026, 5, 2, 10, 0, 0, DateTimeKind.Utc), 2, "ALERTA" },
                    { 3, "Reporte mensual", "Contenido 3", "user3@example.com", null, "PENDIENTE", new DateTime(2026, 5, 2, 10, 0, 0, DateTimeKind.Utc), 0, "REPORTE" }
                });
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "Mensajes");
        }
    }
}
