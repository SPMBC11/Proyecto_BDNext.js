# QUICKSTART - Inicio Rápido en 5 Minutos

## 1️⃣ Clonar y Compilar (2 minutos)

```bash
# Clonar repositorio
git clone https://github.com/gabrielcamachoo/Taller3JEE.git
cd Taller3JEE

# Compilar
mvn clean package -DskipTests
```

## 2️⃣ Ejecutar la Aplicación (1 minuto)

```bash
mvn spring-boot:run
```

**La aplicación estará lista en:** `http://localhost:8080/taller3jee`

El batch se ejecuta automáticamente y carga los 70 temas.

## 3️⃣ Probar la API (2 minutos)

### Crear un Estudiante
```bash
curl -X POST http://localhost:8080/taller3jee/api/v1/estudiantes \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Test Student",
    "email": "test@example.com",
    "matricula": "TEST001"
  }'
```

ID devuelto: **1**

### Simular Ejercicio
```bash
curl -X POST http://localhost:8080/taller3jee/api/v1/simulacion/ejercicio \
  -H "Content-Type: application/json" \
  -d '{
    "estudianteId": 1,
    "temaId": 1
  }'
```

### Ver Progreso
```bash
curl http://localhost:8080/taller3jee/api/v1/progreso/1
```

### Obtener Recomendación
```bash
curl http://localhost:8080/taller3jee/api/v1/recomendaciones/1
```

---

## ✅ ¿Funcionó?

Si ves respuestas JSON, ¡todo está funcionando correctamente!

## 📚 Próximos Pasos

- Ver [README.md](../README.md) para documentación completa
- Ver [INSTRUCCIONES.md](../INSTRUCCIONES.md) para más ejemplos
- Acceder a H2 Console: `http://localhost:8080/taller3jee/h2-console`
- Leer [RESUMEN_EJECUTIVO.md](RESUMEN_EJECUTIVO.md) para descripción de características

## 🔧 Solucionar Problemas

**Puerto 8080 en uso:**
```bash
# Cambiar en src/main/resources/application.properties
server.port=8081
```

**Maven no encontrado:**
```bash
# Instalar Maven
# Ubuntu: sudo apt-get install maven
# macOS: brew install maven
```

**Java no encontrado:**
```bash
# Descargar JDK 17 desde https://www.oracle.com/java/technologies/downloads/
```

---

¡Listo! Ya tienes Taller3JEE funcionando. 🚀
