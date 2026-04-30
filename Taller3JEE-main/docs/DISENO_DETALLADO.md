# Diseño Detallado - Especificaciones Técnicas
## Taller 3 JEE - Sistema de Gestión de Curso de Estructuras de Datos

---

## 1. ESPECIFICACIÓN DE ENTIDADES

### 1.1 Entidad Tema
```
Clase: Tema
├── atributos
│   ├── id: Long [PK]
│   ├── titulo: String (max 255)
│   ├── descripcion: String (max 1000)
│   ├── tipo: TipoTema (TEORICA, PRACTICA)
│   ├── numeroOrden: Integer 1-70
│   ├── objetivos: String (max 2000)
│   ├── duracionEstimada: Integer (minutos)
│   └── fechaCreacion: LocalDateTime
└── métodos
    ├── getters/setters
    └── toString()
```

### 1.2 Entidad Estudiante
```
Clase: Estudiante
├── atributos
│   ├── id: Long [PK]
│   ├── nombre: String (max 255)
│   ├── email: String (unique)
│   ├── matricula: String (unique)
│   ├── estado: EstadoEstudiante (ACTIVO, INACTIVO)
│   └── fechaCreacion: LocalDateTime
└── métodos
    ├── getters/setters
    └── toString()
```

### 1.3 Entidad ProgresEstudiante
```
Clase: ProgresEstudiante
├── atributos
│   ├── id: Long [PK]
│   ├── estudiante: Estudiante [FK]
│   ├── tema: Tema [FK]
│   ├── nivelComprension: Integer (0-100)
│   ├── intentos: Integer
│   ├── completado: Boolean
│   ├── fechaInicio: LocalDateTime
│   ├── fechaCompletacion: LocalDateTime
│   └── fechaUltimaActualizacion: LocalDateTime
├── índices
│   └── UNIQUE(estudiante_id, tema_id)
└── métodos
    ├── getters/setters
    ├── marcarCompletado()
    └── incrementarIntento()
```

### 1.4 Entidad Recomendacion
```
Clase: Recomendacion
├── atributos
│   ├── id: Long [PK]
│   ├── estudiante: Estudiante [FK]
│   ├── temaActual: Tema [FK]
│   ├── temaRecomendado: Tema [FK]
│   ├── tipo: TipoRecomendacion (REFUERZO, AVANCE)
│   ├── razon: String (max 500)
│   ├── nivelConfianza: Double (0.0-1.0)
│   └── fechaCreacion: LocalDateTime
└── métodos
    ├── getters/setters
    └── toString()
```

---

## 2. ESPECIFICACIÓN DE SERVICIOS

### 2.1 CourseService
```
Interfaz: CourseService
├── métodos
│   ├── obtenerTodosTemas(): List<TemaDTO>
│   ├── obtenerTema(id): TemaDTO
│   ├── obtenerTemasSiguientes(temaId): List<TemaDTO>
│   ├── contarTemas(): Integer
│   └── obtenerProgresoGlobal(): Double
```

### 2.2 StudentProgressService
```
Interfaz: StudentProgressService
├── métodos
│   ├── obtenerProgreso(estudiante): ProgresEstuadianteDTO
│   ├── obtenerProgresoPorTema(estudianteId, temaId): ProgresEstudianteDTO
│   ├── actualizarComprension(estudianteId, temaId, nivel): void
│   ├── marcarCompletado(estudianteId, temaId): void
│   ├── obtenerTasaCompletacion(estudianteId): Double
│   └── obtenerTemasSiguientes(estudianteId): List<TemaDTO>
```

### 2.3 SimulationService
```
Interfaz: SimulationService
├── métodos
│   ├── simularEjercicio(estudianteId, temaId): ResultadoSimulacion
│   ├── simularMultiplesEjercicios(estudianteId, temaId, cantidad): ResultadoSimulacion
│   ├── calcularPuntuacion(numAciertos, totalEjercicios): Integer
│   └── registrarIntento(estudianteId, temaId, puntuacion): void
```

