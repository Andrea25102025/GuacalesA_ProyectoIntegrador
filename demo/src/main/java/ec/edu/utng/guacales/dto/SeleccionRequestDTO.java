package ec.edu.utng.guacales.dto;

public class SeleccionRequestDTO {
    private String nombre;
    private String codigoFifa;
    private String confederacion;
    private Boolean esAnfitrion;
    private String clasificacion;
    private String banderaUrl;
    private Long grupoId;

    public SeleccionRequestDTO() {}

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCodigoFifa() { return codigoFifa; }
    public void setCodigoFifa(String codigoFifa) { this.codigoFifa = codigoFifa; }
    public String getConfederacion() { return confederacion; }
    public void setConfederacion(String confederacion) { this.confederacion = confederacion; }
    public Boolean getEsAnfitrion() { return esAnfitrion; }
    public void setEsAnfitrion(Boolean esAnfitrion) { this.esAnfitrion = esAnfitrion; }
    public String getClasificacion() { return clasificacion; }
    public void setClasificacion(String clasificacion) { this.clasificacion = clasificacion; }
    public String getBanderaUrl() { return banderaUrl; }
    public void setBanderaUrl(String banderaUrl) { this.banderaUrl = banderaUrl; }
    public Long getGrupoId() { return grupoId; }
    public void setGrupoId(Long grupoId) { this.grupoId = grupoId; }
}
