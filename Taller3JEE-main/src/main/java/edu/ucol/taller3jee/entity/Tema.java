package edu.ucol.taller3jee.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tema", uniqueConstraints = @UniqueConstraint(columnNames = "numero_orden"))
public class Tema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(length = 2000)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTema tipo;

    @Column(name = "numero_orden", nullable = false)
    private Integer numeroOrden;

    @Column(length = 2000)
    private String objetivos;

    @Column(name = "duracion_estimada")
    private Integer duracionEstimada;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    public Tema() {
        this.fechaCreacion = LocalDateTime.now();
    }

    public Tema(String titulo, String descripcion, TipoTema tipo, Integer numeroOrden, String objetivos, Integer duracionEstimada) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.numeroOrden = numeroOrden;
        this.objetivos = objetivos;
        this.duracionEstimada = duracionEstimada;
        this.fechaCreacion = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoTema getTipo() {
        return tipo;
    }

    public void setTipo(TipoTema tipo) {
        this.tipo = tipo;
    }

    public Integer getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(Integer numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public String getObjetivos() {
        return objetivos;
    }

    public void setObjetivos(String objetivos) {
        this.objetivos = objetivos;
    }

    public Integer getDuracionEstimada() {
        return duracionEstimada;
    }

    public void setDuracionEstimada(Integer duracionEstimada) {
        this.duracionEstimada = duracionEstimada;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
