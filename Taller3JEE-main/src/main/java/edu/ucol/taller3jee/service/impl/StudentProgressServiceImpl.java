package edu.ucol.taller3jee.service.impl;

import edu.ucol.taller3jee.dto.ProgresEstudianteDTO;
import edu.ucol.taller3jee.dto.ProgresGlobalDTO;
import edu.ucol.taller3jee.dto.TemaDTO;
import edu.ucol.taller3jee.entity.ProgresEstudiante;
import edu.ucol.taller3jee.entity.Tema;
import edu.ucol.taller3jee.repository.EstudianteRepository;
import edu.ucol.taller3jee.repository.ProgresEstudianteRepository;
import edu.ucol.taller3jee.repository.TemaRepository;
import edu.ucol.taller3jee.service.StudentProgressService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentProgressServiceImpl implements StudentProgressService {

    private final ProgresEstudianteRepository progresRepository;
    private final EstudianteRepository estudianteRepository;
    private final TemaRepository temaRepository;

    public StudentProgressServiceImpl(ProgresEstudianteRepository progresRepository,
                                      EstudianteRepository estudianteRepository,
                                      TemaRepository temaRepository) {
        this.progresRepository = progresRepository;
        this.estudianteRepository = estudianteRepository;
        this.temaRepository = temaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ProgresGlobalDTO obtenerProgreso(Long estudianteId) {
        var estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        var progresos = progresRepository.findByEstudianteIdOrderByTemaNumeroOrdenAsc(estudianteId);
        Integer totalTemas = temaRepository.contarTemas();
        Integer completados = progresRepository.contarCompletados(estudianteId);
        Double promedioComprension = progresRepository.promedioComprension(estudianteId);

        int total = totalTemas == null ? 0 : totalTemas;
        int comp = completados == null ? 0 : completados;
        double promedio = promedioComprension == null ? 0.0 : promedioComprension;
        double porcentajeCompletacion = total > 0 ? (comp * 100.0 / total) : 0.0;

        List<ProgresEstudianteDTO> detalles = progresos.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());

        return new ProgresGlobalDTO(
                estudianteId,
                estudiante.getNombre(),
                comp,
                total,
                porcentajeCompletacion,
                promedio,
                detalles
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ProgresEstudianteDTO obtenerProgresoEnTema(Long estudianteId, Long temaId) {
        return progresRepository.findByEstudianteIdAndTemaId(estudianteId, temaId)
                .map(this::convertirADTO)
                .orElseThrow(() -> new RuntimeException("Progreso no encontrado"));
    }

    @Override
    public void actualizarComprension(Long estudianteId, Long temaId, Integer nivelComprension) {
        ProgresEstudiante progres = progresRepository.findByEstudianteIdAndTemaId(estudianteId, temaId)
                .orElseGet(() -> {
                    var estudiante = estudianteRepository.findById(estudianteId)
                            .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));
                    var tema = temaRepository.findById(temaId)
                            .orElseThrow(() -> new RuntimeException("Tema no encontrado"));
                    return new ProgresEstudiante(estudiante, tema);
                });

        progres.actualizarComprension(nivelComprension);
        progresRepository.save(progres);
    }

    @Override
    public void marcarCompletado(Long estudianteId, Long temaId) {
        ProgresEstudiante progres = progresRepository.findByEstudianteIdAndTemaId(estudianteId, temaId)
                .orElseThrow(() -> new RuntimeException("Progreso no encontrado"));

        progres.marcarCompletado();
        progresRepository.save(progres);
    }

    @Override
    @Transactional(readOnly = true)
    public Double obtenerTasaCompletacion(Long estudianteId) {
        Integer completados = progresRepository.contarCompletados(estudianteId);
        Integer totalTemas = temaRepository.contarTemas();

        int comp = completados == null ? 0 : completados;
        int total = totalTemas == null ? 0 : totalTemas;
        if (total == 0) {
            return 0.0;
        }
        return (comp * 100.0) / total;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TemaDTO> obtenerTemasCompletados(Long estudianteId) {
        return progresRepository.findByCompletadoTrueAndEstudianteId(estudianteId)
                .stream()
                .map(p -> convertirTemaDTO(p.getTema()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TemaDTO> obtenerTemasEnProgreso(Long estudianteId) {
        return progresRepository.findProgresosPendientes(estudianteId)
                .stream()
                .map(p -> convertirTemaDTO(p.getTema()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Double obtenerPromedioComprension(Long estudianteId) {
        Double promedio = progresRepository.promedioComprension(estudianteId);
        return promedio == null ? 0.0 : promedio;
    }

    private ProgresEstudianteDTO convertirADTO(ProgresEstudiante progres) {
        return new ProgresEstudianteDTO(
                progres.getId(),
                progres.getEstudiante().getId(),
                progres.getTema().getId(),
                progres.getTema().getTitulo(),
                progres.getNivelComprension(),
                progres.getIntentos(),
                progres.getCompletado(),
                progres.getFechaInicio(),
                progres.getFechaCompletacion(),
                progres.getFechaUltimaActualizacion()
        );
    }

    private TemaDTO convertirTemaDTO(Tema tema) {
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
