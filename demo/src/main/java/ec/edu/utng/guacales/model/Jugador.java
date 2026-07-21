package ec.edu.utng.guacales.model;

import jakarta.persistence.*;

@Entity
@Table(name = "jugador")
public class Jugador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seleccion_id", nullable = false)
    private Seleccion seleccion;

    @Column(name = "numero_camiseta")
    private Integer numeroCamiseta;

    @Column(length = 30)
    private String posicion;

    public Jugador() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Seleccion getSeleccion() { return seleccion; }
    public void setSeleccion(Seleccion seleccion) { this.seleccion = seleccion; }
    public Integer getNumeroCamiseta() { return numeroCamiseta; }
    public void setNumeroCamiseta(Integer numeroCamiseta) { this.numeroCamiseta = numeroCamiseta; }
    public String getPosicion() { return posicion; }
    public void setPosicion(String posicion) { this.posicion = posicion; }
}
