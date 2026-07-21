package ec.edu.utng.guacales.rest;

import ec.edu.utng.guacales.model.Auditoria;
import ec.edu.utng.guacales.repository.AuditoriaRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/auditoria")
@Produces(MediaType.APPLICATION_JSON)
public class AuditoriaResource {

    @Inject
    private AuditoriaRepository repository;

    @GET
    public List<Auditoria> listar() {
        return repository.listar();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registrar(Auditoria auditoria) {
        if (auditoria.getAccion() == null || auditoria.getEntidad() == null) {
            return Response.status(422)
                    .entity("{\"codigo\":\"DATOS_INCOMPLETOS\",\"mensaje\":\"Faltan campos obligatorios: accion, entidad\"}")
                    .build();
        }
        Auditoria creada = repository.crear(auditoria);
        return Response.status(Response.Status.CREATED).entity(creada).build();
    }
}
