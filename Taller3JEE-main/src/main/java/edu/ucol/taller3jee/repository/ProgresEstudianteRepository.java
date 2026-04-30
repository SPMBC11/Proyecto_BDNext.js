package edu.ucol.taller3jee.repository;

import edu.ucol.taller3jee.entity.ProgresEstudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgresEstudianteRepository extends JpaRepository<ProgresEstudiante, Long> {
    
    Optional<ProgresEstudiante> findByEstudianteIdAndTemaId(Long estudianteId, Long temaId);
    
    List<ProgresEstudiante> findByEstudianteId(Long estudianteId);
    
    List<ProgresEstudiante> findByEstudianteIdOrderByTemaNumeroOrdenAsc(Long estudianteId);
    
    List<ProgresEstudiante> findByCompletadoTrueAndEstudianteId(Long estudianteId);
    
    @Query("SELECT p FROM ProgresEstudiante p WHERE p.estudiante.id = :estudianteId " +
           "AND p.completado = false ORDER BY p.tema.numeroOrden ASC")
    List<ProgresEstudiante> findProgresosPendientes(@Param("estudianteId") Long estudianteId);
    
    @Query("SELECT AVG(p.nivelComprension) FROM ProgresEstudiante p WHERE p.estudiante.id = :estudianteId")
    Double promedioComprension(@Param("estudianteId") Long estudianteId);
    
    @Query("SELECT COUNT(p) FROM ProgresEstudiante p WHERE p.estudiante.id = :estudianteId AND p.completado = true")
    Integer contarCompletados(@Param("estudianteId") Long estudianteId);
}
