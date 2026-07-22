package ec.edu.utng.guacales.dto;
public class RegistroRequestDTO {
    private String nombre;
    private String correo;
    private String contrasena;
    public RegistroRequestDTO() {}
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
}
