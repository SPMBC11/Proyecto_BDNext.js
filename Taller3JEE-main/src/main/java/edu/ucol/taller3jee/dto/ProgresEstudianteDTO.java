package edu.ucol.taller3jee.dto;

import java.time.LocalDateTime;

public class ProgresEstudianteDTO {
    private Long id;
    private Long estudianteId;
    private Long temaId;
    private String temaTitulo;
    private Integer nivelComprension;
    private Integer intentos;
    private Boolean completado;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaCompletacion;
    private LocalDateTime fechaUltimaActualizacion;

    public ProgresEstudianteDTO() {
    }

    public ProgresEstudianteDTO(Long id, Long estudianteId, Long temaId, String temaTitulo, Integer nivelComprension,
                                Integer intentos, Boolean completado, LocalDateTime fechaInicio,
                                LocalDateTime fechaCompletacion, LocalDateTime fechaUltimaActualizacion) {
        this.id = id;
        this.estudianteId = estudianteId;
        this.temaId = temaId;
        this.temaTitulo = temaTitulo;
        this.nivelComprension = nivelComprension;
        this.intentos = intentos;
        this.completado = completado;
        this.fechaInicio = fechaInicio;
        this.fechaCompletacion = fechaCompletacion;
        this.fechaUltimaActualizacion = fechaUltimaActualizacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEstudianteId() {
        return estudianteId;
    }

    public void setEstudianteId(Long estudianteId) {
        this.estudianteId = estudianteId;
    }

    public Long getTemaId() {
        return temaId;
    }

    public void setTemaId(Long temaId) {
        this.temaId = temaId;
    }

    public String getTemaTitulo() {
        return temaTitulo;
    }

    public void setTemaTitulo(String temaTitulo) {
        this.temaTitulo = temaTitulo;
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
