package edu.ucol.taller3jee.dto;

import java.util.List;

public class ProgresGlobalDTO {
    private Long estudianteId;
    private String nombreEstudiante;
    private Integer totalTemasCompletados;
    private Integer totalTemasActuales;
    private Double porcentajeCompletacion;
    private Double nivelComprensionPromedio;
    private List<ProgresEstudianteDTO> detallesPorTema;

    public ProgresGlobalDTO() {
    }

    public ProgresGlobalDTO(Long estudianteId, String nombreEstudiante, Integer totalTemasCompletados,
                            Integer totalTemasActuales, Double porcentajeCompletacion,
                            Double nivelComprensionPromedio, List<ProgresEstudianteDTO> detallesPorTema) {
        this.estudianteId = estudianteId;
        this.nombreEstudiante = nombreEstudiante;
        this.totalTemasCompletados = totalTemasCompletados;
        this.totalTemasActuales = totalTemasActuales;
        this.porcentajeCompletacion = porcentajeCompletacion;
        this.nivelComprensionPromedio = nivelComprensionPromedio;
        this.detallesPorTema = detallesPorTema;
    }

    public Long getEstudianteId() {
        return estudianteId;
    }

    public void setEstudianteId(Long estudianteId) {
        this.estudianteId = estudianteId;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public Integer getTotalTemasCompletados() {
        return totalTemasCompletados;
    }

    public void setTotalTemasCompletados(Integer totalTemasCompletados) {
        this.totalTemasCompletados = totalTemasCompletados;
    }

    public Integer getTotalTemasActuales() {
        return totalTemasActuales;
    }

    public void setTotalTemasActuales(Integer totalTemasActuales) {
        this.totalTemasActuales = totalTemasActuales;
    }

    public Double getPorcentajeCompletacion() {
        return porcentajeCompletacion;
    }

    public void setPorcentajeCompletacion(Double porcentajeCompletacion) {
        this.porcentajeCompletacion = porcentajeCompletacion;
    }

    public Double getNivelComprensionPromedio() {
        return nivelComprensionPromedio;
    }

    public void setNivelComprensionPromedio(Double nivelComprensionPromedio) {
        this.nivelComprensionPromedio = nivelComprensionPromedio;
    }

    public List<ProgresEstudianteDTO> getDetallesPorTema() {
        return detallesPorTema;
    }

    public void setDetallesPorTema(List<ProgresEstudianteDTO> detallesPorTema) {
        this.detallesPorTema = detallesPorTema;
    }
}
