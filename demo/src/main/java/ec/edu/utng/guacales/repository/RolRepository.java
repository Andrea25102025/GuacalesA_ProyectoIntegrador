package ec.edu.utng.guacales.repository;
import ec.edu.utng.guacales.model.Rol;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@ApplicationScoped
public class RolRepository {
    @PersistenceContext(unitName = "guacalesPU")
    private EntityManager em;

    public Rol buscarPorNombre(String nombre) {
        TypedQuery<Rol> query = em.createQuery(
                "SELECT r FROM Rol r WHERE r.nombre = :nombre", Rol.class);
        query.setParameter("nombre", nombre);
        List<Rol> resultado = query.getResultList();
        return resultado.isEmpty() ? null : resultado.get(0);
    }
}
