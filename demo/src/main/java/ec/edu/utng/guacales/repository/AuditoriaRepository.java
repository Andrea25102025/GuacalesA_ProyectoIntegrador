package ec.edu.utng.guacales.repository;

import ec.edu.utng.guacales.model.Auditoria;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class AuditoriaRepository {

    @PersistenceContext(unitName = "guacalesPU")
    private EntityManager em;

    public List<Auditoria> listar() {
        TypedQuery<Auditoria> query = em.createQuery(
            "SELECT a FROM Auditoria a LEFT JOIN FETCH a.usuario ORDER BY a.fechaEvento DESC",
            Auditoria.class);
        return query.getResultList();
    }

    @Transactional
    public Auditoria crear(Auditoria auditoria) {
        em.persist(auditoria);
        em.flush();
        return auditoria;
    }
}
