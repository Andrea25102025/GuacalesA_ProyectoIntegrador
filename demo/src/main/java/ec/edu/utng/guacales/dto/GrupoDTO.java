package ec.edu.utng.guacales.dto;

import java.util.List;

public class GrupoDTO {
    private Long id;
    private String nombre;
    private String codigo;
    private List<SeleccionDTO> selecciones;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public List<SeleccionDTO> getSelecciones() { return selecciones; }
    public void setSelecciones(List<SeleccionDTO> selecciones) { this.selecciones = selecciones; }
}
