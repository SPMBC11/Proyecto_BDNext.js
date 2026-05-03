using System.Threading.Tasks;
using MensajeriaNet.Core.Entities;

namespace MensajeriaNet.Core.Interfaces
{
    public interface IEmailService
    {
        Task SendAsync(Mensaje mensaje);
    }
}
