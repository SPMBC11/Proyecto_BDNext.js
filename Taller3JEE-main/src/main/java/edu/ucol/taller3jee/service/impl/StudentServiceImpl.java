package edu.ucol.taller3jee.service.impl;

import edu.ucol.taller3jee.dto.CrearEstudianteDTO;
import edu.ucol.taller3jee.dto.EstudianteDTO;
import edu.ucol.taller3jee.entity.Estudiante;
import edu.ucol.taller3jee.entity.EstadoEstudiante;
import edu.ucol.taller3jee.repository.EstudianteRepository;
import edu.ucol.taller3jee.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final EstudianteRepository estudianteRepository;

    public StudentServiceImpl(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }

    @Override
    public EstudianteDTO crearEstudiante(CrearEstudianteDTO dto) {
        if (estudianteRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya existe: " + dto.getEmail());
        }

        Estudiante estudiante = new Estudiante(dto.getNombre(), dto.getEmail(), dto.getMatricula());
        estudiante = estudianteRepository.save(estudiante);
        return convertirADTO(estudiante);
    }

    @Override
    @Transactional(readOnly = true)
    public EstudianteDTO obtenerEstudiante(Long id) {
        return estudianteRepository.findById(id)
                .map(this::convertirADTO)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public EstudianteDTO obtenerEstudiantePorEmail(String email) {
        return estudianteRepository.findByEmail(email)
                .map(this::convertirADTO)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con email: " + email));
    }

    @Override
    @Transactional(readOnly = true)
    public EstudianteDTO obtenerEstudiantePorMatricula(String matricula) {
        return estudianteRepository.findByMatricula(matricula)
                .map(this::convertirADTO)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con matricula: " + matricula));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EstudianteDTO> obtenerEstudiantes() {
        return estudianteRepository.findByEstado(EstadoEstudiante.ACTIVO)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    public EstudianteDTO actualizarEstudiante(Long id, CrearEstudianteDTO dto) {
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con ID: " + id));

        estudiante.setNombre(dto.getNombre());
        estudiante.setEmail(dto.getEmail());
        estudiante.setMatricula(dto.getMatricula());

        estudiante = estudianteRepository.save(estudiante);
        return convertirADTO(estudiante);
    }

    private EstudianteDTO convertirADTO(Estudiante estudiante) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return new EstudianteDTO(
                estudiante.getId(),
                estudiante.getNombre(),
                estudiante.getEmail(),
                estudiante.getMatricula(),
                estudiante.getEstado().toString(),
                estudiante.getFechaCreacion() == null ? null : estudiante.getFechaCreacion().format(formatter)
        );
    }
}
