package edu.ucol.taller3jee.service;

import edu.ucol.taller3jee.dto.CrearEstudianteDTO;
import edu.ucol.taller3jee.dto.EstudianteDTO;
import java.util.List;

public interface StudentService {
    
    /**
     * Registra un nuevo estudiante en el sistema
     */
    EstudianteDTO crearEstudiante(CrearEstudianteDTO dto);
    
    /**
     * Obtiene los datos de un estudiante
     */
    EstudianteDTO obtenerEstudiante(Long id);
    
    /**
     * Obtiene un estudiante por email
     */
    EstudianteDTO obtenerEstudiantePorEmail(String email);
    
    /**
     * Obtiene un estudiante por matrícula
     */
    EstudianteDTO obtenerEstudiantePorMatricula(String matricula);
    
    /**
     * Obtiene todos los estudiantes activos
     */
    List<EstudianteDTO> obtenerEstudiantes();
    
    /**
     * Actualiza los datos de un estudiante
     */
    EstudianteDTO actualizarEstudiante(Long id, CrearEstudianteDTO dto);
}
