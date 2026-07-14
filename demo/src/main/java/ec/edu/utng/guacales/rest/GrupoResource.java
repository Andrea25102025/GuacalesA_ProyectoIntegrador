package ec.edu.utng.guacales.rest;

import ec.edu.utng.guacales.dto.GrupoDTO;
import ec.edu.utng.guacales.dto.SeleccionDTO;
import ec.edu.utng.guacales.model.Grupo;
import ec.edu.utng.guacales.model.Seleccion;
import ec.edu.utng.guacales.repository.GrupoRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Path("/grupos")
@Produces(MediaType.APPLICATION_JSON)
public class GrupoResource {

    @Inject
    private GrupoRepository repository;

    @GET
    public List<GrupoDTO> listar() {
        return repository.listarTodos().stream()
                .map(this::convertir)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{codigo}")
    public Response buscarPorCodigo(@PathParam("codigo") String codigo) {
        Grupo g = repository.buscarPorCodigo(codigo);
        if (g == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Grupo no encontrado\"}")
                    .build();
        }
        return Response.ok(convertir(g)).build();
    }

    private GrupoDTO convertir(Grupo g) {
        GrupoDTO dto = new GrupoDTO();
        dto.setId(g.getId());
        dto.setNombre(g.getNombre());
        dto.setCodigo(g.getCodigo());

        List<SeleccionDTO> selecciones = g.getSelecciones() == null ? List.of() :
                g.getSelecciones().stream()
                        .sorted(
                                Comparator.comparingInt(Seleccion::getPuntos).reversed()
                                        .thenComparing(Comparator.comparingInt(
                                                (Seleccion s) -> s.getGolesFavor() - s.getGolesContra()
                                        ).reversed())
                        )
                        .map(this::convertirSeleccion)
                        .collect(Collectors.toList());
        dto.setSelecciones(selecciones);
        return dto;
    }

    private SeleccionDTO convertirSeleccion(Seleccion s) {
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
