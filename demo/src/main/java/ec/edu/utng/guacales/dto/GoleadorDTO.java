package ec.edu.utng.guacales.dto;

public class GoleadorDTO {
    private String jugador;
    private String seleccion;
    private Integer minuto;

    public GoleadorDTO() {}

    public GoleadorDTO(String jugador, String seleccion, Integer minuto) {
        this.jugador = jugador;
        this.seleccion = seleccion;
        this.minuto = minuto;
    }

    public String getJugador() { return jugador; }
    public void setJugador(String jugador) { this.jugador = jugador; }
    public String getSeleccion() { return seleccion; }
    public void setSeleccion(String seleccion) { this.seleccion = seleccion; }
    public Integer getMinuto() { return minuto; }
    public void setMinuto(Integer minuto) { this.minuto = minuto; }
}
