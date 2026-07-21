package ec.edu.utng.guacales.model;

import jakarta.persistence.*;

@Entity
@Table(name = "gol")
public class Gol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partido_id", nullable = false)
    private Partido partido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jugador_id", nullable = false)
    private Jugador jugador;

    @Column(nullable = false)
    private Integer minuto;

    public Gol() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Partido getPartido() { return partido; }
    public void setPartido(Partido partido) { this.partido = partido; }
    public Jugador getJugador() { return jugador; }
    public void setJugador(Jugador jugador) { this.jugador = jugador; }
    public Integer getMinuto() { return minuto; }
    public void setMinuto(Integer minuto) { this.minuto = minuto; }
}
