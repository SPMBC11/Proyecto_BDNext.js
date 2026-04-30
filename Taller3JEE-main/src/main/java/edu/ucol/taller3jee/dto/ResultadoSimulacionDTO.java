package edu.ucol.taller3jee.dto;

import java.time.LocalDateTime;

public class ResultadoSimulacionDTO {
    private Long estudianteId;
    private Long temaId;
    private Integer aciertos;
    private Integer total;
    private Integer puntuacion;
    private Integer nivelComprensionActualizado;
    private Boolean completado;
    private LocalDateTime timestamp;

    public ResultadoSimulacionDTO() {
    }

    public ResultadoSimulacionDTO(Long estudianteId, Long temaId, Integer aciertos, Integer total,
                                  Integer puntuacion, Integer nivelComprensionActualizado,
                                  Boolean completado, LocalDateTime timestamp) {
        this.estudianteId = estudianteId;
        this.temaId = temaId;
        this.aciertos = aciertos;
        this.total = total;
        this.puntuacion = puntuacion;
        this.nivelComprensionActualizado = nivelComprensionActualizado;
        this.completado = completado;
        this.timestamp = timestamp;
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

    public Integer getAciertos() {
        return aciertos;
    }

    public void setAciertos(Integer aciertos) {
        this.aciertos = aciertos;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Integer puntuacion) {
        this.puntuacion = puntuacion;
    }

    public Integer getNivelComprensionActualizado() {
        return nivelComprensionActualizado;
    }

    public void setNivelComprensionActualizado(Integer nivelComprensionActualizado) {
        this.nivelComprensionActualizado = nivelComprensionActualizado;
    }

    public Boolean getCompletado() {
        return completado;
    }

    public void setCompletado(Boolean completado) {
        this.completado = completado;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
