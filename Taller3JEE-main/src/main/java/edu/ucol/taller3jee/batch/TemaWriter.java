package edu.ucol.taller3jee.batch;

import edu.ucol.taller3jee.entity.Tema;
import edu.ucol.taller3jee.repository.TemaRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class TemaWriter implements ItemWriter<Tema> {

    private final TemaRepository temaRepository;

    public TemaWriter(TemaRepository temaRepository) {
        this.temaRepository = temaRepository;
    }

    @Override
    public void write(Chunk<? extends Tema> chunk) {
        temaRepository.saveAll(chunk.getItems());
    }
}
