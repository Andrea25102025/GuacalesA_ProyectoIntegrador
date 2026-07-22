package ec.edu.utng.guacales.repository;
import ec.edu.utng.guacales.model.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class UsuarioRepository {
    @PersistenceContext(unitName = "guacalesPU")
    private EntityManager em;

    private static final String BASE_QUERY =
        "SELECT u FROM Usuario u LEFT JOIN FETCH u.rol WHERE 1=1";

    public Usuario buscarPorEmail(String email) {
        TypedQuery<Usuario> query = em.createQuery(
                BASE_QUERY + " AND u.email = :email", Usuario.class);
        query.setParameter("email", email);
        List<Usuario> resultado = query.getResultList();
        return resultado.isEmpty() ? null : resultado.get(0);
    }

    @Transactional
    public Usuario crear(Usuario usuario) {
        em.persist(usuario);
        em.flush();
        return usuario;
    }
}
