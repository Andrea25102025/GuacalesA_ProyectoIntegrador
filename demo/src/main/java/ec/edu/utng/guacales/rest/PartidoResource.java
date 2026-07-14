package ec.edu.utng.guacales.rest;

import ec.edu.utng.guacales.dto.PartidoDTO;
import ec.edu.utng.guacales.model.Partido;
import ec.edu.utng.guacales.repository.PartidoRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/partidos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PartidoResource {

    @Inject
    private PartidoRepository repository;

    @GET
    public List<PartidoDTO> listar() {
        return repository.listarTodos().stream()
                .map(this::convertir)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        Partido p = repository.buscarPorId(id);
        if (p == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Partido no encontrado\"}")
                    .build();
        }
        return Response.ok(convertir(p)).build();
    }

    @GET
    @Path("/grupo/{codigo}")
    public List<PartidoDTO> listarPorGrupo(@PathParam("codigo") String codigo) {
        return repository.listarPorGrupo(codigo).stream()
                .map(this::convertir)
                .collect(Collectors.toList());
    }

    @POST
    @Path("/{id}/resultado")
    public Response registrarResultado(@PathParam("id") Long id, ResultadoRequest resultado) {
        try {
            Partido actualizado = repository.registrarResultado(
                    id, resultado.golesLocal, resultado.golesVisitante);
            if (actualizado == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Partido no encontrado\"}")
                        .build();
            }
            return Response.ok(convertir(actualizado)).build();
        } catch (IllegalStateException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    public static class ResultadoRequest {
        public int golesLocal;
        public int golesVisitante;
    }

    private PartidoDTO convertir(Partido p) {
        PartidoDTO dto = new PartidoDTO();
        dto.setId(p.getId());
        dto.setNumeroPartidoFifa(p.getNumeroPartidoFifa());
        dto.setSeleccionLocalId(p.getSeleccionLocal().getId());
        dto.setSeleccionLocalNombre(p.getSeleccionLocal().getNombre());
        dto.setSeleccionVisitanteId(p.getSeleccionVisitante().getId());
        dto.setSeleccionVisitanteNombre(p.getSeleccionVisitante().getNombre());
        dto.setSedeNombre(p.getSede().getNombre());
        dto.setGrupoCodigo(p.getGrupo() != null ? p.getGrupo().getCodigo() : null);
        dto.setFechaPartido(p.getFechaPartido());
        dto.setFase(p.getFase());
        dto.setGolesLocal(p.getGolesLocal());
        dto.setGolesVisitante(p.getGolesVisitante());
        dto.setEstado(p.getEstado());
        return dto;
    }
}
