package ec.edu.utng.guacales.repository;

import ec.edu.utng.guacales.model.Jugador;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@ApplicationScoped
public class JugadorRepository {
    @PersistenceContext(unitName = "guacalesPU")
    private EntityManager em;

    public List<Jugador> listarPorSeleccion(Long seleccionId) {
        TypedQuery<Jugador> query = em.createQuery(
                "SELECT j FROM Jugador j WHERE j.seleccion.id = :seleccionId ORDER BY j.nombre",
                Jugador.class);
        query.setParameter("seleccionId", seleccionId);
        return query.getResultList();
    }

    public Jugador buscarPorId(Long id) {
        return em.find(Jugador.class, id);
    }
}
