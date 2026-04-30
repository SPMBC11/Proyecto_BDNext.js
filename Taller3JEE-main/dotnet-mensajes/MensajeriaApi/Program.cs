using MensajeriaApi.Data;
using MensajeriaApi.Dtos;
using MensajeriaApi.Models;
using MensajeriaApi.Services;
using Microsoft.EntityFrameworkCore;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();
builder.Services.AddScoped<SimuladorService>();
builder.Services.AddHostedService<SimuladorBackgroundService>();
builder.Services.AddCors(options =>
{
    options.AddPolicy("jee-ui", policy =>
    {
        policy
            .WithOrigins("http://localhost:8080", "http://localhost:5099")
            .AllowAnyHeader()
            .AllowAnyMethod();
    });
});
builder.Services.AddDbContext<AppDbContext>(options =>
    options.UseSqlite(builder.Configuration.GetConnectionString("DefaultConnection")));

var app = builder.Build();

using (var scope = app.Services.CreateScope())
{
    var db = scope.ServiceProvider.GetRequiredService<AppDbContext>();
    db.Database.EnsureCreated();
    // Compatibilidad con bases SQLite ya existentes sin migraciones.
    db.Database.ExecuteSqlRaw("""
        CREATE TABLE IF NOT EXISTS "UsuariosSimulados" (
            "Id" INTEGER NOT NULL CONSTRAINT "PK_UsuariosSimulados" PRIMARY KEY AUTOINCREMENT,
            "Nombre" TEXT NOT NULL,
            "Email" TEXT NOT NULL,
            "FechaCreacion" TEXT NOT NULL
        );
    """);

    var tieneUsuarioSimuladoId = false;
    var connection = db.Database.GetDbConnection();
    var wasClosed = connection.State != System.Data.ConnectionState.Open;
    if (wasClosed)
    {
        connection.Open();
    }

    using (var command = connection.CreateCommand())
    {
        command.CommandText = "PRAGMA table_info('Mensajes');";
        using var reader = command.ExecuteReader();
        while (reader.Read())
        {
            var nombreColumna = reader["name"]?.ToString();
            if (string.Equals(nombreColumna, "UsuarioSimuladoId", StringComparison.OrdinalIgnoreCase))
            {
                tieneUsuarioSimuladoId = true;
                break;
            }
        }
    }

    if (wasClosed)
    {
        connection.Close();
    }

    if (!tieneUsuarioSimuladoId)
    {
        db.Database.ExecuteSqlRaw("""ALTER TABLE "Mensajes" ADD COLUMN "UsuarioSimuladoId" INTEGER NULL;""");
    }
}

if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();
app.UseCors("jee-ui");
app.UseDefaultFiles();
app.UseStaticFiles();

app.MapPost("/api/mensajes", async (CrearMensajeDto dto, AppDbContext db) =>
{
    var mensaje = new Mensaje
    {
        Remitente = dto.Remitente.Trim(),
        Destinatario = dto.Destinatario.Trim(),
        Asunto = dto.Asunto.Trim(),
        Cuerpo = dto.Cuerpo.Trim(),
        Tipo = dto.Tipo,
        FechaEnvio = DateTime.UtcNow
    };

    db.Mensajes.Add(mensaje);
    await db.SaveChangesAsync();
    return Results.Created($"/api/mensajes/{mensaje.Id}", mensaje);
});

app.MapGet("/api/mensajes", async (TipoMensaje? tipo, AppDbContext db) =>
{
    var query = db.Mensajes.AsNoTracking();
    if (tipo.HasValue)
    {
        query = query.Where(m => m.Tipo == tipo.Value);
    }

    var mensajes = await query
        .OrderByDescending(m => m.FechaEnvio)
        .ToListAsync();

    return Results.Ok(mensajes);
});

app.MapGet("/api/mensajes/{id:int}", async (int id, AppDbContext db) =>
{
    var mensaje = await db.Mensajes.AsNoTracking().FirstOrDefaultAsync(m => m.Id == id);
    return mensaje is null ? Results.NotFound() : Results.Ok(mensaje);
});

app.MapGet("/api/estadisticas/tipo", async (AppDbContext db) =>
{
    var total = await db.Mensajes.CountAsync();
    if (total == 0)
    {
        return Results.Ok(new List<EstadisticaTipoDto>());
    }

    var stats = await db.Mensajes
        .AsNoTracking()
        .GroupBy(m => m.Tipo)
        .Select(g => new EstadisticaTipoDto
        {
            Tipo = g.Key.ToString(),
            Cantidad = g.Count(),
            Porcentaje = Math.Round((decimal)g.Count() * 100m / total, 2)
        })
        .OrderByDescending(x => x.Cantidad)
        .ToListAsync();

    return Results.Ok(stats);
});

app.MapGet("/api/estadisticas/resumen", async (AppDbContext db) =>
{
    var total = await db.Mensajes.CountAsync();
    var hoy = DateTime.UtcNow.Date;
    var enviadosHoy = await db.Mensajes.CountAsync(m => m.FechaEnvio >= hoy);
    var totalUsuarios = await db.UsuariosSimulados.CountAsync();

    return Results.Ok(new
    {
        totalMensajes = total,
        enviadosHoy,
        totalUsuarios
    });
});

app.MapGet("/api/usuarios", async (AppDbContext db) =>
{
    var usuarios = await db.UsuariosSimulados
        .AsNoTracking()
        .OrderByDescending(u => u.FechaCreacion)
        .Take(20)
        .ToListAsync();
    return Results.Ok(usuarios);
});

app.MapPost("/api/simulacion/ejecutar", async (int? cantidadMensajes, SimuladorService simulador) =>
{
    var cantidad = Math.Clamp(cantidadMensajes ?? 3, 1, 20);
    var resultado = await simulador.EjecutarCicloAsync(cantidad);
    return Results.Ok(resultado);
});

app.Run();
