package edu.ucol.taller3jee.service;

import edu.ucol.taller3jee.dto.*;
import java.util.List;

public interface StudentProgressService {
    
    /**
     * Obtiene el progreso global de un estudiante
     */
    ProgresGlobalDTO obtenerProgreso(Long estudianteId);
    
    /**
     * Obtiene el progreso en un tema específico
     */
    ProgresEstudianteDTO obtenerProgresoEnTema(Long estudianteId, Long temaId);
    
    /**
     * Actualiza el nivel de comprensión de un tema
     */
    void actualizarComprension(Long estudianteId, Long temaId, Integer nivelComprension);
    
    /**
     * Marca un tema como completado
     */
    void marcarCompletado(Long estudianteId, Long temaId);
    
    /**
     * Obtiene la tasa de completación del estudiante (%)
     */
    Double obtenerTasaCompletacion(Long estudianteId);
    
    /**
     * Obtiene los temas completados por el estudiante
     */
    List<TemaDTO> obtenerTemasCompletados(Long estudianteId);
    
    /**
     * Obtiene los temas que están en progreso
     */
    List<TemaDTO> obtenerTemasEnProgreso(Long estudianteId);
    
    /**
     * Obtiene el promedio de comprensión del estudiante
     */
    Double obtenerPromedioComprension(Long estudianteId);
}
