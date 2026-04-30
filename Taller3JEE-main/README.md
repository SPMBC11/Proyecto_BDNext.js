# Taller3JEE - Sistema de Gestión de Curso de Estructuras de Datos

## Descripción General

Aplicación JEE completa desarrollada en Java que gestiona un curso completo de Estructuras de Datos con:

- **70 temas** (36 teóricos + 34 prácticos)
- **Batch processing** para cargar contenido de cursos
- **Simulación de progreso** del estudiante
- **Motor de recomendaciones** personalizado
- **REST API** completa
- **Base de datos** con JPA/Hibernate

## Diseño Detallado
El diseño detallado del sistema se basa en la definición de clases que representan tanto el dominio como la lógica del negocio.

### a) **Entidades principales:**
- Tema: Representa cada clase del curso.
- Estudiante: Modela al usuario que realiza el curso.
- ProgresoEstudiante: Registra el avance, intentos y nivel de comprensión del estudiante.
- Recomendacion: Almacena las sugerencias generadas por el sistema.
### b) **Componentes de negocio:**
- SimulationServiceImpl: Simula el desarrollo del curso y el desempeño del estudiante.
- RecommendationEngineImpl: Analiza el progreso y genera recomendaciones adaptativas.

Este diseño permite modelar de forma clara la relación entre los elementos del sistema y soportar la lógica requerida, cumpliendo con el enfoque solicitado en el diseño detallado basado en clases.

## Requerimientos Cumplidos

### a) Aplicación Batch
✅ Carga automática de 70 temas (36 teóricos, 34 prácticos) en la BD
- Reader: Lee desde `data.json`
- Processor: Valida y procesa datos
- Writer: Persiste en H2/MySQL

### b) Simulación de Curso
✅ Simula el desarrollo de un estudiante en un curso de E.D
- Ejercicios con resultados aleatorios (0-5 aciertos de 5)
- Cálculo automático de comprensión (0-100%)
- Tracking de intentos por tema

### c) Módulo de Recomendaciones
✅ Motor inteligente que sugiere siguientes pasos
- Refuerzo si comprensión < 70%
- Avance moderado si 70% ≤ comprensión < 80%
- Avance pleno si comprensión ≥ 80%

### d) Diseño AS y Diseño Detallado
✅ Documentación completa en `/docs`
- ARQUITECTURA_AS.md: Lógica, Desarrollo, Procesos, Despliegue
- DISENO_DETALLADO.md: Especificaciones técnicas detalladas

---

## Estructura del Proyecto

```
Taller3JEE/
├── src/main/java/edu/ucol/taller3jee/
│   ├── Taller3JeeApplication.java          # Clase principal
│   ├── model/
│   │   ├── entity/                         # Entidades JPA
│   │   │   ├── Tema, Estudiante
│   │   │   ├── ProgresEstudiante, Recomendacion
│   │   │   └── TipoTema, EstadoEstudiante, TipoRecomendacion
│   │   └── dto/                            # Data Transfer Objects
│   │       └── [11 DTOs para APIs]
│   ├── repository/                         # Spring Data JPA Repositories
│   ├── service/
│   │   ├── [4 Interfaces de Servicios]
│   │   └── impl/                           # Implementaciones
│   │       ├── CourseServiceImpl
│   │       ├── StudentServiceImpl
│   │       ├── StudentProgressServiceImpl
│   │       ├── SimulationServiceImpl
│   │       └── RecommendationEngineImpl
│   ├── batch/
│   │   ├── TemaReader                      # Lee datos JSON
│   │   ├── TemaProcessor                   # Procesa/valida
│   │   └── TemaWriter                      # Persiste en BD
│   ├── controller/                         # REST Controllers (4)
│   ├── config/
│   │   ├── BatchConfiguration              # Configuración del Job
│   │   └── GlobalExceptionHandler          # Manejo de excepciones
│   └── ...
├── src/main/resources/
│   ├── application.properties               # Configuración
│   └── data.json                           # Datos de 70 temas
├── src/test/java/...                      # Tests (estructura lista)
├── docs/
│   ├── ARQUITECTURA_AS.md                  # Diseño de arquitectura
│   └── DISENO_DETALLADO.md                 # Diseño detallado
├── pom.xml                                 # Maven dependencies
└── README.md                               # Este archivo
```

