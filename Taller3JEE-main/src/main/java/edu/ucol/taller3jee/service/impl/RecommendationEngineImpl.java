package edu.ucol.taller3jee.service.impl;

import edu.ucol.taller3jee.dto.AnalisisProgresoDTO;
import edu.ucol.taller3jee.dto.ProgresEstudianteDTO;
import edu.ucol.taller3jee.dto.RecomendacionDTO;
import edu.ucol.taller3jee.entity.ProgresEstudiante;
import edu.ucol.taller3jee.entity.Recomendacion;
import edu.ucol.taller3jee.entity.Tema;
import edu.ucol.taller3jee.entity.TipoRecomendacion;
import edu.ucol.taller3jee.repository.EstudianteRepository;
import edu.ucol.taller3jee.repository.ProgresEstudianteRepository;
import edu.ucol.taller3jee.repository.RecomendacionRepository;
import edu.ucol.taller3jee.repository.TemaRepository;
import edu.ucol.taller3jee.service.RecommendationEngine;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RecommendationEngineImpl implements RecommendationEngine {

    private final RecomendacionRepository recomendacionRepository;
    private final ProgresEstudianteRepository progresRepository;
    private final EstudianteRepository estudianteRepository;
    private final TemaRepository temaRepository;

    public RecommendationEngineImpl(RecomendacionRepository recomendacionRepository,
                                    ProgresEstudianteRepository progresRepository,
                                    EstudianteRepository estudianteRepository,
                                    TemaRepository temaRepository) {
        this.recomendacionRepository = recomendacionRepository;
        this.progresRepository = progresRepository;
        this.estudianteRepository = estudianteRepository;
        this.temaRepository = temaRepository;
    }

    @Override
    public RecomendacionDTO generarRecomendacion(Long estudianteId) {
        var estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        var progresos = progresRepository.findByEstudianteIdOrderByTemaNumeroOrdenAsc(estudianteId);

        if (progresos.isEmpty()) {
            Tema primerTema = temaRepository.findByNumeroOrden(1)
                    .orElseThrow(() -> new RuntimeException("Primer tema no encontrado"));

            Recomendacion recomendacion = new Recomendacion(
                    estudiante,
                    null,
                    primerTema,
                    TipoRecomendacion.AVANCE,
                    "Iniciando el curso. Comienza con el primer tema.",
                    1.0
            );
            return convertirADTO(recomendacionRepository.save(recomendacion));
        }

        ProgresEstudiante progresoActual = progresos.stream()
                .filter(p -> !Boolean.TRUE.equals(p.getCompletado()))
                .findFirst()
                .orElse(progresos.get(progresos.size() - 1));

        Tema temaActual = progresoActual.getTema();
        int comprensionActual = progresoActual.getNivelComprension() == null ? 0 : progresoActual.getNivelComprension();

        Tema temaRecomendado;
        TipoRecomendacion tipo;
        String razon;
        double confianza;

        if (comprensionActual < 70) {
            temaRecomendado = obtenerTemaRefuerzo(temaActual);
            tipo = TipoRecomendacion.REFUERZO;
            razon = String.format("Comprension insuficiente (%d%%). Se recomienda refuerzo antes de continuar.", comprensionActual);
            confianza = 0.8;
        } else {
            Tema proximo = obtenerProximoTema(temaActual);
            if (proximo == null) {
                temaRecomendado = temaActual;
                tipo = TipoRecomendacion.AVANCE;
                razon = "Felicidades, has completado el curso.";
                confianza = 1.0;
            } else if (comprensionActual < 80) {
                temaRecomendado = proximo;
                tipo = TipoRecomendacion.AVANCE;
                razon = String.format("Comprension aceptable (%d%%). Puedes continuar con el siguiente tema.", comprensionActual);
                confianza = 0.7;
            } else {
                temaRecomendado = proximo;
                tipo = TipoRecomendacion.AVANCE;
                razon = String.format("Comprension excelente (%d%%). Listo para avanzar.", comprensionActual);
                confianza = 0.95;
            }
        }

        Recomendacion recomendacion = new Recomendacion(
                estudiante,
                temaActual,
                temaRecomendado,
                tipo,
                razon,
                confianza
        );

        return convertirADTO(recomendacionRepository.save(recomendacion));
    }

    @Override
    @Transactional(readOnly = true)
    public AnalisisProgresoDTO analizarProgreso(Long estudianteId) {
        var progresos = progresRepository.findByEstudianteIdOrderByTemaNumeroOrdenAsc(estudianteId);
        int totalTemas = temaRepository.contarTemas() == null ? 0 : temaRepository.contarTemas();

        int completados = (int) progresos.stream().filter(ProgresEstudiante::getCompletado).count();
        int enProgreso = (int) progresos.stream().filter(p -> !Boolean.TRUE.equals(p.getCompletado())).count();
        int pendientes = Math.max(0, totalTemas - progresos.size());

        double promedioComprension = progresos.isEmpty()
                ? 0.0
                : progresos.stream().mapToInt(p -> p.getNivelComprension() == null ? 0 : p.getNivelComprension()).average().orElse(0.0);

        List<ProgresEstudianteDTO> bajoRendimiento = progresos.stream()
                .filter(p -> (p.getNivelComprension() == null ? 0 : p.getNivelComprension()) < 70)
                .map(this::convertirADTO)
                .collect(Collectors.toList());

        List<ProgresEstudianteDTO> buenRendimiento = progresos.stream()
                .filter(p -> (p.getNivelComprension() == null ? 0 : p.getNivelComprension()) >= 70)
                .map(this::convertirADTO)
                .collect(Collectors.toList());

        ProgresEstudiante temaActual = progresos.stream()
                .filter(p -> !Boolean.TRUE.equals(p.getCompletado()))
                .findFirst()
                .orElse(null);

        String recomendacionGeneral = generarRecomendacionGeneral(promedioComprension);

        return new AnalisisProgresoDTO(
                estudianteId,
                completados,
                enProgreso,
                pendientes,
                Math.round(promedioComprension * 100.0) / 100.0,
                temaActual == null ? null : temaActual.getTema().getId(),
                temaActual == null ? "Sin tema en progreso" : temaActual.getTema().getTitulo(),
                recomendacionGeneral,
                bajoRendimiento,
                buenRendimiento
        );
    }

    private Tema obtenerTemaRefuerzo(Tema temaActual) {
        return temaRepository.findByNumeroOrdenGreaterThan(0)
                .stream()
                .filter(t -> t.getNumeroOrden() < temaActual.getNumeroOrden() && t.getTipo() == temaActual.getTipo())
                .reduce((first, second) -> second)
                .orElse(temaActual);
    }

    private Tema obtenerProximoTema(Tema temaActual) {
        return temaRepository.findByNumeroOrdenGreaterThan(temaActual.getNumeroOrden())
                .stream()
                .findFirst()
                .orElse(null);
    }

    private String generarRecomendacionGeneral(double promedio) {
        if (promedio >= 85) {
            return "Excelente desempeno. Continua con el ritmo actual.";
        }
        if (promedio >= 70) {
            return "Buen desempeno. Refuerza los temas con menor comprension.";
        }
        if (promedio >= 50) {
            return "Necesitas mejorar. Dedica mas tiempo a los temas debiles.";
        }
        return "Desempeno bajo. Revisa fundamentos y practica mas.";
    }

    private RecomendacionDTO convertirADTO(Recomendacion recomendacion) {
        return new RecomendacionDTO(
                recomendacion.getId(),
                recomendacion.getEstudiante().getId(),
                recomendacion.getTemaActual() == null ? null : recomendacion.getTemaActual().getId(),
                recomendacion.getTemaRecomendado().getId(),
                recomendacion.getTemaRecomendado().getTitulo(),
                recomendacion.getTipo(),
                recomendacion.getRazon(),
                recomendacion.getNivelConfianza(),
                recomendacion.getFechaCreacion()
        );
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
}
