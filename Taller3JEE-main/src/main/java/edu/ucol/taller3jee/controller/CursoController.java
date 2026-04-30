package edu.ucol.taller3jee.controller;

import edu.ucol.taller3jee.dto.TemaDTO;
import edu.ucol.taller3jee.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cursos")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CursoController {

    private final CourseService courseService;

    public CursoController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/temas")
    public ResponseEntity<List<TemaDTO>> obtenerTodosTemas() {
        return ResponseEntity.ok(courseService.obtenerTodosTemas());
    }

    @GetMapping("/temas/{id}")
    public ResponseEntity<TemaDTO> obtenerTema(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.obtenerTema(id));
    }

    @GetMapping("/temas/{id}/siguientes")
    public ResponseEntity<List<TemaDTO>> obtenerTemasSiguientes(
            @PathVariable Long id,
            @RequestParam(defaultValue = "5") Integer cantidad) {
        return ResponseEntity.ok(courseService.obtenerTemasSiguientes(id, cantidad));
    }

    @GetMapping("/temas/{id}/proximo")
    public ResponseEntity<TemaDTO> obtenerProximoTema(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.obtenerProximoTema(id));
    }

    @GetMapping("/total")
    public ResponseEntity<Integer> obtenerTotal() {
        return ResponseEntity.ok(courseService.contarTemas());
    }
}
