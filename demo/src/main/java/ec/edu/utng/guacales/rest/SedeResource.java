package ec.edu.utng.guacales.rest;

import ec.edu.utng.guacales.dto.SedeDTO;
import ec.edu.utng.guacales.model.Sede;
import ec.edu.utng.guacales.repository.SedeRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/sedes")
@Produces(MediaType.APPLICATION_JSON)
public class SedeResource {

    @Inject
    private SedeRepository repository;

    @GET
    public List<SedeDTO> listar() {
        return repository.listarTodas().stream()
                .map(this::convertir)
                .collect(Collectors.toList());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response crear(SedeDTO datos) {
        String error = validar(datos);
        if (error != null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("mensaje", error))
                    .build();
        }

        Sede sede = new Sede();
        sede.setNombre(datos.getNombre().trim());
        sede.setCiudad(datos.getCiudad().trim());
        sede.setPais(datos.getPais().trim());
        sede.setCapacidad(datos.getCapacidad());
        repository.crear(sede);

        return Response.created(UriBuilder.fromResource(SedeResource.class)
                        .path(String.valueOf(sede.getId())).build())
                .entity(convertir(sede))
                .build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        Sede s = repository.buscarPorId(id);
        if (s == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"codigo\":\"SEDE_NO_ENCONTRADA\",\"mensaje\":\"Sede no encontrada\"}")
                    .build();
        }
        return Response.ok(convertir(s)).build();
    }

    static String validar(SedeDTO datos) {
        if (datos == null) return "El cuerpo de la solicitud es obligatorio";
        if (datos.getNombre() == null || datos.getNombre().isBlank()) return "El nombre es obligatorio";
        if (datos.getCiudad() == null || datos.getCiudad().isBlank()) return "La ciudad es obligatoria";
        if (datos.getPais() == null || datos.getPais().isBlank()) return "El país es obligatorio";
        if (datos.getCapacidad() != null && datos.getCapacidad() <= 0) {
            return "La capacidad debe ser mayor que cero";
        }
        return null;
    }

    private SedeDTO convertir(Sede s) {
        SedeDTO dto = new SedeDTO();
        dto.setId(s.getId());
        dto.setNombre(s.getNombre());
        dto.setCiudad(s.getCiudad());
        dto.setPais(s.getPais());
        dto.setCapacidad(s.getCapacidad());
        return dto;
    }
}
