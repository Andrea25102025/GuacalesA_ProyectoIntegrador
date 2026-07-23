package ec.edu.utng.guacales.repository;

import ec.edu.utng.guacales.model.Seleccion;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class SeleccionRepository {
    @PersistenceContext(unitName = "guacalesPU")
    private EntityManager em;

    public List<Seleccion> listarTodas() {
        return em.createQuery(
                "SELECT s FROM Seleccion s JOIN FETCH s.grupo ORDER BY s.nombre",
                Seleccion.class)
                .getResultList();
    }

    public Seleccion buscarPorId(Long id) {
        return em.createQuery(
                "SELECT s FROM Seleccion s LEFT JOIN FETCH s.grupo WHERE s.id = :id",
                Seleccion.class)
                .setParameter("id", id)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public List<Seleccion> listarPorGrupo(String codigoGrupo) {
        return em.createQuery(
                "SELECT s FROM Seleccion s JOIN FETCH s.grupo WHERE s.grupo.codigo = :codigo ORDER BY s.puntos DESC, (s.golesFavor - s.golesContra) DESC",
                Seleccion.class)
                .setParameter("codigo", codigoGrupo)
                .getResultList();
    }

    @Transactional
    public void crear(Seleccion seleccion) {
        em.persist(seleccion);
        em.flush();
    }

    @Transactional
    public Seleccion actualizar(Seleccion seleccion) {
        return em.merge(seleccion);
    }

    @Transactional
    public void eliminar(Seleccion seleccion) {
        em.remove(em.contains(seleccion) ? seleccion : em.merge(seleccion));
    }

    public boolean existePorNombre(String nombre, Long excluirId) {
        String jpql = "SELECT COUNT(s) FROM Seleccion s WHERE LOWER(s.nombre) = LOWER(:nombre)";
        if (excluirId != null) {
            jpql += " AND s.id <> :excluirId";
        }
        TypedQuery<Long> query = em.createQuery(jpql, Long.class)
                .setParameter("nombre", nombre);
        if (excluirId != null) {
            query.setParameter("excluirId", excluirId);
        }
        return query.getSingleResult() > 0;
    }

    public boolean existePorCodigoFifa(String codigoFifa, Long excluirId) {
        String jpql = "SELECT COUNT(s) FROM Seleccion s WHERE LOWER(s.codigoFifa) = LOWER(:codigoFifa)";
        if (excluirId != null) {
            jpql += " AND s.id <> :excluirId";
        }
        TypedQuery<Long> query = em.createQuery(jpql, Long.class)
                .setParameter("codigoFifa", codigoFifa);
        if (excluirId != null) {
            query.setParameter("excluirId", excluirId);
        }
        return query.getSingleResult() > 0;
    }

    public boolean existePartidosRelacionados(Long seleccionId) {
        Long total = em.createQuery(
                "SELECT COUNT(p) FROM Partido p WHERE p.seleccionLocal.id = :id OR p.seleccionVisitante.id = :id",
                Long.class)
                .setParameter("id", seleccionId)
                .getSingleResult();
        return total > 0;
    }
}
