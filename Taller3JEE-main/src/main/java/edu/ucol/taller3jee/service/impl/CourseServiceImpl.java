package edu.ucol.taller3jee.service.impl;

import edu.ucol.taller3jee.dto.TemaDTO;
import edu.ucol.taller3jee.entity.Tema;
import edu.ucol.taller3jee.repository.TemaRepository;
import edu.ucol.taller3jee.service.CourseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CourseServiceImpl implements CourseService {

    private final TemaRepository temaRepository;

    public CourseServiceImpl(TemaRepository temaRepository) {
        this.temaRepository = temaRepository;
    }

    @Override
    public List<TemaDTO> obtenerTodosTemas() {
        return temaRepository.findAllOrdenados()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    public TemaDTO obtenerTema(Long id) {
        return temaRepository.findById(id)
                .map(this::convertirADTO)
                .orElseThrow(() -> new RuntimeException("Tema no encontrado con ID: " + id));
    }

    @Override
    public List<TemaDTO> obtenerTemasSiguientes(Long temaId, Integer cantidad) {
        Tema tema = temaRepository.findById(temaId)
                .orElseThrow(() -> new RuntimeException("Tema no encontrado con ID: " + temaId));

        return temaRepository.findByNumeroOrdenGreaterThan(tema.getNumeroOrden())
                .stream()
                .limit(cantidad)
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    public TemaDTO obtenerProximoTema(Long temaId) {
        Tema tema = temaRepository.findById(temaId)
                .orElseThrow(() -> new RuntimeException("Tema no encontrado con ID: " + temaId));

        return temaRepository.findByNumeroOrdenGreaterThan(tema.getNumeroOrden())
                .stream()
                .findFirst()
                .map(this::convertirADTO)
                .orElseThrow(() -> new RuntimeException("No hay siguiente tema disponible"));
    }

    @Override
    public Integer contarTemas() {
        return temaRepository.contarTemas();
    }

    private TemaDTO convertirADTO(Tema tema) {
        return new TemaDTO(
                tema.getId(),
                tema.getTitulo(),
                tema.getDescripcion(),
                tema.getTipo(),
                tema.getNumeroOrden(),
                tema.getObjetivos(),
                tema.getDuracionEstimada(),
                tema.getFechaCreacion()
        );
    }
}
