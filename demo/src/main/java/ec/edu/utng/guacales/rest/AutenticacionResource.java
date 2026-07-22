package ec.edu.utng.guacales.rest;

import ec.edu.utng.guacales.dto.RegistroRequestDTO;
import ec.edu.utng.guacales.dto.SesionRequestDTO;
import ec.edu.utng.guacales.dto.UsuarioDTO;
import ec.edu.utng.guacales.dto.UsuarioSesionResponseDTO;
import ec.edu.utng.guacales.model.Rol;
import ec.edu.utng.guacales.model.Usuario;
import ec.edu.utng.guacales.repository.RolRepository;
import ec.edu.utng.guacales.repository.UsuarioRepository;
import ec.edu.utng.guacales.service.AuditoriaService;
import ec.edu.utng.guacales.service.UtnGolCoinClient;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.mindrot.jbcrypt.BCrypt;

@Path("/autenticacion")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AutenticacionResource {

    @Inject
    private UsuarioRepository usuarioRepository;

    @Inject
    private RolRepository rolRepository;

    @Inject
    private AuditoriaService auditoriaService;

    @Inject
    private UtnGolCoinClient utnGolCoinClient;

    @POST
    @Path("/registro")
    public Response registro(RegistroRequestDTO request) {
        if (request.getNombre() == null || request.getNombre().isBlank()
                || request.getCorreo() == null || request.getCorreo().isBlank()
                || request.getContrasena() == null || request.getContrasena().isBlank()) {
            return Response.status(422)
                    .entity("{\"codigo\":\"DATOS_INCOMPLETOS\",\"mensaje\":\"Faltan campos obligatorios: nombre, correo, contrasena\"}")
                    .build();
        }

        String correo = request.getCorreo().trim().toLowerCase();

        if (usuarioRepository.buscarPorEmail(correo) != null) {
            return Response.status(409)
                    .entity("{\"codigo\":\"CORREO_DUPLICADO\",\"mensaje\":\"El correo ya está registrado.\"}")
                    .build();
        }

        Rol rolUsuario = rolRepository.buscarPorNombre("USUARIO");
        if (rolUsuario == null) {
            return Response.status(500)
                    .entity("{\"codigo\":\"ROL_NO_CONFIGURADO\",\"mensaje\":\"El rol USUARIO no existe en la base de datos.\"}")
                    .build();
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(correo);
        usuario.setNombre(request.getNombre().trim());
        usuario.setEmail(correo);
        usuario.setPassword(BCrypt.hashpw(request.getContrasena(), BCrypt.gensalt()));
        usuario.setRol(rolUsuario);

        Usuario creado = usuarioRepository.crear(usuario);

        utnGolCoinClient.crearBilletera(creado.getId());

        auditoriaService.registrar(creado, "REGISTRO", "Usuario", creado.getId(), "Registro de usuario: " + correo);

        return Response.status(Response.Status.CREATED)
                .entity(new UsuarioSesionResponseDTO(convertir(creado)))
                .build();
    }

    @POST
    @Path("/sesion")
    public Response sesion(SesionRequestDTO request) {
        if (request.getCorreo() == null || request.getContrasena() == null) {
            return Response.status(422)
                    .entity("{\"codigo\":\"DATOS_INCOMPLETOS\",\"mensaje\":\"Faltan campos obligatorios: correo, contrasena\"}")
                    .build();
        }

        String correo = request.getCorreo().trim().toLowerCase();
        Usuario usuario = usuarioRepository.buscarPorEmail(correo);

        if (usuario == null || !BCrypt.checkpw(request.getContrasena(), usuario.getPassword())) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        auditoriaService.registrar(usuario, "LOGIN", "Usuario", usuario.getId(), "Inicio de sesión: " + correo);

        return Response.ok(new UsuarioSesionResponseDTO(convertir(usuario))).build();
    }

    private UsuarioDTO convertir(Usuario u) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(u.getId());
        dto.setNombre(u.getNombre());
        dto.setCorreo(u.getEmail());
        dto.setRol(u.getRol() != null ? u.getRol().getNombre() : null);
        return dto;
    }
}
