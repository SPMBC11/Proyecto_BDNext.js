package edu.ucol.taller3jee.dto;

import java.util.List;

public class ResultadoLoteDTO {
    private Long estudianteId;
    private Long temaId;
    private Integer totalEjercicios;
    private Integer totalAciertos;
    private Integer promedioPuntuacion;
    private Boolean temaSuperado;
    private List<ResultadoSimulacionDTO> detallesEjercicios;

    public ResultadoLoteDTO() {
    }

    public ResultadoLoteDTO(Long estudianteId, Long temaId, Integer totalEjercicios, Integer totalAciertos,
                            Integer promedioPuntuacion, Boolean temaSuperado,
                            List<ResultadoSimulacionDTO> detallesEjercicios) {
        this.estudianteId = estudianteId;
        this.temaId = temaId;
        this.totalEjercicios = totalEjercicios;
        this.totalAciertos = totalAciertos;
        this.promedioPuntuacion = promedioPuntuacion;
        this.temaSuperado = temaSuperado;
        this.detallesEjercicios = detallesEjercicios;
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

    public Integer getTotalEjercicios() {
        return totalEjercicios;
    }

    public void setTotalEjercicios(Integer totalEjercicios) {
        this.totalEjercicios = totalEjercicios;
    }

    public Integer getTotalAciertos() {
        return totalAciertos;
    }

    public void setTotalAciertos(Integer totalAciertos) {
        this.totalAciertos = totalAciertos;
    }

    public Integer getPromedioPuntuacion() {
        return promedioPuntuacion;
    }

    public void setPromedioPuntuacion(Integer promedioPuntuacion) {
        this.promedioPuntuacion = promedioPuntuacion;
    }

    public Boolean getTemaSuperado() {
        return temaSuperado;
    }

    public void setTemaSuperado(Boolean temaSuperado) {
        this.temaSuperado = temaSuperado;
    }

    public List<ResultadoSimulacionDTO> getDetallesEjercicios() {
        return detallesEjercicios;
    }

    public void setDetallesEjercicios(List<ResultadoSimulacionDTO> detallesEjercicios) {
        this.detallesEjercicios = detallesEjercicios;
    }
}
