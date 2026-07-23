package ec.edu.utng.guacales.rest;

import ec.edu.utng.guacales.dto.SeleccionDTO;
import ec.edu.utng.guacales.dto.SeleccionRequestDTO;
import ec.edu.utng.guacales.model.Grupo;
import ec.edu.utng.guacales.model.Seleccion;
import ec.edu.utng.guacales.repository.GrupoRepository;
import ec.edu.utng.guacales.repository.SeleccionRepository;
import ec.edu.utng.guacales.service.AuditoriaService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/selecciones")
@Produces(MediaType.APPLICATION_JSON)
public class SeleccionResource {

    @Inject
    private SeleccionRepository repository;

    @Inject
    private GrupoRepository grupoRepository;

    @Inject
    private AuditoriaService auditoriaService;

    @GET
    public List<SeleccionDTO> listar() {
        return repository.listarTodas().stream()
                .map(SeleccionResource::convertir)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public Response obtener(@PathParam("id") Long id) {
        Seleccion seleccion = repository.buscarPorId(id);
        if (seleccion == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(mensaje("No existe una selección con id " + id))
                    .build();
        }
        return Response.ok(convertir(seleccion)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response crear(SeleccionRequestDTO datos) {
        if (datos.getNombre() == null || datos.getNombre().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(mensaje("El nombre de la selección es obligatorio"))
                    .build();
        }
        if (repository.existePorNombre(datos.getNombre(), null)) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(mensaje("Ya existe una selección con ese nombre"))
                    .build();
        }
        if (datos.getCodigoFifa() != null && !datos.getCodigoFifa().isBlank()
                && repository.existePorCodigoFifa(datos.getCodigoFifa(), null)) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(mensaje("Ya existe una selección con ese código FIFA"))
                    .build();
        }

        Grupo grupo = null;
        if (datos.getGrupoId() != null) {
            grupo = grupoRepository.buscarPorId(datos.getGrupoId());
            if (grupo == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(mensaje("El grupo indicado no existe"))
                        .build();
            }
        }

        Seleccion seleccion = new Seleccion();
        seleccion.setNombre(datos.getNombre());
        seleccion.setCodigoFifa(datos.getCodigoFifa());
        seleccion.setConfederacion(datos.getConfederacion());
        seleccion.setEsAnfitrion(datos.getEsAnfitrion() != null ? datos.getEsAnfitrion() : false);
        seleccion.setClasificacion(datos.getClasificacion());
        seleccion.setBanderaUrl(datos.getBanderaUrl());
        seleccion.setGrupo(grupo);
        seleccion.setActivo(true);

        repository.crear(seleccion);

        auditoriaService.registrar(null, "CREAR_SELECCION", "Seleccion", seleccion.getId(),
                "Se creó la selección " + seleccion.getNombre());

        return Response.created(UriBuilder.fromResource(SeleccionResource.class)
                        .path(String.valueOf(seleccion.getId())).build())
                .entity(convertir(seleccion))
                .build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response actualizar(@PathParam("id") Long id, SeleccionRequestDTO datos) {
        Seleccion seleccion = repository.buscarPorId(id);
        if (seleccion == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(mensaje("No existe una selección con id " + id))
                    .build();
        }
        if (datos.getNombre() == null || datos.getNombre().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(mensaje("El nombre de la selección es obligatorio"))
                    .build();
        }
        if (repository.existePorNombre(datos.getNombre(), id)) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(mensaje("Ya existe otra selección con ese nombre"))
                    .build();
        }
        if (datos.getCodigoFifa() != null && !datos.getCodigoFifa().isBlank()
                && repository.existePorCodigoFifa(datos.getCodigoFifa(), id)) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(mensaje("Ya existe otra selección con ese código FIFA"))
                    .build();
        }

        Grupo grupo = null;
        if (datos.getGrupoId() != null) {
            grupo = grupoRepository.buscarPorId(datos.getGrupoId());
            if (grupo == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(mensaje("El grupo indicado no existe"))
                        .build();
            }
        }

        seleccion.setNombre(datos.getNombre());
        seleccion.setCodigoFifa(datos.getCodigoFifa());
        seleccion.setConfederacion(datos.getConfederacion());
        if (datos.getEsAnfitrion() != null) {
            seleccion.setEsAnfitrion(datos.getEsAnfitrion());
        }
        seleccion.setClasificacion(datos.getClasificacion());
        seleccion.setBanderaUrl(datos.getBanderaUrl());
        seleccion.setGrupo(grupo);

        Seleccion actualizada = repository.actualizar(seleccion);

        auditoriaService.registrar(null, "EDITAR_SELECCION", "Seleccion", actualizada.getId(),
                "Se editaron los datos de la selección " + actualizada.getNombre());

        return Response.ok(convertir(actualizada)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") Long id) {
        Seleccion seleccion = repository.buscarPorId(id);
        if (seleccion == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(mensaje("No existe una selección con id " + id))
                    .build();
        }

        if (repository.existePartidosRelacionados(id)) {
            seleccion.setActivo(false);
            Seleccion desactivada = repository.actualizar(seleccion);

            auditoriaService.registrar(null, "DESACTIVAR_SELECCION", "Seleccion", desactivada.getId(),
                    "Se desactivó la selección " + desactivada.getNombre()
                            + " porque tiene partidos relacionados");

            return Response.ok(convertir(desactivada)).build();
        }

        repository.eliminar(seleccion);

        auditoriaService.registrar(null, "ELIMINAR_SELECCION", "Seleccion", id,
                "Se eliminó la selección " + seleccion.getNombre());

        return Response.noContent().build();
    }

    private Map<String, String> mensaje(String texto) {
        return Map.of("mensaje", texto);
    }

    public static SeleccionDTO convertir(Seleccion s) {
        SeleccionDTO dto = new SeleccionDTO();
        dto.setId(s.getId());
        dto.setNombre(s.getNombre());
        dto.setCodigoPais(s.getCodigoFifa());
        dto.setGrupo(s.getGrupo() != null ? s.getGrupo().getNombre() : null);
        dto.setActivo(s.getActivo());
        return dto;
    }
}
