package ec.edu.utng.guacales.dto;

public class PartidoRequestDTO {

    private Integer numeroPartidoFifa;
    private Long seleccionLocalId;
    private Long seleccionVisitanteId;
    private Long sedeId;
    private Long grupoId;
    private String fechaHora;
    private String fase;
    private String estado;
    private Double cuotaLocal;
    private Double cuotaEmpate;
    private Double cuotaVisitante;

    public PartidoRequestDTO() {}

    public Integer getNumeroPartidoFifa() { return numeroPartidoFifa; }
    public void setNumeroPartidoFifa(Integer numeroPartidoFifa) { this.numeroPartidoFifa = numeroPartidoFifa; }

    public Long getSeleccionLocalId() { return seleccionLocalId; }
    public void setSeleccionLocalId(Long seleccionLocalId) { this.seleccionLocalId = seleccionLocalId; }

    public Long getSeleccionVisitanteId() { return seleccionVisitanteId; }
    public void setSeleccionVisitanteId(Long seleccionVisitanteId) { this.seleccionVisitanteId = seleccionVisitanteId; }

    public Long getSedeId() { return sedeId; }
    public void setSedeId(Long sedeId) { this.sedeId = sedeId; }

    public Long getGrupoId() { return grupoId; }
    public void setGrupoId(Long grupoId) { this.grupoId = grupoId; }

    public String getFechaHora() { return fechaHora; }
    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }

    public String getFase() { return fase; }
    public void setFase(String fase) { this.fase = fase; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Double getCuotaLocal() { return cuotaLocal; }
    public void setCuotaLocal(Double cuotaLocal) { this.cuotaLocal = cuotaLocal; }

    public Double getCuotaEmpate() { return cuotaEmpate; }
    public void setCuotaEmpate(Double cuotaEmpate) { this.cuotaEmpate = cuotaEmpate; }

    public Double getCuotaVisitante() { return cuotaVisitante; }
    public void setCuotaVisitante(Double cuotaVisitante) { this.cuotaVisitante = cuotaVisitante; }
}