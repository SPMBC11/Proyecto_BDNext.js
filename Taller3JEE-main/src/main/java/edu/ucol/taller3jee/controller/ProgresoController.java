package edu.ucol.taller3jee.controller;

import edu.ucol.taller3jee.dto.ProgresEstudianteDTO;
import edu.ucol.taller3jee.dto.ProgresGlobalDTO;
import edu.ucol.taller3jee.dto.TemaDTO;
import edu.ucol.taller3jee.service.StudentProgressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/progreso")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProgresoController {

    private final StudentProgressService progressService;

    public ProgresoController(StudentProgressService progressService) {
        this.progressService = progressService;
    }

    @GetMapping("/{estudianteId}")
    public ResponseEntity<ProgresGlobalDTO> obtenerProgreso(@PathVariable Long estudianteId) {
        return ResponseEntity.ok(progressService.obtenerProgreso(estudianteId));
    }

    @GetMapping("/{estudianteId}/tema/{temaId}")
    public ResponseEntity<ProgresEstudianteDTO> obtenerProgresoEnTema(
            @PathVariable Long estudianteId,
            @PathVariable Long temaId) {
        return ResponseEntity.ok(progressService.obtenerProgresoEnTema(estudianteId, temaId));
    }

    @GetMapping("/{estudianteId}/completacion")
    public ResponseEntity<Double> obtenerTasaCompletacion(@PathVariable Long estudianteId) {
        return ResponseEntity.ok(progressService.obtenerTasaCompletacion(estudianteId));
    }

    @GetMapping("/{estudianteId}/completados")
    public ResponseEntity<List<TemaDTO>> obtenerTemasCompletados(@PathVariable Long estudianteId) {
        return ResponseEntity.ok(progressService.obtenerTemasCompletados(estudianteId));
    }

    @GetMapping("/{estudianteId}/enProgreso")
    public ResponseEntity<List<TemaDTO>> obtenerTemasEnProgreso(@PathVariable Long estudianteId) {
        return ResponseEntity.ok(progressService.obtenerTemasEnProgreso(estudianteId));
    }

    @GetMapping("/{estudianteId}/promedio")
    public ResponseEntity<Double> obtenerPromedioComprension(@PathVariable Long estudianteId) {
        return ResponseEntity.ok(progressService.obtenerPromedioComprension(estudianteId));
    }
}
