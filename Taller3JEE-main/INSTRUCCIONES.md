# INSTRUCCIONES DE USO - Taller3JEE

## Guía Rápida de Inicio

### 1. Compilar y Ejecutar

```bash
# Compilar
mvn clean package -DskipTests

# Ejecutar
mvn spring-boot:run
```

### 2. Cargar Datos en la Base de Datos (Batch)

El batch se ejecuta automáticamente al iniciar la aplicación. Los 70 temas se cargarán en H2 desde `data.json`.

Para verificar que se cargaron correctamente:
```bash
# Acceder a H2 Console
http://localhost:8080/taller3jee/h2-console

# Query para verificar
SELECT COUNT(*) FROM tema;
```

### 3. Crear un Estudiante

```bash
curl -X POST http://localhost:8080/taller3jee/api/v1/estudiantes \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Juan Pérez García",
    "email": "juan.perez@example.com",
    "matricula": "2024001"
  }'
```

**Respuesta:**
```json
{
  "id": 1,
  "nombre": "Juan Pérez García",
  "email": "juan.perez@example.com",
  "matricula": "2024001",
  "estado": "ACTIVO",
  "fechaCreacion": "2026-04-23T10:30:00"
}
```

### 4. Simular un Ejercicio

```bash
curl -X POST http://localhost:8080/taller3jee/api/v1/simulacion/ejercicio \
  -H "Content-Type: application/json" \
  -d '{
    "estudianteId": 1,
    "temaId": 1
  }'
```

**Respuesta:**
```json
{
  "estudianteId": 1,
  "temaId": 1,
  "aciertos": 3,
  "total": 5,
  "puntuacion": 60,
  "nivelComprensionActualizado": 60,
  "completado": false,
  "timestamp": "2026-04-23T10:31:00"
}
```

### 5. Obtener Progreso

```bash
curl http://localhost:8080/taller3jee/api/v1/progreso/1
```

### 6. Generar Recomendación

```bash
curl http://localhost:8080/taller3jee/api/v1/recomendaciones/1
```

**Respuesta (comprensión < 70):**
```json
{
  "id": 1,
  "estudianteId": 1,
  "temaActualId": 1,
  "temaRecomendadoId": 1,
  "temaRecomendadoTitulo": "Introducción a Estructuras de Datos",
  "tipo": "REFUERZO",
  "razon": "Comprensión insuficiente (60%). Se recomienda refuerzo antes de continuar.",
  "nivelConfianza": 0.8,
  "fechaCreacion": "2026-04-23T10:32:00"
}
```

---

## Flujos Completos

### Flujo Completo: Estudiante Nuevo

```bash
# 1. Crear estudiante
export ESTUDIANTE_ID=$(curl -s -X POST http://localhost:8080/taller3jee/api/v1/estudiantes \
  -H "Content-Type: application/json" \
  -d '{"nombre":"María López","email":"maria@example.com","matricula":"2024002"}' \
  | jq '.id')

# 2. Estudiar tema 1 (simular ejercicio)
curl -X POST http://localhost:8080/taller3jee/api/v1/simulacion/ejercicio \
  -H "Content-Type: application/json" \
  -d "{\"estudianteId\":$ESTUDIANTE_ID,\"temaId\":1}"

# 3. Simular 5 ejercicios más
curl -X POST http://localhost:8080/taller3jee/api/v1/simulacion/lote \
  -H "Content-Type: application/json" \
  -d "{\"estudianteId\":$ESTUDIANTE_ID,\"temaId\":1,\"cantidad\":5}"

# 4. Ver progreso
curl http://localhost:8080/taller3jee/api/v1/progreso/$ESTUDIANTE_ID

# 5. Obtener recomendación
curl http://localhost:8080/taller3jee/api/v1/recomendaciones/$ESTUDIANTE_ID

# 6. Ver análisis detallado
curl http://localhost:8080/taller3jee/api/v1/recomendaciones/$ESTUDIANTE_ID/analisis
```

### Flujo Completo: Múltiples Estudiantes

```bash
# Script para crear 5 estudiantes y generar datos
for i in {1..5}; do
  echo "Creando estudiante $i..."
  curl -s -X POST http://localhost:8080/taller3jee/api/v1/estudiantes \
    -H "Content-Type: application/json" \
    -d "{
      \"nombre\": \"Estudiante $i\",
      \"email\": \"estudiante$i@example.com\",
      \"matricula\": \"202400$i\"
    }" | jq .
  
  sleep 1
done

# Listar todos los estudiantes
curl http://localhost:8080/taller3jee/api/v1/estudiantes | jq .
```

---

