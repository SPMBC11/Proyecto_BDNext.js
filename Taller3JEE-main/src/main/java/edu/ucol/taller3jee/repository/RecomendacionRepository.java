package edu.ucol.taller3jee.repository;

import edu.ucol.taller3jee.entity.Recomendacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecomendacionRepository extends JpaRepository<Recomendacion, Long> {

    Optional<Recomendacion> findTopByEstudianteIdOrderByFechaCreacionDesc(Long estudianteId);
    
    List<Recomendacion> findByEstudianteIdOrderByFechaCreacionDesc(Long estudianteId);
    
    List<Recomendacion> findByEstudianteId(Long estudianteId);
}
