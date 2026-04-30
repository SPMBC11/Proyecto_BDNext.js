package edu.ucol.taller3jee.dto;

import edu.ucol.taller3jee.entity.TipoTema;
import java.time.LocalDateTime;

public class TemaDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private TipoTema tipo;
    private Integer numeroOrden;
    private String objetivos;
    private Integer duracionEstimada;
    private LocalDateTime fechaCreacion;

    public TemaDTO() {
    }

    public TemaDTO(Long id, String titulo, String descripcion, TipoTema tipo, Integer numeroOrden, String objetivos,
                   Integer duracionEstimada, LocalDateTime fechaCreacion) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.numeroOrden = numeroOrden;
        this.objetivos = objetivos;
        this.duracionEstimada = duracionEstimada;
        this.fechaCreacion = fechaCreacion;
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
