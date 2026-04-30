package edu.ucol.taller3jee.service.impl;

import edu.ucol.taller3jee.dto.EjercicioSimulacionDTO;
import edu.ucol.taller3jee.dto.ResultadoLoteDTO;
import edu.ucol.taller3jee.dto.ResultadoSimulacionDTO;
import edu.ucol.taller3jee.dto.SimulacionLoteDTO;
import edu.ucol.taller3jee.entity.ProgresEstudiante;
import edu.ucol.taller3jee.repository.EstudianteRepository;
import edu.ucol.taller3jee.repository.ProgresEstudianteRepository;
import edu.ucol.taller3jee.repository.TemaRepository;
import edu.ucol.taller3jee.service.SimulationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Transactional
public class SimulationServiceImpl implements SimulationService {

    private final ProgresEstudianteRepository progresRepository;
    private final EstudianteRepository estudianteRepository;
    private final TemaRepository temaRepository;
    private final Random random = new Random();

    public SimulationServiceImpl(ProgresEstudianteRepository progresRepository,
                                 EstudianteRepository estudianteRepository,
                                 TemaRepository temaRepository) {
        this.progresRepository = progresRepository;
        this.estudianteRepository = estudianteRepository;
        this.temaRepository = temaRepository;
    }

    @Override
    public ResultadoSimulacionDTO simularEjercicio(EjercicioSimulacionDTO dto) {
        var estudiante = estudianteRepository.findById(dto.getEstudianteId())
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        var tema = temaRepository.findById(dto.getTemaId())
                .orElseThrow(() -> new RuntimeException("Tema no encontrado"));

        int aciertos = random.nextInt(6);
        int total = 5;
        int puntuacion = calcularPuntuacion(aciertos, total);

        ProgresEstudiante progres = progresRepository.findByEstudianteIdAndTemaId(dto.getEstudianteId(), dto.getTemaId())
                .orElseGet(() -> new ProgresEstudiante(estudiante, tema));

        progres.incrementarIntento();
        progres.actualizarComprension(puntuacion);
        progresRepository.save(progres);

        return new ResultadoSimulacionDTO(
                dto.getEstudianteId(),
                dto.getTemaId(),
                aciertos,
                total,
                puntuacion,
                progres.getNivelComprension(),
                progres.getCompletado(),
                LocalDateTime.now()
        );
    }

    @Override
    public ResultadoLoteDTO simularEjercicios(SimulacionLoteDTO dto) {
        int cantidad = dto.getCantidad() == null ? 0 : dto.getCantidad();

        List<ResultadoSimulacionDTO> resultados = new ArrayList<>();
        int totalAciertos = 0;
        int totalPuntuacion = 0;

        for (int i = 0; i < cantidad; i++) {
            ResultadoSimulacionDTO resultado = simularEjercicio(
                    new EjercicioSimulacionDTO(dto.getEstudianteId(), dto.getTemaId())
            );
            resultados.add(resultado);
            totalAciertos += resultado.getAciertos();
            totalPuntuacion += resultado.getPuntuacion();
        }

        int promedioPuntuacion = cantidad > 0 ? totalPuntuacion / cantidad : 0;

        ProgresEstudiante progresFinal = progresRepository.findByEstudianteIdAndTemaId(dto.getEstudianteId(), dto.getTemaId())
                .orElseThrow(() -> new RuntimeException("Progreso no encontrado"));

        return new ResultadoLoteDTO(
                dto.getEstudianteId(),
                dto.getTemaId(),
                cantidad,
                totalAciertos,
                promedioPuntuacion,
                progresFinal.getCompletado(),
                resultados
        );
    }

    @Override
    public Integer calcularPuntuacion(Integer aciertos, Integer total) {
        if (total == null || total == 0) {
            return 0;
        }
        return Math.round((aciertos * 100.0f) / total);
    }
}
