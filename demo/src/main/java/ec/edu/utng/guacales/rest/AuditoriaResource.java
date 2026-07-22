package ec.edu.utng.guacales.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import ec.edu.utng.guacales.service.AuditoriaService;
import ec.edu.utng.guacales.model.Auditoria;

@Path("/auditoria")
@Produces(MediaType.APPLICATION_JSON)
public class AuditoriaResource {

    @Inject
    private AuditoriaService auditoriaService;

    @GET
    public Response listar() {
        List<Auditoria> registros = auditoriaService.listar();
        return Response.ok(registros).build();
    }
}
