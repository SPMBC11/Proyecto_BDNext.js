package edu.ucol.taller3jee.dto;

import edu.ucol.taller3jee.entity.TipoRecomendacion;
import java.time.LocalDateTime;

public class RecomendacionDTO {
    private Long id;
    private Long estudianteId;
    private Long temaActualId;
    private Long temaRecomendadoId;
    private String temaRecomendadoTitulo;
    private TipoRecomendacion tipo;
    private String razon;
    private Double nivelConfianza;
    private LocalDateTime fechaCreacion;

    public RecomendacionDTO() {
    }

    public RecomendacionDTO(Long id, Long estudianteId, Long temaActualId, Long temaRecomendadoId,
                            String temaRecomendadoTitulo, TipoRecomendacion tipo, String razon,
                            Double nivelConfianza, LocalDateTime fechaCreacion) {
        this.id = id;
        this.estudianteId = estudianteId;
        this.temaActualId = temaActualId;
        this.temaRecomendadoId = temaRecomendadoId;
        this.temaRecomendadoTitulo = temaRecomendadoTitulo;
        this.tipo = tipo;
        this.razon = razon;
        this.nivelConfianza = nivelConfianza;
        this.fechaCreacion = fechaCreacion;
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

    public Long getTemaActualId() {
        return temaActualId;
    }

    public void setTemaActualId(Long temaActualId) {
        this.temaActualId = temaActualId;
    }

    public Long getTemaRecomendadoId() {
        return temaRecomendadoId;
    }

    public void setTemaRecomendadoId(Long temaRecomendadoId) {
        this.temaRecomendadoId = temaRecomendadoId;
    }

    public String getTemaRecomendadoTitulo() {
        return temaRecomendadoTitulo;
    }

    public void setTemaRecomendadoTitulo(String temaRecomendadoTitulo) {
        this.temaRecomendadoTitulo = temaRecomendadoTitulo;
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
