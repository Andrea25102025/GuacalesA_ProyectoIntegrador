package ec.edu.utng.guacales.repository;

import ec.edu.utng.guacales.model.Partido;
import ec.edu.utng.guacales.model.Seleccion;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class PartidoRepository {

    @PersistenceContext(unitName = "guacalesPU")
    private EntityManager em;

    private static final String BASE_QUERY =
        "SELECT p FROM Partido p " +
        "JOIN FETCH p.seleccionLocal " +
        "JOIN FETCH p.seleccionVisitante " +
        "JOIN FETCH p.sede " +
        "LEFT JOIN FETCH p.grupo ";

    public List<Partido> listarTodos() {
        return em.createQuery(BASE_QUERY + "ORDER BY p.fechaPartido", Partido.class)
                .getResultList();
    }

    public Partido buscarPorId(Long id) {
        return em.createQuery(BASE_QUERY + "WHERE p.id = :id", Partido.class)
                .setParameter("id", id)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public List<Partido> listarPorGrupo(String codigoGrupo) {
        return em.createQuery(BASE_QUERY + "WHERE p.grupo.codigo = :codigo ORDER BY p.fechaPartido",
                Partido.class)
                .setParameter("codigo", codigoGrupo)
                .getResultList();
    }

    @Transactional
    public Partido registrarResultado(Long partidoId, int golesLocal, int golesVisitante) {
        Partido partido = em.find(Partido.class, partidoId);
        if (partido == null) {
            return null;
        }
        if ("FINALIZADO".equals(partido.getEstado())) {
            throw new IllegalStateException("Este partido ya tiene un resultado registrado");
        }

        partido.setGolesLocal(golesLocal);
        partido.setGolesVisitante(golesVisitante);
        partido.setEstado("FINALIZADO");

        Seleccion local = partido.getSeleccionLocal();
        Seleccion visitante = partido.getSeleccionVisitante();

        local.setPartidosJugados(local.getPartidosJugados() + 1);
        visitante.setPartidosJugados(visitante.getPartidosJugados() + 1);
        local.setGolesFavor(local.getGolesFavor() + golesLocal);
        local.setGolesContra(local.getGolesContra() + golesVisitante);
        visitante.setGolesFavor(visitante.getGolesFavor() + golesVisitante);
        visitante.setGolesContra(visitante.getGolesContra() + golesLocal);

        if (golesLocal > golesVisitante) {
            local.setPartidosGanados(local.getPartidosGanados() + 1);
            local.setPuntos(local.getPuntos() + 3);
            visitante.setPartidosPerdidos(visitante.getPartidosPerdidos() + 1);
        } else if (golesLocal < golesVisitante) {
            visitante.setPartidosGanados(visitante.getPartidosGanados() + 1);
            visitante.setPuntos(visitante.getPuntos() + 3);
            local.setPartidosPerdidos(local.getPartidosPerdidos() + 1);
        } else {
            local.setPartidosEmpatados(local.getPartidosEmpatados() + 1);
            visitante.setPartidosEmpatados(visitante.getPartidosEmpatados() + 1);
            local.setPuntos(local.getPuntos() + 1);
            visitante.setPuntos(visitante.getPuntos() + 1);
        }

        return partido;
    }
}
