package edu.ucol.taller3jee.batch;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ucol.taller3jee.batch.TemaInput;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Reader que carga temas desde un archivo JSON
 */
@Component
public class TemaReader implements ItemReader<TemaInput> {

    private List<TemaInput> items = new ArrayList<>();
    private int currentIndex = 0;
    private boolean initialized = false;

    @Override
    public TemaInput read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (!initialized) {
            inicializarDatos();
            initialized = true;
        }

        if (currentIndex < items.size()) {
            TemaInput tema = items.get(currentIndex);
            currentIndex++;
            return tema;
        }

        return null; // Fin del archivo
    }

    @SuppressWarnings("unchecked")
    private void inicializarDatos() throws IOException {
        
        try {
            ObjectMapper mapper = new ObjectMapper();
            ClassPathResource resource = new ClassPathResource("data.json");
            
            Map<String, Object> data = mapper.readValue(resource.getInputStream(), Map.class);
            List<Map<String, Object>> temasData = (List<Map<String, Object>>) data.get("temas");
            
            if (temasData == null) {
                throw new IOException("No se encontró la clave 'temas' en el archivo JSON");
            }
            
            for (Map<String, Object> temaData : temasData) {
                TemaInput tema = new TemaInput();
                tema.setId(((Number) temaData.get("id")).longValue());
                tema.setTitulo((String) temaData.get("titulo"));
                tema.setDescripcion((String) temaData.get("descripcion"));
                tema.setTipo((String) temaData.get("tipo"));
                tema.setNumeroOrden(((Number) temaData.get("numeroOrden")).intValue());
                tema.setObjetivos((String) temaData.get("objetivos"));
                
               Object duracion = temaData.get("duracionEstimada");
                if (duracion != null) {
                    tema.setDuracionEstimada(((Number) duracion).intValue());
                }
                
                items.add(tema);
            }
            
            
        } catch (IOException e) {
            throw e;
        }
    }
}
