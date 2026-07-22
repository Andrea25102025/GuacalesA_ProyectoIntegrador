package ec.edu.utng.guacales.repository;
import ec.edu.utng.guacales.model.Partido;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
@ApplicationScoped
public class PartidoRepository {
    @PersistenceContext(unitName = "guacalesPU")
    private EntityManager em;
    private static final String BASE_QUERY =
    "SELECT p FROM Partido p " +
    "LEFT JOIN FETCH p.seleccionLocal " +
    "LEFT JOIN FETCH p.seleccionLocal.grupo " +
    "LEFT JOIN FETCH p.seleccionVisitante " +
    "LEFT JOIN FETCH p.seleccionVisitante.grupo " +
    "LEFT JOIN FETCH p.sede " +
    "LEFT JOIN FETCH p.grupo " +
    "WHERE 1=1";
    public List<Partido> listar(String estado, String grupoNombre, String fase, LocalDate fecha) {
        StringBuilder jpql = new StringBuilder(BASE_QUERY);
        if (estado != null) jpql.append(" AND p.estado = :estado");
        if (grupoNombre != null) jpql.append(" AND p.grupo.nombre = :grupoNombre");
        if (fase != null) jpql.append(" AND p.fase = :fase");
        if (fecha != null) jpql.append(" AND FUNCTION('DATE', p.fechaPartido) = :fecha");
        jpql.append(" ORDER BY p.fechaPartido");
        TypedQuery<Partido> query = em.createQuery(jpql.toString(), Partido.class);
        if (estado != null) query.setParameter("estado", estado);
        if (grupoNombre != null) query.setParameter("grupoNombre", grupoNombre);
        if (fase != null) query.setParameter("fase", fase);
        if (fecha != null) query.setParameter("fecha", fecha);
        return query.getResultList();
    }
    public Partido buscarPorId(Long id) {
        TypedQuery<Partido> query = em.createQuery(
        BASE_QUERY + " AND p.id = :id", Partido.class);
        query.setParameter("id", id);
        List<Partido> resultado = query.getResultList();
        return resultado.isEmpty() ? null : resultado.get(0);
    }
    public Partido buscarPorNumeroFifa(Integer numeroPartidoFifa) {
        TypedQuery<Partido> query = em.createQuery(
                BASE_QUERY + " AND p.numeroPartidoFifa = :numero", Partido.class);
        query.setParameter("numero", numeroPartidoFifa);
        List<Partido> resultado = query.getResultList();
        return resultado.isEmpty() ? null : resultado.get(0);
    }
    @Transactional
    public Partido crear(Partido partido) {
        em.persist(partido);
        em.flush();
        return partido;
    }
    @Transactional
    public Partido actualizar(Partido partido) {
        Partido actualizado = em.merge(partido);
        em.flush();
        return actualizado;
    }
    @Transactional
    public void eliminar(Partido partido) {
        Partido gestionado = em.merge(partido);
        em.remove(gestionado);
    }
}
