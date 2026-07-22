package ec.edu.utng.guacales.service;

import ec.edu.utng.guacales.model.Auditoria;
import ec.edu.utng.guacales.model.Usuario;
import ec.edu.utng.guacales.repository.AuditoriaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class AuditoriaService {

    @Inject
    private AuditoriaRepository auditoriaRepository;

    public void registrar(Usuario usuario, String accion, String entidad, Long entidadId, String detalle) {
        Auditoria auditoria = new Auditoria();
        auditoria.setUsuario(usuario);
        auditoria.setAccion(accion);
        auditoria.setEntidad(entidad);
        auditoria.setEntidadId(entidadId);
        auditoria.setDetalle(detalle);
        auditoriaRepository.crear(auditoria);
    }

    public List<Auditoria> listar() {
        return auditoriaRepository.listar();
    }
}
