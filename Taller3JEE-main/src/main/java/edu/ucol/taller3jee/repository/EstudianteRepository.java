package edu.ucol.taller3jee.repository;

import edu.ucol.taller3jee.entity.Estudiante;
import edu.ucol.taller3jee.entity.EstadoEstudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    Optional<Estudiante> findByEmail(String email);
    
    Optional<Estudiante> findByMatricula(String matricula);
    
    List<Estudiante> findByEstado(EstadoEstudiante estado);
}
