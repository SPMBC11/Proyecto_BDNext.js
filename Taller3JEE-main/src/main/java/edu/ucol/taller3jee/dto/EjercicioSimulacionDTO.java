package edu.ucol.taller3jee.dto;

public class EjercicioSimulacionDTO {
    private Long estudianteId;
    private Long temaId;

    public EjercicioSimulacionDTO() {
    }

    public EjercicioSimulacionDTO(Long estudianteId, Long temaId) {
        this.estudianteId = estudianteId;
        this.temaId = temaId;
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
}
