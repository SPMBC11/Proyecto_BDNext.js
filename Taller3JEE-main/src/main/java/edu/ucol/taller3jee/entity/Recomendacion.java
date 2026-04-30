package edu.ucol.taller3jee.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "recomendacion")
public class Recomendacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tema_actual_id")
    private Tema temaActual;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tema_recomendado_id", nullable = false)
    private Tema temaRecomendado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoRecomendacion tipo;

    @Column(nullable = false, length = 2000)
    private String razon;

    @Column(name = "nivel_confianza", nullable = false)
    private Double nivelConfianza;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    public Recomendacion() {
        this.fechaCreacion = LocalDateTime.now();
    }

    public Recomendacion(Estudiante estudiante, Tema temaActual, Tema temaRecomendado, TipoRecomendacion tipo, String razon, Double nivelConfianza) {
        this.estudiante = estudiante;
        this.temaActual = temaActual;
        this.temaRecomendado = temaRecomendado;
        this.tipo = tipo;
        this.razon = razon;
        this.nivelConfianza = nivelConfianza;
        this.fechaCreacion = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Tema getTemaActual() {
        return temaActual;
    }

    public void setTemaActual(Tema temaActual) {
        this.temaActual = temaActual;
    }

    public Tema getTemaRecomendado() {
        return temaRecomendado;
    }

    public void setTemaRecomendado(Tema temaRecomendado) {
        this.temaRecomendado = temaRecomendado;
    }

    public TipoRecomendacion getTipo() {
        return tipo;
    }

    public void setTipo(TipoRecomendacion tipo) {
        this.tipo = tipo;
    }

    public String getRazon() {
        return razon;
    }

    public void setRazon(String razon) {
        this.razon = razon;
    }

    public Double getNivelConfianza() {
        return nivelConfianza;
    }

    public void setNivelConfianza(Double nivelConfianza) {
        this.nivelConfianza = nivelConfianza;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
