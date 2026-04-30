package edu.ucol.taller3jee.service;

import edu.ucol.taller3jee.dto.AnalisisProgresoDTO;
import edu.ucol.taller3jee.dto.RecomendacionDTO;

public interface RecommendationEngine {
    
    /**
     * Genera una recomendación personalizada basada en el progreso del estudiante
     */
    RecomendacionDTO generarRecomendacion(Long estudianteId);
    
    /**
     * Analiza el progreso detallado del estudiante
     */
    AnalisisProgresoDTO analizarProgreso(Long estudianteId);
}
