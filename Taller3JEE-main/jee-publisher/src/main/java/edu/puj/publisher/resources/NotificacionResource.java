package edu.puj.publisher.resources;

import edu.puj.publisher.dto.MensajePublicadoDto;
import edu.puj.publisher.dto.MensajeRequestDto;
import edu.puj.publisher.util.RabbitMqPublisher;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.Instant;

@Path("/api/notificaciones")
@ApplicationScoped
public class NotificacionResource {

    @Inject
    RabbitMqPublisher publisher;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(MensajeRequestDto req) {
        try {
            MensajePublicadoDto dto = new MensajePublicadoDto();
            dto.destinatario = req.destinatario;
            dto.asunto = req.asunto;
            dto.cuerpo = req.cuerpo;
            dto.tipoMensaje = req.tipoMensaje;
            dto.origen = "SistemaJEE";
            dto.timestamp = Instant.now().toString();
            publisher.publish(dto);
            return Response.accepted().entity(java.util.Map.of("mensaje","Notificación encolada","timestamp",dto.timestamp)).build();
        } catch (Exception e) {
            return Response.serverError().entity(java.util.Map.of("error", e.getMessage())).build();
        }
    }
}
