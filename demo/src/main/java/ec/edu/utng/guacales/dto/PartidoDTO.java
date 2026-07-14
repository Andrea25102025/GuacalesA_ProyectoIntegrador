package ec.edu.utng.guacales.dto;

import java.time.LocalDateTime;

public class PartidoDTO {
    private Long id;
    private Integer numeroPartidoFifa;
    private Long seleccionLocalId;
    private String seleccionLocalNombre;
    private Long seleccionVisitanteId;
    private String seleccionVisitanteNombre;
    private String sedeNombre;
    private String grupoCodigo;
    private LocalDateTime fechaPartido;
    private String fase;
    private Integer golesLocal;
    private Integer golesVisitante;
    private String estado;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getNumeroPartidoFifa() { return numeroPartidoFifa; }
    public void setNumeroPartidoFifa(Integer numeroPartidoFifa) { this.numeroPartidoFifa = numeroPartidoFifa; }
    public Long getSeleccionLocalId() { return seleccionLocalId; }
    public void setSeleccionLocalId(Long seleccionLocalId) { this.seleccionLocalId = seleccionLocalId; }
    public String getSeleccionLocalNombre() { return seleccionLocalNombre; }
    public void setSeleccionLocalNombre(String seleccionLocalNombre) { this.seleccionLocalNombre = seleccionLocalNombre; }
    public Long getSeleccionVisitanteId() { return seleccionVisitanteId; }
    public void setSeleccionVisitanteId(Long seleccionVisitanteId) { this.seleccionVisitanteId = seleccionVisitanteId; }
    public String getSeleccionVisitanteNombre() { return seleccionVisitanteNombre; }
    public void setSeleccionVisitanteNombre(String seleccionVisitanteNombre) { this.seleccionVisitanteNombre = seleccionVisitanteNombre; }
    public String getSedeNombre() { return sedeNombre; }
    public void setSedeNombre(String sedeNombre) { this.sedeNombre = sedeNombre; }
    public String getGrupoCodigo() { return grupoCodigo; }
    public void setGrupoCodigo(String grupoCodigo) { this.grupoCodigo = grupoCodigo; }
    public LocalDateTime getFechaPartido() { return fechaPartido; }
    public void setFechaPartido(LocalDateTime fechaPartido) { this.fechaPartido = fechaPartido; }
    public String getFase() { return fase; }
    public void setFase(String fase) { this.fase = fase; }
    public Integer getGolesLocal() { return golesLocal; }
    public void setGolesLocal(Integer golesLocal) { this.golesLocal = golesLocal; }
    public Integer getGolesVisitante() { return golesVisitante; }
    public void setGolesVisitante(Integer golesVisitante) { this.golesVisitante = golesVisitante; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