---

## Stack Tecnológico

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **Spring Batch**
- **H2 Database** (desarrollo)
- **MySQL** (producción)
- **Lombok**
- **Maven 3.8+**

---

## Instalación y Ejecución

### Requisitos Previos
- JDK 17 o superior
- Maven 3.8+
- Git

### Pasos de Instalación

1. **Clonar el repositorio**
```bash
git clone https://github.com/gabrielcamachoo/Taller3JEE.git
cd Taller3JEE
```

2. **Compilar el proyecto**
```bash
mvn clean package -DskipTests
```

3. **Ejecutar la aplicación**
```bash
mvn spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080/taller3jee`

### Ejecutar el Batch Job

```bash
# Con esta opción se ejecuta el job al iniciar
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.batch.job.names=loadCourseContentJob"
```

### H2 Console (Desarrollo)

Acceder a: `http://localhost:8080/taller3jee/h2-console`
- URL JDBC: `jdbc:h2:mem:testdb`
- Usuario: `sa`
- Contraseña: (dejar vacía)

---

## Endpoints de la API

### Cursos
- `GET /api/v1/cursos/temas` - Obtener todos los temas
- `GET /api/v1/cursos/temas/{id}` - Obtener tema específico
- `GET /api/v1/cursos/temas/{id}/siguientes` - Temas siguientes
- `GET /api/v1/cursos/temas/{id}/proximo` - Próximo tema
- `GET /api/v1/cursos/total` - Total de temas

### Estudiantes
- `POST /api/v1/estudiantes` - Crear estudiante
- `GET /api/v1/estudiantes/{id}` - Obtener datos
- `GET /api/v1/estudiantes` - Listar estudiantes
- `PUT /api/v1/estudiantes/{id}` - Actualizar

### Progreso
- `GET /api/v1/progreso/{estudianteId}` - Progreso global
- `GET /api/v1/progreso/{estudianteId}/tema/{temaId}` - Progreso en tema
- `GET /api/v1/progreso/{estudianteId}/completacion` - Tasa de completación
- `GET /api/v1/progreso/{estudianteId}/completados` - Temas completados
- `GET /api/v1/progreso/{estudianteId}/enProgreso` - Temas en progreso
- `GET /api/v1/progreso/{estudianteId}/promedio` - Promedio de comprensión

### Simulación
- `POST /api/v1/simulacion/ejercicio` - Simular un ejercicio
- `POST /api/v1/simulacion/lote` - Simular múltiples ejercicios

### Recomendaciones
- `GET /api/v1/recomendaciones/{estudianteId}` - Generar recomendación
- `GET /api/v1/recomendaciones/{estudianteId}/analisis` - Análisis detallado

---

## Flujo de Uso

### 1. Cargar Contenido (Batch)
```bash
# El batch se ejecuta automáticamente al iniciar
# Carga 70 temas desde data.json
```

### 2. Crear un Estudiante
```javascript
POST http://localhost:8080/taller3jee/api/v1/estudiantes
Content-Type: application/json

{
  "nombre": "Juan Pérez",
  "email": "juan@example.com",
  "matricula": "2024001"
}
```

### 3. Simular Ejercicio
```javascript
POST http://localhost:8080/taller3jee/api/v1/simulacion/ejercicio
Content-Type: application/json

{
  "estudianteId": 1,
  "temaId": 1
}
```

### 4. Obtener Recomendación
```javascript
GET http://localhost:8080/taller3jee/api/v1/recomendaciones/1
```

---

## Características Principales

### Motor de Recomendaciones Inteligente

El motor analiza:
- Comprensión actual del tema (%)
- Número de intentos
- Progreso general del curso

Y genera recomendaciones:
- **REFUERZO**: Si comprensión < 70%
- **AVANCE MODERADO**: Si 70% ≤ comprensión < 80%
- **AVANCE PLENO**: Si comprensión ≥ 80%

