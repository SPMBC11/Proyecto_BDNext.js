package edu.ucol.taller3jee.service;

import edu.ucol.taller3jee.dto.TemaDTO;
import java.util.List;

public interface CourseService {
    
    /**
     * Obtiene todos los temas del curso ordenados por número
     */
    List<TemaDTO> obtenerTodosTemas();
    
    /**
     * Obtiene un tema específico por ID
     */
    TemaDTO obtenerTema(Long id);
    
    /**
     * Obtiene los temas siguientes a un tema específico
     */
    List<TemaDTO> obtenerTemasSiguientes(Long temaId, Integer cantidad);
    
    /**
     * Obtiene el próximo tema después del especificado
     */
    TemaDTO obtenerProximoTema(Long temaId);
    
    /**
     * Cuenta el total de temas en el curso
     */
    Integer contarTemas();
}
