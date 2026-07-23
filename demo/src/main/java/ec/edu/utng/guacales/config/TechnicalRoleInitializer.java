package ec.edu.utng.guacales.config;

import ec.edu.utng.guacales.model.Rol;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Singleton
@Startup
public class TechnicalRoleInitializer {

    @PersistenceContext(unitName = "guacalesPU")
    private EntityManager em;

    @PostConstruct
    public void inicializar() {
        crearSiNoExiste("USUARIO", "Usuario de la aplicación");
        crearSiNoExiste("ADMINISTRADOR", "Administrador de la aplicación");
    }

    private void crearSiNoExiste(String nombre, String descripcion) {
        Long existentes = em.createQuery(
                        "SELECT COUNT(r) FROM Rol r WHERE r.nombre = :nombre", Long.class)
                .setParameter("nombre", nombre)
                .getSingleResult();
        if (existentes == 0) {
            Rol rol = new Rol();
            rol.setNombre(nombre);
            rol.setDescripcion(descripcion);
            em.persist(rol);
        }
    }
}
