package ec.edu.utng.guacales.rest;

import ec.edu.utng.guacales.dto.SeleccionDTO;
import ec.edu.utng.guacales.model.Seleccion;
import ec.edu.utng.guacales.repository.SeleccionRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/selecciones")
@Produces(MediaType.APPLICATION_JSON)
public class SeleccionResource {

    @Inject
    private SeleccionRepository repository;

    @GET
    public List<SeleccionDTO> listar() {
        return repository.listarTodas().stream()
                .map(this::convertir)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        Seleccion s = repository.buscarPorId(id);
        if (s == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Selección no encontrada\"}")
                    .build();
        }
        return Response.ok(convertir(s)).build();
    }

    @GET
    @Path("/grupo/{codigo}")
    public List<SeleccionDTO> listarPorGrupo(@PathParam("codigo") String codigo) {
        return repository.listarPorGrupo(codigo).stream()
                .map(this::convertir)
                .collect(Collectors.toList());
    }

    private SeleccionDTO convertir(Seleccion s) {
        SeleccionDTO dto = new SeleccionDTO();
        dto.setId(s.getId());
        dto.setNombre(s.getNombre());
        dto.setCodigoFifa(s.getCodigoFifa());
        dto.setConfederacion(s.getConfederacion());
        dto.setEsAnfitrion(s.getEsAnfitrion());
        dto.setClasificacion(s.getClasificacion());
        dto.setGrupoCodigo(s.getGrupo() != null ? s.getGrupo().getCodigo() : null);
        dto.setPuntos(s.getPuntos());
        dto.setPartidosJugados(s.getPartidosJugados());
        dto.setPartidosGanados(s.getPartidosGanados());
        dto.setPartidosEmpatados(s.getPartidosEmpatados());
        dto.setPartidosPerdidos(s.getPartidosPerdidos());
        dto.setGolesFavor(s.getGolesFavor());
        dto.setGolesContra(s.getGolesContra());
        return dto;
    }
}
