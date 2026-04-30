# Diseño AS - Arquitectura de Software
## Taller 3 JEE - Sistema de Gestión de Curso de Estructuras de Datos

### 1. LÓGICA DEL NEGOCIO

#### 1.1 Procesos Principales
- **Carga de Contenido**: Batch que carga 36 clases teóricas y 34 prácticas
- **Simulación de Progreso**: Simula avance del estudiante a través de los temas
- **Motor de Recomendaciones**: Sugiere el siguiente tema con base en el avance

#### 1.2 Entidades Clave
- **Curso**: Estructura general del curso (36 temas teóricos, 34 prácticos)
- **Tema**: Unidad de contenido (teórica o práctica)
- **Estudiante**: Usuario del sistema
- **ProgresEstudiante**: Seguimiento de avance por tema
- **Recomendacion**: Sugerencia personalizada según desempeño

#### 1.3 Reglas de Negocio
1. Un estudiante debe completar el tema antes de pasar al siguiente
2. Las recomendaciones se basan en el porcentaje de comprensión (0-100%)
3. Si comprensión < 70%, se recomienda refuerzo antes de continuar
4. Si comprensión >= 70%, se recomienda el siguiente tema
5. Se considera completado cuando comprensión >= 80%

---

### 2. DESARROLLO - Arquitectura Técnica

#### 2.1 Arquitectura de Capas

```
┌─────────────────────────────────────────┐
│         Capa de Presentación             │
│   (REST Controllers, DTOs, Responses)    │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│      Capa de Aplicación/Servicios        │
│  (CourseService, StudentService,         │
│   RecommendationEngine, SimulationService)
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│      Capa de Persistencia                │
│   (JPA Repositories, Entities)           │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│         Base de Datos (H2/MySQL)        │
└─────────────────────────────────────────┘

Procesador Batch:
┌─────────────────────────────────────────┐
│    Spring Batch Job Configuration       │
│  (Lectura, Procesamiento, Escritura)    │
└─────────────────────────────────────────┘
```

#### 2.2 Componentes Principales

**1. Batch Job (CourseContentLoader)**
- ItemReader: Carga datos de CSV/JSON
- ItemProcessor: Valida y procesa datos
- ItemWriter: Persiste en BD

**2. Motor de Recomendaciones**
- Analiza progreso del estudiante
- Calcula comprensión por tema
- Genera recomendaciones personalizadas

**3. Simulador de Progreso**
- Simula solución de ejercicios
- Calcula puntuación y comprensión
- Actualiza registro de estudiante

**4. REST API**
- EndPoints para consultar cursos
- EndPoints para obtener progreso
- EndPoints para solicitar recomendaciones
- EndPoints para simular progreso

#### 2.3 Patrones de Diseño
- **Repository Pattern**: Abstracción de acceso a datos
- **Service Layer**: Lógica de negocio centralizada
- **DTO Pattern**: Transferencia de datos segura
- **Dependency Injection**: Gestión de dependencias con Spring

---

### 3. PROCESOS

#### 3.1 Proceso de Carga de Contenido (Batch)
1. Lectura de archivo data.json/CSV
2. Validación de estructura
3. Creación de entidades Tema
4. Persistencia en BD
5. Generación de log de carga

#### 3.2 Proceso de Simulación
1. Estudiante selecciona tema
2. Se simula resolución de ejercicios
3. Cálculo de puntuación (0-100%)
4. Actualización de ProgresEstudiante
5. Notificación para recomendación

#### 3.3 Proceso de Recomendación
1. Consultar progreso del estudiante
2. Identificar temas completados
3. Analizar comprensión de últimos temas
4. Determinar si refuerzo o avance
5. Generar recomendación personalizada

---

### 4. DESPLIEGUE

#### 4.1 Tecnología Stack
- **Lenguaje**: Java 17
- **Framework**: Spring Boot 3.2.0
- **Persistencia**: Spring Data JPA
- **Base de Datos**: H2 (dev), MySQL (prod)
- **Batch Processing**: Spring Batch
- **Build Tool**: Maven 3.8+

#### 4.2 Estructura de Despliegue
```
Aplicación JAR ejecutable
├── application.properties (Configuración)
├── batch-config.xml (Configuración Batch)
├── data.json (Datos de carga)
└── logs/ (Directorio de logs)
```

#### 4.3 Instrucciones de Ejecución
```bash
# Compilar
mvn clean package

# Ejecutar con H2
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=h2"

# Ejecutar Batch
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.batch.job.names=loadCourseContentJob"

# Ejecutar con MySQL
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=mysql"
```

#### 4.4 Base de Datos - Esquema
```sql
-- Tabla de Temas
CREATE TABLE tema (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(255),
    descripcion TEXT,
    tipo ENUM('TEORICA', 'PRACTICA'),
    numero_orden INT,
    created_at TIMESTAMP
);

-- Tabla de Estudiantes
CREATE TABLE estudiante (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(255),
    email VARCHAR(255),
    estado VARCHAR(50),
    created_at TIMESTAMP
);

-- Tabla de Progreso
CREATE TABLE progreso_estudiante (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    estudiante_id BIGINT,
    tema_id BIGINT,
    comprension INT (0-100),
    intentos INT,
    completado BOOLEAN,
    fecha_actualizacion TIMESTAMP,
    FOREIGN KEY (estudiante_id) REFERENCES estudiante(id),
    FOREIGN KEY (tema_id) REFERENCES tema(id)
);

-- Tabla de Recomendaciones
CREATE TABLE recomendacion (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    estudiante_id BIGINT,
    tema_actual_id BIGINT,
    tema_recomendado_id BIGINT,
    tipo ENUM('REFUERZO', 'AVANCE'),
    razon VARCHAR(500),
    fecha_creacion TIMESTAMP,
    FOREIGN KEY (estudiante_id) REFERENCES estudiante(id),
    FOREIGN KEY (tema_actual_id) REFERENCES tema(id),
    FOREIGN KEY (tema_recomendado_id) REFERENCES tema(id)
);
```

---

### 5. FLUJOS PRINCIPALES

#### 5.1 Flujo de Inicio de Aplicación
```
Inicio → Carga de Propiedades → Inicializa BD 
→ Reserva para Batch → Aplicación lista
```

#### 5.2 Flujo de Ejecución del Batch
```
Lectura CSV → Procesamiento → Validación 
→ Persistencia → Log de éxito
```

#### 5.3 Flujo de Consulta de Recomendación
```
GET /api/recomendaciones/{estudianteId} 
→ Servicio consulta progreso 
→ Motor calcula recomendación 
→ Respuesta JSON con recomendación
```

---

### 6. CASOS DE USO

| Caso de Uso | Actor | Descripción |
|---|---|---|
| CU-001 | Admin | Ejecutar batch de carga |
| CU-002 | Estudiante | Simular resolución de tema |
| CU-003 | Estudiante | Consultar progreso |
| CU-004 | Estudiante | Obtener recomendación |
| CU-005 | Profesor | Consultar progreso grupo |

---

### 7. SEGURIDAD Y CONSIDERACIONES

- Validación de entrada en todos los endpoints
- Manejo de excepciones centralizado
- Logs detallados de operaciones batch
- BD encriptada en producción
- Control de acceso por roles (básico)

