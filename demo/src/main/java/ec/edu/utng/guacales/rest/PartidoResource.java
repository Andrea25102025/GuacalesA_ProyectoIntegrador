package ec.edu.utng.guacales.rest;

import ec.edu.utng.guacales.dto.CuotasPartidoDTO;
import ec.edu.utng.guacales.dto.GoleadorDTO;
import ec.edu.utng.guacales.dto.PartidoDTO;
import ec.edu.utng.guacales.dto.PartidoRequestDTO;
import ec.edu.utng.guacales.dto.ResultadoDTO;
import ec.edu.utng.guacales.model.Gol;
import ec.edu.utng.guacales.model.Grupo;
import ec.edu.utng.guacales.model.Partido;
import ec.edu.utng.guacales.model.Sede;
import ec.edu.utng.guacales.model.Seleccion;
import ec.edu.utng.guacales.repository.GolRepository;
import ec.edu.utng.guacales.repository.GrupoRepository;
import ec.edu.utng.guacales.repository.PartidoRepository;
import ec.edu.utng.guacales.repository.SedeRepository;
import ec.edu.utng.guacales.repository.SeleccionRepository;
import ec.edu.utng.guacales.service.ResultadoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Path("/partidos")
@Produces(MediaType.APPLICATION_JSON)
public class PartidoResource {

    @Inject
    private PartidoRepository repository;

    @Inject
    private GolRepository golRepository;

    @Inject
    private ResultadoService resultadoService;

    @Inject
    private SeleccionRepository seleccionRepository;

    @Inject
    private SedeRepository sedeRepository;

    @Inject
    private GrupoRepository grupoRepository;

    @GET
    public List<PartidoDTO> listar(
            @QueryParam("estado") String estado,
            @QueryParam("grupo") String grupo,
            @QueryParam("fase") String fase,
            @QueryParam("fecha") String fechaStr) {

        java.time.LocalDate fecha = (fechaStr != null && !fechaStr.isBlank()) ? java.time.LocalDate.parse(fechaStr) : null;

        return repository.listar(estado, grupo, fase, fecha).stream()
                .map(p -> convertir(p, false))
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{partidoId}")
    public Response buscarPorId(@PathParam("partidoId") Long partidoId) {
        Partido p = repository.buscarPorId(partidoId);
        if (p == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"codigo\":\"PARTIDO_NO_ENCONTRADO\",\"mensaje\":\"Partido no encontrado\"}")
                    .build();
        }
        return Response.ok(convertir(p, true)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response crear(PartidoRequestDTO request) {
        Response validacion = validarRequest(request, true);
        if (validacion != null) return validacion;

        Seleccion local = seleccionRepository.buscarPorId(request.getSeleccionLocalId());
        Seleccion visitante = seleccionRepository.buscarPorId(request.getSeleccionVisitanteId());
        Sede sede = sedeRepository.buscarPorId(request.getSedeId());

        if (local == null || visitante == null) {
            return Response.status(422)
                    .entity("{\"codigo\":\"SELECCION_NO_ENCONTRADA\",\"mensaje\":\"Una de las selecciones no existe\"}")
                    .build();
        }
        if (sede == null) {
            return Response.status(422)
                    .entity("{\"codigo\":\"SEDE_NO_ENCONTRADA\",\"mensaje\":\"La sede indicada no existe\"}")
                    .build();
        }

        Grupo grupo = null;
        if (request.getGrupoId() != null) {
            grupo = grupoRepository.buscarPorId(request.getGrupoId());
            if (grupo == null) {
                return Response.status(422)
                        .entity("{\"codigo\":\"GRUPO_NO_ENCONTRADO\",\"mensaje\":\"El grupo indicado no existe\"}")
                        .build();
            }
        }

        Partido p = new Partido();
        p.setNumeroPartidoFifa(request.getNumeroPartidoFifa());
        p.setSeleccionLocal(local);
        p.setSeleccionVisitante(visitante);
        p.setSede(sede);
        p.setGrupo(grupo);
        p.setFechaPartido(parsearFecha(request.getFechaHora()));
        p.setFase(request.getFase());
        p.setEstado(request.getEstado() != null ? request.getEstado() : "PROGRAMADO");
        p.setCuotaLocal(request.getCuotaLocal());
        p.setCuotaEmpate(request.getCuotaEmpate());
        p.setCuotaVisitante(request.getCuotaVisitante());

        Partido creado = repository.crear(p);
        Partido completo = repository.buscarPorId(creado.getId());

        return Response.created(UriBuilder.fromResource(PartidoResource.class)
                        .path(String.valueOf(completo.getId())).build())
                .entity(convertir(completo, false))
                .build();
    }

    @PUT
    @Path("/{partidoId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response actualizar(@PathParam("partidoId") Long partidoId, PartidoRequestDTO request) {
        Partido p = repository.buscarPorId(partidoId);
        if (p == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"codigo\":\"PARTIDO_NO_ENCONTRADO\",\"mensaje\":\"Partido no encontrado\"}")
                    .build();
        }

        Response validacion = validarRequest(request, false);
        if (validacion != null) return validacion;

        if (request.getSeleccionLocalId() != null) {
            Seleccion local = seleccionRepository.buscarPorId(request.getSeleccionLocalId());
            if (local == null) {
                return Response.status(422)
                        .entity("{\"codigo\":\"SELECCION_NO_ENCONTRADA\",\"mensaje\":\"La seleccion local no existe\"}")
                        .build();
            }
            p.setSeleccionLocal(local);
        }

        if (request.getSeleccionVisitanteId() != null) {
            Seleccion visitante = seleccionRepository.buscarPorId(request.getSeleccionVisitanteId());
            if (visitante == null) {
                return Response.status(422)
                        .entity("{\"codigo\":\"SELECCION_NO_ENCONTRADA\",\"mensaje\":\"La seleccion visitante no existe\"}")
                        .build();
            }
            p.setSeleccionVisitante(visitante);
        }

        if (request.getSedeId() != null) {
            Sede sede = sedeRepository.buscarPorId(request.getSedeId());
            if (sede == null) {
                return Response.status(422)
                        .entity("{\"codigo\":\"SEDE_NO_ENCONTRADA\",\"mensaje\":\"La sede indicada no existe\"}")
                        .build();
            }
            p.setSede(sede);
        }

        if (request.getGrupoId() != null) {
            Grupo grupo = grupoRepository.buscarPorId(request.getGrupoId());
            if (grupo == null) {
                return Response.status(422)
                        .entity("{\"codigo\":\"GRUPO_NO_ENCONTRADO\",\"mensaje\":\"El grupo indicado no existe\"}")
                        .build();
            }
            p.setGrupo(grupo);
        }

        if (request.getFechaHora() != null) p.setFechaPartido(parsearFecha(request.getFechaHora()));
        if (request.getFase() != null) p.setFase(request.getFase());
        if (request.getEstado() != null) p.setEstado(request.getEstado());
        if (request.getNumeroPartidoFifa() != null) p.setNumeroPartidoFifa(request.getNumeroPartidoFifa());
        if (request.getCuotaLocal() != null) p.setCuotaLocal(request.getCuotaLocal());
        if (request.getCuotaEmpate() != null) p.setCuotaEmpate(request.getCuotaEmpate());
        if (request.getCuotaVisitante() != null) p.setCuotaVisitante(request.getCuotaVisitante());

        repository.actualizar(p);
        Partido actualizado = repository.buscarPorId(partidoId);

        return Response.ok(convertir(actualizado, true)).build();
    }

    @PUT
    @Path("/{partidoId}/resultado")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registrarResultado(@PathParam("partidoId") Long partidoId, ResultadoDTO resultado) {
        if (resultado.getGolesLocal() == null || resultado.getGolesVisitante() == null
                || resultado.getGolesLocal() < 0 || resultado.getGolesVisitante() < 0) {
            return Response.status(422)
                    .entity("{\"codigo\":\"RESULTADO_INVALIDO\",\"mensaje\":\"Los goles deben ser numeros positivos\"}")
                    .build();
        }

        Partido guardado = resultadoService.registrarResultado(
                partidoId, resultado.getGolesLocal(), resultado.getGolesVisitante());

        if (guardado == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"codigo\":\"PARTIDO_NO_ENCONTRADO\",\"mensaje\":\"Partido no encontrado\"}")
                    .build();
        }

        Partido actualizado = repository.buscarPorId(partidoId);

        if (actualizado == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"codigo\":\"PARTIDO_NO_ENCONTRADO\",\"mensaje\":\"Partido no encontrado\"}")
                    .build();
        }

