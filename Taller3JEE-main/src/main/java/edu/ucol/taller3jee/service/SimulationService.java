package edu.ucol.taller3jee.service;

import edu.ucol.taller3jee.dto.EjercicioSimulacionDTO;
import edu.ucol.taller3jee.dto.ResultadoLoteDTO;
import edu.ucol.taller3jee.dto.ResultadoSimulacionDTO;
import edu.ucol.taller3jee.dto.SimulacionLoteDTO;

public interface SimulationService {
    
    /**
     * Simula la realización de un ejercicio por parte del estudiante
     */
    ResultadoSimulacionDTO simularEjercicio(EjercicioSimulacionDTO dto);
    
    /**
     * Simula múltiples ejercicios del mismo tema
     */
    ResultadoLoteDTO simularEjercicios(SimulacionLoteDTO dto);
    
    /**
     * Calcula la puntuación basada en aciertos y total
     */
    Integer calcularPuntuacion(Integer aciertos, Integer total);
}
