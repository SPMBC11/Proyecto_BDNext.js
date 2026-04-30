package edu.ucol.taller3jee.controller;

import edu.ucol.taller3jee.dto.AnalisisProgresoDTO;
import edu.ucol.taller3jee.dto.RecomendacionDTO;
import edu.ucol.taller3jee.service.RecommendationEngine;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recomendaciones")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RecomendacionController {

    private final RecommendationEngine recommendationEngine;

    public RecomendacionController(RecommendationEngine recommendationEngine) {
        this.recommendationEngine = recommendationEngine;
    }

    @GetMapping("/{estudianteId}")
    public ResponseEntity<RecomendacionDTO> generarRecomendacion(@PathVariable Long estudianteId) {
        return ResponseEntity.ok(recommendationEngine.generarRecomendacion(estudianteId));
    }

    @GetMapping("/{estudianteId}/analisis")
    public ResponseEntity<AnalisisProgresoDTO> analizarProgreso(@PathVariable Long estudianteId) {
        return ResponseEntity.ok(recommendationEngine.analizarProgreso(estudianteId));
    }
}
