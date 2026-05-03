using System;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Logging;
using Microsoft.EntityFrameworkCore;
using MensajeriaNet.Core.Interfaces;
using MensajeriaNet.Infrastructure.Data;
using MensajeriaNet.Infrastructure.Repositories;
using MensajeriaNet.Worker.Services;
using MensajeriaNet.Worker.Workers;

IHost host = Host.CreateDefaultBuilder(args)
    .ConfigureAppConfiguration((ctx, cfg) =>
    {
        cfg.AddJsonFile("appsettings.json", optional: false, reloadOnChange: true);
    })
    .ConfigureServices((context, services) =>
    {
        services.AddDbContext<AppDbContext>(options =>
            options.UseNpgsql(context.Configuration.GetConnectionString("Default")));

        services.AddScoped<MensajeRepository>();
        services.AddScoped<IEmailService, EmailService>();
        services.AddHostedService<RabbitMqConsumer>();
    })
    .ConfigureLogging(logging => logging.AddConsole())
    .Build();

using (var scope = host.Services.CreateScope())
{
    var db = scope.ServiceProvider.GetRequiredService<AppDbContext>();
    await db.Database.EnsureCreatedAsync();
}

await host.RunAsync();

/*
 Notes:
 - BackgroundService used to continuously consume RabbitMQ; this avoids exposing an endpoint that could be called by producers when blocking long-running tasks.
 - BasicAck is always used (even on failure) to prevent infinite requeues and to leave retry/inspection to application-level logic.
 - MailKit is preferred over native SmtpClient for modern async usage and cross-platform reliability.
*/
