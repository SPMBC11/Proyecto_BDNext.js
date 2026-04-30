package edu.ucol.taller3jee.dto;

import java.util.List;

public class AnalisisProgresoDTO {
    private Long estudianteId;
    private Integer temasCompletados;
    private Integer temasEnProgreso;
    private Integer temasPendientes;
    private Double promedioComprension;
    private Long tema_id_actual;
    private String tema_actual_titulo;
    private String recomendacionGeneral;
    private List<ProgresEstudianteDTO> temasConBajoRendimiento;
    private List<ProgresEstudianteDTO> temasConBuenRendimiento;

    public AnalisisProgresoDTO() {
    }

    public AnalisisProgresoDTO(Long estudianteId, Integer temasCompletados, Integer temasEnProgreso,
                               Integer temasPendientes, Double promedioComprension, Long tema_id_actual,
                               String tema_actual_titulo, String recomendacionGeneral,
                               List<ProgresEstudianteDTO> temasConBajoRendimiento,
                               List<ProgresEstudianteDTO> temasConBuenRendimiento) {
        this.estudianteId = estudianteId;
        this.temasCompletados = temasCompletados;
        this.temasEnProgreso = temasEnProgreso;
        this.temasPendientes = temasPendientes;
        this.promedioComprension = promedioComprension;
        this.tema_id_actual = tema_id_actual;
        this.tema_actual_titulo = tema_actual_titulo;
        this.recomendacionGeneral = recomendacionGeneral;
        this.temasConBajoRendimiento = temasConBajoRendimiento;
        this.temasConBuenRendimiento = temasConBuenRendimiento;
    }

    public Long getEstudianteId() {
        return estudianteId;
    }

    public void setEstudianteId(Long estudianteId) {
        this.estudianteId = estudianteId;
    }

    public Integer getTemasCompletados() {
        return temasCompletados;
    }

    public void setTemasCompletados(Integer temasCompletados) {
        this.temasCompletados = temasCompletados;
    }

    public Integer getTemasEnProgreso() {
        return temasEnProgreso;
    }

    public void setTemasEnProgreso(Integer temasEnProgreso) {
        this.temasEnProgreso = temasEnProgreso;
    }

    public Integer getTemasPendientes() {
        return temasPendientes;
    }

    public void setTemasPendientes(Integer temasPendientes) {
        this.temasPendientes = temasPendientes;
    }

    public Double getPromedioComprension() {
        return promedioComprension;
    }

    public void setPromedioComprension(Double promedioComprension) {
        this.promedioComprension = promedioComprension;
    }

    public Long getTema_id_actual() {
        return tema_id_actual;
    }

    public void setTema_id_actual(Long tema_id_actual) {
        this.tema_id_actual = tema_id_actual;
    }

    public String getTema_actual_titulo() {
        return tema_actual_titulo;
    }

    public void setTema_actual_titulo(String tema_actual_titulo) {
        this.tema_actual_titulo = tema_actual_titulo;
    }

    public String getRecomendacionGeneral() {
        return recomendacionGeneral;
    }

    public void setRecomendacionGeneral(String recomendacionGeneral) {
        this.recomendacionGeneral = recomendacionGeneral;
    }

    public List<ProgresEstudianteDTO> getTemasConBajoRendimiento() {
        return temasConBajoRendimiento;
    }

    public void setTemasConBajoRendimiento(List<ProgresEstudianteDTO> temasConBajoRendimiento) {
        this.temasConBajoRendimiento = temasConBajoRendimiento;
    }

    public List<ProgresEstudianteDTO> getTemasConBuenRendimiento() {
        return temasConBuenRendimiento;
    }

    public void setTemasConBuenRendimiento(List<ProgresEstudianteDTO> temasConBuenRendimiento) {
        this.temasConBuenRendimiento = temasConBuenRendimiento;
    }
}
