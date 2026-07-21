package ec.edu.utng.guacales.repository;

import ec.edu.utng.guacales.model.Gol;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@ApplicationScoped
public class GolRepository {
    @PersistenceContext(unitName = "guacalesPU")
    private EntityManager em;

    private static final String BASE_QUERY =
            "SELECT g FROM Gol g " +
            "LEFT JOIN FETCH g.jugador " +
            "LEFT JOIN FETCH g.jugador.seleccion " +
            "WHERE g.partido.id = :partidoId " +
            "ORDER BY g.minuto";

    public List<Gol> listarPorPartido(Long partidoId) {
        TypedQuery<Gol> query = em.createQuery(BASE_QUERY, Gol.class);
        query.setParameter("partidoId", partidoId);
        return query.getResultList();
    }
}
