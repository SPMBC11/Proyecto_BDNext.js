package edu.ucol.taller3jee.dto;

public class SimulacionLoteDTO {
    private Long estudianteId;
    private Long temaId;
    private Integer cantidad;

    public SimulacionLoteDTO() {
    }

    public SimulacionLoteDTO(Long estudianteId, Long temaId, Integer cantidad) {
        this.estudianteId = estudianteId;
        this.temaId = temaId;
        this.cantidad = cantidad;
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

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
