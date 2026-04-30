package edu.ucol.taller3jee.controller;

import edu.ucol.taller3jee.dto.CrearEstudianteDTO;
import edu.ucol.taller3jee.dto.EstudianteDTO;
import edu.ucol.taller3jee.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/estudiantes")
@CrossOrigin(origins = "*", maxAge = 3600)
public class EstudianteController {

    private final StudentService studentService;

    public EstudianteController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<EstudianteDTO> crearEstudiante(@RequestBody CrearEstudianteDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.crearEstudiante(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstudianteDTO> obtenerEstudiante(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.obtenerEstudiante(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<EstudianteDTO> obtenerEstudiantePorEmail(@PathVariable String email) {
        return ResponseEntity.ok(studentService.obtenerEstudiantePorEmail(email));
    }

    @GetMapping
    public ResponseEntity<List<EstudianteDTO>> obtenerEstudiantes() {
        return ResponseEntity.ok(studentService.obtenerEstudiantes());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstudianteDTO> actualizarEstudiante(
            @PathVariable Long id,
            @RequestBody CrearEstudianteDTO dto) {
        return ResponseEntity.ok(studentService.actualizarEstudiante(id, dto));
    }
}
