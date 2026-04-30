package edu.ucol.taller3jee.controller;

import edu.ucol.taller3jee.dto.EjercicioSimulacionDTO;
import edu.ucol.taller3jee.dto.ResultadoLoteDTO;
import edu.ucol.taller3jee.dto.ResultadoSimulacionDTO;
import edu.ucol.taller3jee.dto.SimulacionLoteDTO;
import edu.ucol.taller3jee.service.SimulationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/simulacion")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SimulacionController {

    private final SimulationService simulationService;

    public SimulacionController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @PostMapping("/ejercicio")
    public ResponseEntity<ResultadoSimulacionDTO> simularEjercicio(
            @RequestBody EjercicioSimulacionDTO dto) {
        return ResponseEntity.ok(simulationService.simularEjercicio(dto));
    }

    @PostMapping("/lote")
    public ResponseEntity<ResultadoLoteDTO> simularEjercicios(
            @RequestBody SimulacionLoteDTO dto) {
        return ResponseEntity.ok(simulationService.simularEjercicios(dto));
    }
}
