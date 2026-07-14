package ec.edu.utng.guacales.dto;

public class SeleccionDTO {
    private Long id;
    private String nombre;
    private String codigoFifa;
    private String confederacion;
    private Boolean esAnfitrion;
    private String clasificacion;
    private String grupoCodigo;
    private int puntos;
    private int partidosJugados;
    private int partidosGanados;
    private int partidosEmpatados;
    private int partidosPerdidos;
    private int golesFavor;
    private int golesContra;

    public SeleccionDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public String getGrupoCodigo() { return grupoCodigo; }
    public void setGrupoCodigo(String grupoCodigo) { this.grupoCodigo = grupoCodigo; }
    public int getPuntos() { return puntos; }
    public void setPuntos(int puntos) { this.puntos = puntos; }
    public int getPartidosJugados() { return partidosJugados; }
    public void setPartidosJugados(int partidosJugados) { this.partidosJugados = partidosJugados; }
    public int getPartidosGanados() { return partidosGanados; }
    public void setPartidosGanados(int partidosGanados) { this.partidosGanados = partidosGanados; }
    public int getPartidosEmpatados() { return partidosEmpatados; }
    public void setPartidosEmpatados(int partidosEmpatados) { this.partidosEmpatados = partidosEmpatados; }
    public int getPartidosPerdidos() { return partidosPerdidos; }
    public void setPartidosPerdidos(int partidosPerdidos) { this.partidosPerdidos = partidosPerdidos; }
    public int getGolesFavor() { return golesFavor; }
    public void setGolesFavor(int golesFavor) { this.golesFavor = golesFavor; }
    public int getGolesContra() { return golesContra; }
    public void setGolesContra(int golesContra) { this.golesContra = golesContra; }
}
