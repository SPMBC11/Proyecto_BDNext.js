package edu.ucol.taller3jee.batch;

import edu.ucol.taller3jee.entity.Tema;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * Procesador que valida y transforma los items de entrada
 */
@Component
public class TemaProcessor implements ItemProcessor<TemaInput, Tema> {

    @Override
    public Tema process(TemaInput temaInput) throws Exception {
        
        // Validaciones
        if (temaInput.getTitulo() == null || temaInput.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título del tema no puede estar vacío");
        }
        
        if (temaInput.getNumeroOrden() == null || temaInput.getNumeroOrden() <= 0) {
            throw new IllegalArgumentException("El número de orden debe ser mayor a 0");
        }
        
        if (temaInput.getTipo() == null || 
            (!temaInput.getTipo().equalsIgnoreCase("TEORICA") && 
             !temaInput.getTipo().equalsIgnoreCase("PRACTICA"))) {
            throw new IllegalArgumentException("El tipo debe ser TEORICA o PRACTICA");
        }
        
        // Convertir a entidad
        Tema tema = temaInput.convertirAEntidad();
        
        return tema;
    }
}
