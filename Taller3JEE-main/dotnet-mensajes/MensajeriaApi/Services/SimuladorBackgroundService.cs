using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;

namespace MensajeriaApi.Services;

public class SimuladorBackgroundService(IServiceProvider serviceProvider) : BackgroundService
{
    protected override async Task ExecuteAsync(CancellationToken stoppingToken)
    {
        using var timer = new PeriodicTimer(TimeSpan.FromSeconds(10));

        while (!stoppingToken.IsCancellationRequested &&
               await timer.WaitForNextTickAsync(stoppingToken))
        {
            try
            {
                using var scope = serviceProvider.CreateScope();
                var simulador = scope.ServiceProvider.GetRequiredService<SimuladorService>();
                await simulador.EjecutarCicloAsync(2);
            }
            catch
            {
                // Evita detener el host si falla una iteración de simulación.
            }
        }
    }
}
