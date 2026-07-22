package ec.edu.utng.guacales.rest;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import ec.edu.utng.guacales.model.Usuario;
import ec.edu.utng.guacales.model.Rol;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    @PersistenceContext(unitName = "guacalesPU")
    private EntityManager em;

    @GET
    public Response listar() {
        List<Usuario> usuarios = em.createQuery("SELECT u FROM Usuario u", Usuario.class)
                                    .getResultList();
        return Response.ok(usuarios).build();
    }

    @GET
    @Path("/{id}")
    public Response obtener(@PathParam("id") Long id) {
        Usuario u = em.find(Usuario.class, id);
        if (u == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(u).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response editar(@PathParam("id") Long id, Usuario datos) {
        Usuario u = em.find(Usuario.class, id);
        if (u == null) return Response.status(Response.Status.NOT_FOUND).build();
        u.setNombre(datos.getNombre());
        u.setEmail(datos.getEmail());
        u.setUsername(datos.getUsername());
        return Response.ok(u).build();
    }

    @PUT
    @Path("/{id}/estado")
    @Transactional
    public Response cambiarEstado(@PathParam("id") Long id, Map<String, Boolean> body) {
        Usuario u = em.find(Usuario.class, id);
        if (u == null) return Response.status(Response.Status.NOT_FOUND).build();
        u.setActivo(body.get("activo"));
        return Response.ok(u).build();
    }

    @PUT
    @Path("/{id}/rol")
    @Transactional
    public Response cambiarRol(@PathParam("id") Long id, Map<String, Long> body) {
        Usuario u = em.find(Usuario.class, id);
        if (u == null) return Response.status(Response.Status.NOT_FOUND).build();
        Rol rol = em.find(Rol.class, body.get("rolId"));
        u.setRol(rol);
        return Response.ok(u).build();
    }
}
