using MensajeriaApi.Data;
using MensajeriaApi.Models;
using Microsoft.EntityFrameworkCore;

namespace MensajeriaApi.Services;

public class SimuladorService(AppDbContext db)
{
    private static readonly string[] Nombres =
    [
        "Ana", "Luis", "Camila", "Diego", "Sofia", "Mario", "Laura", "Carlos"
    ];

    private static readonly string[] Asuntos =
    [
        "Bienvenido al curso",
        "Recordatorio de actividad",
        "Alerta de avance",
        "Resultado de simulacion",
        "Novedades del sistema"
    ];

    private static readonly string[] Cuerpos =
    [
        "Tu cuenta fue creada correctamente.",
        "Tienes una actividad pendiente para hoy.",
        "Se detecto bajo progreso y se sugiere refuerzo.",
        "Se actualizo tu estado con base en la simulacion.",
        "Revisa las recomendaciones para avanzar en el curso."
    ];

    public async Task<object> EjecutarCicloAsync(int cantidadMensajes = 3)
    {
        var random = new Random();
        var crearUsuario = !await db.UsuariosSimulados.AnyAsync() || random.NextDouble() < 0.35;
        UsuarioSimulado? usuarioNuevo = null;

        if (crearUsuario)
        {
            var nombre = Nombres[random.Next(Nombres.Length)];
            var numero = random.Next(100, 999);
            usuarioNuevo = new UsuarioSimulado
            {
                Nombre = $"{nombre} {numero}",
                Email = $"{nombre.ToLowerInvariant()}.{numero}@demo.local",
                FechaCreacion = DateTime.UtcNow
            };
            db.UsuariosSimulados.Add(usuarioNuevo);
            await db.SaveChangesAsync();
        }

        var usuarios = await db.UsuariosSimulados.AsNoTracking().ToListAsync();
        if (usuarios.Count == 0)
        {
            return new { usuarioCreado = false, mensajesGenerados = 0 };
        }

        var tipos = Enum.GetValues<TipoMensaje>();
        for (var i = 0; i < cantidadMensajes; i++)
        {
            var usuario = usuarios[random.Next(usuarios.Count)];
            var tipo = tipos[random.Next(tipos.Length)];
            var asunto = Asuntos[random.Next(Asuntos.Length)];
            var cuerpo = Cuerpos[random.Next(Cuerpos.Length)];

            db.Mensajes.Add(new Mensaje
            {
                UsuarioSimuladoId = usuario.Id,
                Remitente = "noreply@taller3.net",
                Destinatario = usuario.Email,
                Asunto = asunto,
                Cuerpo = cuerpo,
                Tipo = tipo,
                FechaEnvio = DateTime.UtcNow
            });
        }

        await db.SaveChangesAsync();

        return new
        {
            usuarioCreado = usuarioNuevo is not null,
            usuario = usuarioNuevo,
            mensajesGenerados = cantidadMensajes
        };
    }
}
