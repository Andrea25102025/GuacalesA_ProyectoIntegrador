package ec.edu.utng.guacales.dto;
import java.util.List;
/**
 * DTO de partido. Expone tanto el shape anidado (cuotas) como alias planos
 * (fechaHoraUtc, cuotaLocal, ...) para que UTNGolCoin y el frontend público
 * puedan deserializar sin adaptadores adicionales.
 */
public class PartidoDTO {
    private Long id;
    private Integer numeroPartidoFifa;
    private SeleccionDTO seleccionLocal;
    private SeleccionDTO seleccionVisitante;
    private String fechaHora;
    private String fechaActualizacion;
    private String sede;
    private String fase;
    private String grupo;
    private String estado;
    private Integer golesLocal;
    private Integer golesVisitante;
    private CuotasPartidoDTO cuotas;
    private List<GoleadorDTO> goleadores;
    public PartidoDTO() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getNumeroPartidoFifa() { return numeroPartidoFifa; }
    public void setNumeroPartidoFifa(Integer numeroPartidoFifa) { this.numeroPartidoFifa = numeroPartidoFifa; }
    public SeleccionDTO getSeleccionLocal() { return seleccionLocal; }
    public void setSeleccionLocal(SeleccionDTO seleccionLocal) { this.seleccionLocal = seleccionLocal; }
    public SeleccionDTO getSeleccionVisitante() { return seleccionVisitante; }
    public void setSeleccionVisitante(SeleccionDTO seleccionVisitante) { this.seleccionVisitante = seleccionVisitante; }
    public String getFechaHora() { return fechaHora; }
    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }
    /** Alias para clientes que esperan fechaHoraUtc (UTNGolCoin). */
    public String getFechaHoraUtc() { return fechaHora; }
    public String getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(String fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    public String getSede() { return sede; }
    public void setSede(String sede) { this.sede = sede; }
    public String getFase() { return fase; }
    public void setFase(String fase) { this.fase = fase; }
    public String getGrupo() { return grupo; }
    public void setGrupo(String grupo) { this.grupo = grupo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Integer getGolesLocal() { return golesLocal; }
    public void setGolesLocal(Integer golesLocal) { this.golesLocal = golesLocal; }
    public Integer getGolesVisitante() { return golesVisitante; }
    public void setGolesVisitante(Integer golesVisitante) { this.golesVisitante = golesVisitante; }
    public CuotasPartidoDTO getCuotas() { return cuotas; }
    public void setCuotas(CuotasPartidoDTO cuotas) { this.cuotas = cuotas; }
    public List<GoleadorDTO> getGoleadores() { return goleadores; }
    public void setGoleadores(List<GoleadorDTO> goleadores) { this.goleadores = goleadores; }
    public Double getCuotaLocal() { return cuotas != null ? cuotas.getLocal() : null; }
    public Double getCuotaEmpate() { return cuotas != null ? cuotas.getEmpate() : null; }
    public Double getCuotaVisitante() { return cuotas != null ? cuotas.getVisitante() : null; }
}
