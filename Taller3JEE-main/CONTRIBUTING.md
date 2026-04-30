# Guía de Contribución - Taller3JEE

Agradecemos tu interés en contribuir al proyecto Taller3JEE. Esta guía te ayudará a entender cómo hacerlo.

## 📋 Código de Conducta

Por favor, sé respetuoso con los otros contribuyentes y mantén un ambiente positivo.

## 🐛 Reportar Bugs

### Antes de crear un reporte
- Verifica que el bug no haya sido reportado ya
- Usa la búsqueda de issues
- Intenta reproducir el error

### Cómo reportar un bug
1. **Usa un título descriptivo**
   - Malo: `Error en recomendaciones`
   - Bueno: `Error al calcular recomendación cuando comprensión = 0%`

2. **Describe los pasos exactos para reproducir el error**
   ```
   1. Crear estudiante
   2. Simular ejercicio
   3. Obtener recomendación
   → Se produce error X
   ```

3. **Proporciona ejemplos específicos**
   - Código utilizado
   - JSON enviado
   - Error exacto recibido

4. **Describe el comportamiento observado vs esperado**

5. **Incluye logs o screenshots si es posible**

### Plantilla de Reporte
```markdown
## Descripción
Una descripción clara del bug.

## Pasos para Reproducir
1. ...
2. ...

## Comportamiento Esperado
Qué debería pasar.

## Comportamiento Actual
Qué pasó en su lugar.

## Ambiente
- OS: 
- JDK: 
- Maven: 
```

## 💡 Sugerir Mejoras

Las mejoras son bienvenidas. Abre un issue con la etiqueta `enhancement`.

### Información útil
- **Descripción clara** de la mejora
- **Caso de uso** para la mejora
- **Ejemplos** de cómo sería
- **Posibles dificultades** de implementación

## 🔧 Configuración del Ambiente

### Requisitos
- JDK 17 o superior
- Maven 3.8+
- Git

### Configurar los repositories
```bash
git clone https://github.com/gabrielcamachoo/Taller3JEE.git
cd Taller3JEE

# Crear rama de desarrollo
git checkout -b develop

# Crear rama para tu feature
git checkout -b feature/descripcion-breve
```

### Compilar localmente
```bash
mvn clean install -DskipTests
```

### Ejecutar tests
```bash
mvn test
```

## 📝 Hacer cambios

### Convención de nombres
- Ramas: `feature/descripcion`, `bugfix/descripcion`, `docs/descripcion`
- Commits: Começar con verbo (Add, Fix, Update, Remove, Refactor)
- Variables: camelCase
- Clases: PascalCase
- Métodos: camelCase

### Guía de estilo
```java
// ✅ Bueno
public void metodoDescriptivo(String parametro) {
    // Código limpio con comentarios si es necesario
}

// ❌ Malo
public void m(String p) {
    // Código sin contexto
}
```

### Commits significativos
```bash
# Bueno
git commit -m "Add recommendation algorithm for students with low comprehension"
git commit -m "Fix batch processor error when tema.titulo is null"
git commit -m "Update documentation for MySQL configuration"

# Malo
git commit -m "fixes"
git commit -m "more stuff"
```

## 🧪 Testing

### Escribir tests
```java
@Test
public void shouldGenerateRecommendationWhenComprehensionLessThan70() {
    // Setup
    // Execute
    // Assert
}
```

### Ejecutar tests especí­ficos
```bash
mvn test -Dtest=RecommendationEngineImplTest
```

## ✅ Proceso de Pull Request

### 1. Fork el repositorio
```bash
# En GitHub, hacer click en "Fork"
```

### 2. Crear una rama
```bash
git checkout -b feature/tu-feature
```

### 3. Hacer cambios y commits
```bash
git add .
git commit -m "Add description of changes"
```

### 4. Push a tu fork
```bash
git push origin feature/tu-feature
```

### 5. Crear Pull Request
- Ve a GitHub
- Click en "Create Pull Request"
- Usa esta plantilla:

```markdown
## Descripción
Qué cambios hace este PR.

## Tipo de cambio
- [ ] Bug fix
- [ ] Nueva feature
- [ ] Mejora
- [ ] Changes Documentation

## Checklist
- [ ] Mi código sigue la guía de estilo del proyecto
- [ ] He ejecutado `mvn test` y todos pasan
- [ ] He actualizado la documentación si es necesario
- [ ] He testeado los cambios localmente
```

### 6. Revisión del código
- Los maintainers revisarán el código
- Pueden solicitar cambios
- Una vez aprobado, se hará merge

## 📦 División de Responsabilidades

### Áreas principales
- **Batch**: `/src/main/java/edu/ucol/taller3jee/batch/`
- **Services**: `/src/main/java/edu/ucol/taller3jee/service/`
- **API**: `/src/main/java/edu/ucol/taller3jee/controller/`
- **Database**: Entidades en `/src/main/java/edu/ucol/taller3jee/model/entity/`
- **Documentation**: `/docs/`

## 📚 Desarrollo de Features

### Feature: Nuevo Endpoint
1. Crear método en controlador
2. Implementar lógica en servicio
3. Agregar tests
4. Actualizar documentación
5. Crear PR

Ejemplo:
```java
// Controller
@GetMapping("/nuevo/{parametro}")
public ResponseEntity<Resultado> nuevoEndpoint(@PathVariable Long parametro) {
    return ResponseEntity.ok(servicio.nuevoMetodo(parametro));
}

// Service
public Resultado nuevoMetodo(Long parametro) {
    // Implementación
}

// Test
@Test
public void shouldReturnResultWhenParameterIsValid() {
    // Test
}
```

## 📖 Documentación

### Actualizar documentación
- README.md: General
- ARQUITECTURA_AS.md: Cambios arquitectónicos
- DISENO_DETALLADO.md: Cambios técnicos
- javadoc: Comentarios en código

### Ejemplo javadoc
```java
/**
 * Genera una recomendación personalizada basada en el progreso del estudiante.
 * 
 * @param estudianteId ID del estudiante
 * @return RecomendacionDTO con la recomendación personalizada
 * @throws RuntimeException si el estudiante no existe
 */
public RecomendacionDTO generarRecomendacion(Long estudianteId) {
    // Implementación
}
```

## 🚀 Antes de hacer Push

```bash
# 1. Verificar código
mvn clean compile

# 2. Ejecutar tests
mvn test

# 3. Verfar con coverage
mvn clean test jacoco:report

# 4. Verificar estilo
mvn checkstyle:check # (si está configurado)

# 5. Limpiar código local
git status
# Asegurate de no incluir archivos innecesarios
```

## 💬 Comunicación

- GitHub Issues: Para bugs y features
- GitHub Discussions: Para preguntas generales
- Commit messages: Claros y descriptivos

## 🎓 Recursos Útiles

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA Guide](https://spring.io/projects/spring-data-jpa)
- [Spring Batch Documentation](https://spring.io/projects/spring-batch)
- [Java Javadoc Style Guide](https://www.java.com/en/)

## ❓ Preguntas?

- Abre un GitHub Discussion
- Revisa documentación existente
- Pregunta en un issue

---

**¡Gracias por contribuir a Taller3JEE!** 🙌

