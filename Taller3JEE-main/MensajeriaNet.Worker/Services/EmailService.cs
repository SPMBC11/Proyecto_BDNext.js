using System;
using System.Threading.Tasks;
using MailKit.Net.Smtp;
using MailKit.Security;
using MimeKit;
using Microsoft.Extensions.Configuration;
using MensajeriaNet.Core.Entities;
using MensajeriaNet.Core.Interfaces;

namespace MensajeriaNet.Worker.Services
{
    public class EmailService : IEmailService
    {
        private readonly IConfiguration _config;
        public EmailService(IConfiguration config)
        {
            _config = config;
        }

        public async Task SendAsync(Mensaje mensaje)
        {
            var smtp = _config.GetSection("Smtp");
            var host = smtp.GetValue<string>("Host");
            var port = smtp.GetValue<int>("Port");
            var user = smtp.GetValue<string>("Username");
            var pass = smtp.GetValue<string>("Password");
            var useSsl = smtp.GetValue<bool>("UseSsl");
            var fromAddress = smtp.GetValue<string>("FromAddress");
            var fromName = smtp.GetValue<string>("FromName");

            var message = new MimeMessage();
            message.From.Add(new MailboxAddress(fromName, fromAddress));
            message.To.Add(MailboxAddress.Parse(mensaje.Destinatario));
            message.Subject = mensaje.Asunto;
            message.Body = new TextPart("plain") { Text = mensaje.Cuerpo };

            using var client = new SmtpClient();
            var socketOptions = useSsl ? SecureSocketOptions.SslOnConnect : SecureSocketOptions.None;
            await client.ConnectAsync(host, port, socketOptions);
            if (!string.IsNullOrWhiteSpace(user))
            {
                await client.AuthenticateAsync(user, pass);
            }
            await client.SendAsync(message);
            await client.DisconnectAsync(true);
        }
    }
}