## Postman Collection

```json
{
  "info": {
    "name": "Taller3JEE",
    "description": "API del sistema de gestión de cursos"
  },
  "item": [
    {
      "name": "Crear Estudiante",
      "request": {
        "method": "POST",
        "header": [
          {"key": "Content-Type", "value": "application/json"}
        ],
        "url": {
          "raw": "http://localhost:8080/taller3jee/api/v1/estudiantes",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["taller3jee", "api", "v1", "estudiantes"]
        },
        "body": {
          "mode": "raw",
          "raw": "{\"nombre\":\"Test\",\"email\":\"test@example.com\",\"matricula\":\"TEST\"}"
        }
      }
    },
    {
      "name": "Simular Ejercicio",
      "request": {
        "method": "POST",
        "url": {
          "raw": "http://localhost:8080/taller3jee/api/v1/simulacion/ejercicio",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["taller3jee", "api", "v1", "simulacion", "ejercicio"]
        },
        "body": {
          "mode": "raw",
          "raw": "{\"estudianteId\":1,\"temaId\":1}"
        }
      }
    },
    {
      "name": "Obtener Recomendación",
      "request": {
        "method": "GET",
        "url": {
          "raw": "http://localhost:8080/taller3jee/api/v1/recomendaciones/1",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["taller3jee", "api", "v1", "recomendaciones", "1"]
        }
      }
    }
  ]
}
```

---

## Casos de Prueba

### Caso 1: Estudiante Principiante
- Crear estudiante nuevo
- Hacer ejercicios en tema 1 (resultado: comprensión 45%)
- Generar recomendación → Debe ser REFUERZO
- Hacer más ejercicios
- Verificar aumento de comprensión

### Caso 2: Progresión Normal
- Hacer ejercicios hasta comprensión 75%
- Generar recomendación → Debe ser AVANCE MODERADO (0.7 confianza)
- Hacer más ejercicios hasta 85%
- Generar recomendación → Debe ser AVANCE (0.95 confianza)

### Caso 3: Múltiples Temas
- Crear estudiante
- Hacer ejercicios en tema 1 hasta 90% (completado)
- Hacer ejercicios en tema 2 hasta 70%
- Ver progreso global
- Obtener análisis detallado

---

## Queries Útiles para H2 Console

```sql
-- Ver todos los temas
SELECT * FROM tema ORDER BY numero_orden;

-- Ver todos los estudiantes
SELECT * FROM estudiante;

-- Ver progreso de un estudiante
SELECT p.*, t.titulo 
FROM progreso_estudiante p
JOIN tema t ON p.tema_id = t.id
WHERE p.estudiante_id = 1
ORDER BY t.numero_orden;

-- Ver promedio de comprensión por estudiante
SELECT e.nombre, AVG(p.nivel_comprension) as promedio
FROM estudiante e
LEFT JOIN progreso_estudiante p ON e.id = p.estudiante_id
GROUP BY e.id, e.nombre;

-- Ver recomendaciones recientes
SELECT r.*, e.nombre, t1.titulo as tema_actual, t2.titulo as tema_recomendado
FROM recomendacion r
JOIN estudiante e ON r.estudiante_id = e.id
LEFT JOIN tema t1 ON r.tema_actual_id = t1.id
JOIN tema t2 ON r.tema_recomendado_id = t2.id
ORDER BY r.fecha_creacion DESC;

-- Ver estudiantes completados
SELECT COUNT(*) as total_completados, COUNT(DISTINCT estudiante_id) as estudiantes
FROM progreso_estudiante
WHERE completado = true;
```

---

## Troubleshooting

### El batch no cargó datos
```bash
# Verificar en los logs
tail -f logs/application.log | grep "Tema"

# Verificar en H2
SELECT COUNT(*) FROM BATCH_JOB_EXECUTION;
```

### Error de conexión a BD
```bash
# Verificar propiedades
cat src/main/resources/application.properties | grep datasource

# Reiniciar aplicación
# Los datos de H2 se reinician cada ejecución en modo create-drop
```

### Puerto 8080 en uso
```bash
# Cambiar puerto en application.properties
server.port=8081
```

---

## Performance

- Cargue de 70 temas: ~2-3 segundos
- Simulación de ejercicio: <100ms
- Generación de recomendación: ~200ms
- Cálculo de análisis: <500ms

---

## Próximos Pasos

1. Integrar autenticación (Spring Security)
2. Agregar Swagger/OpenAPI
3. Implementar WebSocket para notificaciones
4. Crear frontend (React/Angular)
5. Agregar más tipos de ejercicios
6. Implementar estadísticas avanzadas

