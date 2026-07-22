package ec.edu.utng.guacales.dto;
public class UsuarioSesionResponseDTO {
    private UsuarioDTO usuario;
    public UsuarioSesionResponseDTO() {}
    public UsuarioSesionResponseDTO(UsuarioDTO usuario) { this.usuario = usuario; }
    public UsuarioDTO getUsuario() { return usuario; }
    public void setUsuario(UsuarioDTO usuario) { this.usuario = usuario; }
}