### 2.4 RecommendationEngine
```
Interfaz: RecommendationEngine
├── métodos
│   ├── generarRecomendacion(estudianteId): RecomendacionDTO
│   ├── analizarProgreso(estudianteId): AnalisisProgreso
│   ├── sugerirTemaRefuerzo(estudianteId, temaId): TemaDTO
│   ├── sugerirTemaSiguiente(estudianteId): TemaDTO
│   └── calcularNivelConfianza(analisis): Double
```

---

## 3. ESPECIFICACIÓN DE ENDPOINTS REST

### 3.1 Gestión de Cursos
```
GET /api/v1/cursos
├── response: List<CursoDTO>
└── descripción: Obtener toda la estructura del curso

GET /api/v1/cursos/temas
├── response: List<TemaDTO>
└── descripción: Obtener lista de temas

GET /api/v1/cursos/temas/{id}
├── response: TemaDTO
└── descripción: Obtener detalle de un tema

GET /api/v1/cursos/estadisticas
├── response: EstadisticasDTO
└── descripción: Obtener estadísticas del curso
```

### 3.2 Estudiantes
```
POST /api/v1/estudiantes
├── request: CrearEstudianteDTO
├── response: EstudianteDTO
└── descripción: Registrar nuevo estudiante

GET /api/v1/estudiantes/{id}
├── response: EstudianteDTO
└── descripción: Obtener datos de estudiante

GET /api/v1/estudiantes/{id}/progreso
├── response: ProgresGlobalDTO
└── descripción: Obtener progreso global del estudiante

GET /api/v1/estudiantes/{id}/progreso/{temaId}
├── response: ProgresEstudianteDTO
└── descripción: Obtener progreso en tema específico
```

### 3.3 Simulación
```
POST /api/v1/simulacion/ejercicio
├── request: EjercicioSimulacionDTO
├── response: ResultadoSimulacionDTO
└── descripción: Simular ejercicio

POST /api/v1/simulacion/lote
├── request: SimulacionLoteDTO
├── response: ResultadoLoteDTO
└── descripción: Simular múltiples ejercicios
```

### 3.4 Recomendaciones
```
GET /api/v1/recomendaciones/{estudianteId}
├── response: RecomendacionDTO
└── descripción: Obtener recomendación personalizada

GET /api/v1/recomendaciones/{estudianteId}/historico
├── response: List<RecomendacionDTO>
└── descripción: Obtener histórico de recomendaciones

GET /api/v1/recomendaciones/{estudianteId}/analisis
├── response: AnalisisProgresoDTO
└── descripción: Obtener análisis detallado del progreso
```

---

## 4. ESTRUCTURA DE DTOs

### 4.1 TemaDTO
```java
class TemaDTO {
    Long id
    String titulo
    String descripcion
    TipoTema tipo
    Integer numeroOrden
    String objetivos
    Integer duracionEstimada
}
```

### 4.2 ProgresEstudianteDTO
```java
class ProgresEstudianteDTO {
    Long id
    Long estudianteId
    Long temaId
    String temaTitulo
    Integer nivelComprension
    Integer intentos
    Boolean completado
    LocalDateTime fechaInicio
    LocalDateTime fechaCompletacion
}
```

### 4.3 RecomendacionDTO
```java
class RecomendacionDTO {
    Long id
    Long estudianteId
    Long temaActualId
    Long temaRecomendadoId
    String temaRecomendadoTitulo
    TipoRecomendacion tipo
    String razon
    Double nivelConfianza
    LocalDateTime fechaCreacion
}
```

### 4.4 ResultadoSimulacionDTO
```java
class ResultadoSimulacionDTO {
    Long estudianteId
    Long temaId
    Integer aciertos
    Integer total
    Integer puntuacion (0-100)
    Integer nivelComprensionActualizado
    Boolean completado
    LocalDateTime timestamp
}
```

---

## 5. CONFIGURACIÓN DE BATCH

### 5.1 Job: loadCourseContentJob
```
Job Configuration
├── reader: FlatFileItemReader<TemaInput>
├── processor: TemaProcessor
├── writer: TemaWriter (JPA)
└── step: loadCourseContentStep
    ├── chunkSize: 10
    ├── skipPolicy: AlwaysSkipItemSkipPolicy
    └── listeners: TemaProcessListener
```