        return Response.ok(convertir(actualizado, true)).build();
    }

    private Response validarRequest(PartidoRequestDTO request, boolean esCreacion) {
        if (esCreacion) {
            if (request.getSeleccionLocalId() == null || request.getSeleccionVisitanteId() == null
                    || request.getSedeId() == null || request.getFechaHora() == null || request.getFase() == null) {
                return Response.status(422)
                        .entity("{\"codigo\":\"DATOS_INCOMPLETOS\",\"mensaje\":\"Faltan campos obligatorios: seleccionLocalId, seleccionVisitanteId, sedeId, fechaHora, fase\"}")
                        .build();
            }
        }
        if (request.getSeleccionLocalId() != null && request.getSeleccionVisitanteId() != null
                && request.getSeleccionLocalId().equals(request.getSeleccionVisitanteId())) {
            return Response.status(422)
                    .entity("{\"codigo\":\"SELECCIONES_IGUALES\",\"mensaje\":\"La seleccion local y visitante no pueden ser la misma\"}")
                    .build();
        }
        return null;
    }

    private LocalDateTime parsearFecha(String fechaHora) {
        String limpio = fechaHora.endsWith("Z") ? fechaHora.substring(0, fechaHora.length() - 1) : fechaHora;
        return LocalDateTime.parse(limpio);
    }

    private PartidoDTO convertir(Partido p, boolean incluirGoleadores) {
        PartidoDTO dto = new PartidoDTO();
        dto.setId(p.getId());
        dto.setSeleccionLocal(SeleccionResource.convertir(p.getSeleccionLocal()));
        dto.setSeleccionVisitante(SeleccionResource.convertir(p.getSeleccionVisitante()));
        dto.setFechaHora(p.getFechaPartido().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "Z");
        dto.setSede(p.getSede().getNombre() + ", " + p.getSede().getCiudad());
        dto.setFase(p.getFase());
        dto.setGrupo(p.getGrupo() != null ? p.getGrupo().getNombre() : null);
        dto.setEstado(p.getEstado());
        dto.setGolesLocal(p.getGolesLocal());
        dto.setGolesVisitante(p.getGolesVisitante());
        dto.setCuotas(new CuotasPartidoDTO(p.getCuotaLocal(), p.getCuotaEmpate(), p.getCuotaVisitante()));

        if (incluirGoleadores) {
            List<Gol> goles = golRepository.listarPorPartido(p.getId());
            List<GoleadorDTO> goleadores = goles.stream()
                    .map(g -> new GoleadorDTO(
                            g.getJugador().getNombre(),
                            g.getJugador().getSeleccion().getNombre(),
                            g.getMinuto()))
                    .collect(Collectors.toList());
            dto.setGoleadores(goleadores);
        }

        return dto;
    }
}
