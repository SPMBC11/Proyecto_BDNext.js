package edu.ucol.taller3jee.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "progres_estudiante", uniqueConstraints = @UniqueConstraint(columnNames = {"estudiante_id", "tema_id"}))
public class ProgresEstudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tema_id", nullable = false)
    private Tema tema;

    @Column(name = "nivel_comprension", nullable = false)
    private Integer nivelComprension;

    @Column(nullable = false)
    private Integer intentos;

    @Column(nullable = false)
    private Boolean completado;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_completacion")
    private LocalDateTime fechaCompletacion;

    @Column(name = "fecha_ultima_actualizacion", nullable = false)
    private LocalDateTime fechaUltimaActualizacion;

    public ProgresEstudiante() {
        this.nivelComprension = 0;
        this.intentos = 0;
        this.completado = false;
        this.fechaInicio = LocalDateTime.now();
        this.fechaUltimaActualizacion = LocalDateTime.now();
    }

    public ProgresEstudiante(Estudiante estudiante, Tema tema) {
        this();
        this.estudiante = estudiante;
        this.tema = tema;
    }

    public void actualizarComprension(Integer nuevoNivel) {
        this.nivelComprension = nuevoNivel;
        this.fechaUltimaActualizacion = LocalDateTime.now();
        if (nuevoNivel != null && nuevoNivel >= 80) {
            this.completado = true;
            if (this.fechaCompletacion == null) {
                this.fechaCompletacion = LocalDateTime.now();
            }
        }
    }

    public void incrementarIntento() {
        this.intentos = this.intentos + 1;
        this.fechaUltimaActualizacion = LocalDateTime.now();
    }

    public void marcarCompletado() {
        this.completado = true;
        this.fechaCompletacion = LocalDateTime.now();
        this.fechaUltimaActualizacion = LocalDateTime.now();
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

    public Tema getTema() {
        return tema;
    }

    public void setTema(Tema tema) {
        this.tema = tema;
    }

    public Integer getNivelComprension() {
        return nivelComprension;
    }

    public void setNivelComprension(Integer nivelComprension) {
        this.nivelComprension = nivelComprension;
    }

    public Integer getIntentos() {
        return intentos;
    }

    public void setIntentos(Integer intentos) {
        this.intentos = intentos;
    }

    public Boolean getCompletado() {
        return completado;
    }

    public void setCompletado(Boolean completado) {
        this.completado = completado;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaCompletacion() {
        return fechaCompletacion;
    }

    public void setFechaCompletacion(LocalDateTime fechaCompletacion) {
        this.fechaCompletacion = fechaCompletacion;
    }

    public LocalDateTime getFechaUltimaActualizacion() {
        return fechaUltimaActualizacion;
    }

    public void setFechaUltimaActualizacion(LocalDateTime fechaUltimaActualizacion) {
        this.fechaUltimaActualizacion = fechaUltimaActualizacion;
    }
}