### Simulación Realista

- Genera 0-5 aciertos de 5 ejercicios (aleatorio)
- Calcula comprensión con promedio móvil simple
- Marca tema como completado cuando comprensión ≥ 80%

### Batch Processing Robusto

- Lee datos de JSON
- Valida estructura de datos
- Maneja errores con skip policy
- Registra operaciones en logs

---

## Mapeo de Base de Datos

```sql
-- Tabla de Temas
CREATE TABLE tema (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(255) NOT NULL,
    descripcion TEXT,
    tipo VARCHAR(50) NOT NULL,
    numero_orden INT NOT NULL,
    objetivos TEXT,
    duracion_estimada INT
);

-- Tabla de Estudiantes
CREATE TABLE estudiante (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    matricula VARCHAR(50) UNIQUE,
    estado VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de Progreso
CREATE TABLE progreso_estudiante (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    estudiante_id BIGINT NOT NULL,
    tema_id BIGINT NOT NULL,
    nivel_comprension INT (0-100),
    intentos INT,
    completado BOOLEAN,
    fecha_inicio TIMESTAMP,
    fecha_completacion TIMESTAMP,
    UNIQUE(estudiante_id, tema_id),
    FOREIGN KEY (estudiante_id) REFERENCES estudiante(id),
    FOREIGN KEY (tema_id) REFERENCES tema(id)
);

-- Tabla de Recomendaciones
CREATE TABLE recomendacion (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    estudiante_id BIGINT NOT NULL,
    tema_actual_id BIGINT,
    tema_recomendado_id BIGINT NOT NULL,
    tipo VARCHAR(50),
    razon VARCHAR(500),
    nivel_confianza DECIMAL(3,2)
);
```

---

## Documentación

- **[ARQUITECTURA_AS.md](docs/ARQUITECTURA_AS.md)**: Lógica, desarrollo, procesos y despliegue
- **[DISENO_DETALLADO.md](docs/DISENO_DETALLADO.md)**: Especificaciones técnicas completas

---

## Testing

```bash
# Ejecutar todas las pruebas
mvn test

# Ejecutar con cobertura
mvn test jacoco:report
```

---

## Configuración para Producción

1. **Cambiar a MySQL**
```properties
# application-prod.properties
spring.datasource.url=jdbc:mysql://localhost:3306/taller3jee
spring.datasource.username=root
spring.datasource.password=password
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

2. **Ejecutar con producción**
```bash
mvn clean package -Pprod -DskipTests
java -jar target/taller3jee-ed-1.0.0.jar --spring.profiles.active=prod
```

---

## Logs

Los logs se escriben en:
- **Consola**: Información general
- **Archivo**: `logs/application.log` - Todas las operaciones

Niveles de logging:
- `DEBUG`: edu.ucol.taller3jee
- `INFO`: Spring Framework
- `WARN`: Hibernate

---

## Troubleshooting

### El batch no carga los datos
```bash
# Verificar que data.json esté en src/main/resources/
# Verificar propiedades de batch en application.properties
spring.batch.job.enabled=false
```

### Puerto 8080 en uso
```bash
# Cambiar puerto en application.properties
server.port=8081
```

### H2 Console no accesible
```bash
# Verificar que esté habilitada
spring.h2.console.enabled=true
```

---

## Contribución

Para contribuir al proyecto:
1. Fork el repositorio
2. Crear una rama para tu feature
3. Commit tus cambios
4. Push a la rama
5. Abrir un Pull Request

---

## Licencia

MIT License - Ver LICENSE para detalles

---

## Autor

Desarrollado como Taller 3 de JEE por Gabriel Camacho

---

## Contacto

Para preguntas o sugerencias, abrir un issue en GitHub.

---

**Última actualización**: 2026-04-23

## Reemplazo de módulo de correos con .NET

Se agregó un reemplazo en `dotnet-mensajes/MensajeriaApi` con:

- ASP.NET Core (.NET 8).
- Entity Framework Core + SQLite.
- API para recepción y consulta de mensajes por tipo.
- Estadísticas por tipo y dashboard web para visualización.