### 5.2 Fuente de Datos
```json
// data.json - Estructura de carga
{
  "temas": [
    {
      "id": 1,
      "titulo": "Introducción a Estructuras de Datos",
      "tipo": "TEORICA",
      "numeroOrden": 1,
      "objetivos": "Comprender conceptos básicos..."
    },
    ...
  ]
}
```

---

## 6. ALGORITMO DE RECOMENDACIONES

```
FUNCIÓN generarRecomendacion(estudianteId)
    1. progreso ← obtenerProgreso(estudianteId)
    
    2. SI no existe progreso ENTONCES
        RETORNAR recomendación primer tema
    
    3. temaActual ← obtenerTemaActual(progreso)
    
    4. porcentajeComprension ← temaActual.nivelComprension
    
    5. SI porcentajeComprension < 70 ENTONCES
        // Recomendar refuerzo
        temaSugerido ← sugerirRefuerzo(temaActual)
        crearRecomendacion(estudiante, temaActual, temaSugerido, REFUERZO, 
                          "Comprensión insuficiente", 0.8)
    SINO SI porcentajeComprension >= 70 Y porcentajeComprension < 80 ENTONCES
        // Permite avance pero con cuidado
        temaSugerido ← obtenerTemaSiguiente(temaActual)
        crearRecomendacion(estudiante, temaActual, temaSugerido, AVANCE,
                          "Listo para siguiente tema", 0.7)
    SINO
        // Completado, avanzar
        temaSugerido ← obtenerTemaSiguiente(temaActual)
        crearRecomendacion(estudiante, temaActual, temaSugerido, AVANCE,
                          "Tema completado, listo para avanzar", 0.95)
    FINSI
    
    6. RETORNAR recomendacion
FIN
```

---

## 7. ALGORITMO DE SIMULACIÓN

```
FUNCIÓN simularEjercicio(estudianteId, temaId)
    1. tema ← buscarTema(temaId)
    
    2. numAciertos ← generarNumeroAleatorio(0, 4)  // 0-4 aciertos de 5
    
    3. total ← 5
    
    4. puntuacion ← calcularPuntuacion(numAciertos, total)
       puntuacion = (numAciertos / total) * 100
    
    5. progreso ← obtenerOCrearProgreso(estudianteId, temaId)
    
    6. // Actualizar comprensión (PromPndio móvil)
       comprensionActual ← progreso.nivelComprension
       comprensionNueva ← (comprensionActual + puntuacion) / 2
       progreso.nivelComprension ← Math.min(comprensionNueva, 100)
    
    7. SI comprensionNueva >= 80 ENTONCES
        progreso.completado ← true
        progreso.fechaCompletacion ← ahora()
    FINSI
    
    8. progreso.intentos ← progreso.intentos + 1
    
    9. guardarProgreso(progreso)
    
    10. RETORNAR ResultadoSimulacion
FIN
```

---

## 8. MAPEO JPA

```java
// Tema.java
@Entity
@Table(name = "tema")
public class Tema {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 255)
    private String titulo;
    
    @Enumerated(EnumType.STRING)
    private TipoTema tipo;
    
    @Column(name = "numero_orden")
    private Integer numeroOrden;
    
    @OneToMany(mappedBy = "tema", cascade = CascadeType.ALL)
    private Set<ProgresEstudiante> progresos = new HashSet<>();
}
```

---

## 9. EXCEPCIONES PERSONALIZADAS

```
├── ApiException (base)
├── TemaNoEncontradaException
├── EstudianteNoEncontradoException
├── ProgresNoEncontradoException
├── RecomendacionNoDisponibleException
└── BatchProcessingException
```

---

## 10. CONFIGURACIÓN DE PROPIEDADES

```properties
# application.properties
spring.application.name=taller3jee-ed
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect

# Batch
spring.batch.job.enabled=false

# API
server.port=8080
server.servlet.context-path=/taller3jee

# Logging
logging.level.edu.ucol.taller3jee=DEBUG
```

