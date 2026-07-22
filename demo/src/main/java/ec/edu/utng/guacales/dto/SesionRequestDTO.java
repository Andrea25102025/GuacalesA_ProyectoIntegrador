package ec.edu.utng.guacales.dto;
public class SesionRequestDTO {
    private String correo;
    private String contrasena;
    public SesionRequestDTO() {}
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
}
