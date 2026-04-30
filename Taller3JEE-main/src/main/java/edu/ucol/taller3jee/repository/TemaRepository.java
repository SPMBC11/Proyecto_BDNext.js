package edu.ucol.taller3jee.repository;

import edu.ucol.taller3jee.entity.Tema;
import edu.ucol.taller3jee.entity.TipoTema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TemaRepository extends JpaRepository<Tema, Long> {
    List<Tema> findByTipo(TipoTema tipo);
    
    Optional<Tema> findByNumeroOrden(Integer numeroOrden);
    
    List<Tema> findByNumeroOrdenGreaterThan(Integer numeroOrden);
    
    @Query("SELECT t FROM Tema t ORDER BY t.numeroOrden ASC")
    List<Tema> findAllOrdenados();
    
    @Query("SELECT COUNT(t) FROM Tema t")
    Integer contarTemas();
}
