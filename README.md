# Proyecto Taller3JEE / TALLERNET

Este repositorio contiene un sistema de gestión de cursos y recomendaciones desarrollado en **Java (Spring Boot)**, integrado con un sistema de mensajería basado en **.NET Core** y **RabbitMQ**.

## Requisitos previos

- **Docker** y **Docker Compose**
- **Java 17** o superior
- **Maven** (o se utilizará el wrapper provisto)
- **Git**
- **cURL** (para el script de inicio y seed de datos)

## Arquitectura

El proyecto está dividido en varios servicios, que corren mediante Docker y Spring Boot de forma local:

- **Spring Boot App (Taller3JEE)**: Aplicación principal de gestión de cursos y estudiantes. (Puerto: `8080`)
- **JEE Publisher**: Publicador JAX-RS que envía mensajes a RabbitMQ. (Puerto: `8081`)
- **PostgreSQL**: Base de datos para la mensajería. (Puerto: `5432`)
- **RabbitMQ**: Message broker. (Puertos: `5672`, `15672`)
- **Mensajeria API (.NET)**: API de consulta de mensajería. (Puerto: `5000`)
- **Mensajeria Worker (.NET)**: Worker de procesamiento en segundo plano.
- **Mailpit**: Pruebas de envío de correo. (Puertos: `1025`, `8025`)

---

## Cómo ejecutar el proyecto

### 1. Iniciar la infraestructura con Docker Compose

La infraestructura y los servicios en .NET deben estar corriendo antes de levantar la aplicación principal. 

Abre una terminal y colócate en el directorio `Taller3JEE-main`:

```bash
cd Taller3JEE-main
docker-compose up -d --build
```

Esto levantará los contenedores de RabbitMQ, PostgreSQL, Mailpit, las APIs de .NET y el Publisher JEE.

### 2. Iniciar la aplicación de Spring Boot (Taller3JEE)

Desde el mismo directorio (`Taller3JEE-main`), puedes utilizar el script provisto que levantará la aplicación e insertará datos de prueba:

**En Linux / Mac / Git Bash (Windows):**
```bash
./run_taller3jee.sh
```

**De forma manual (usando Maven):**
Si prefieres no usar el script, puedes compilar y ejecutar directamente con Maven:
```bash
mvn clean install -DskipTests
mvn spring-boot:run
```

### 3. Verificar el funcionamiento

Una vez que todos los servicios hayan iniciado, puedes acceder a:

- **Aplicación Frontend / API Spring Boot**: [http://localhost:8080/taller3jee](http://localhost:8080/taller3jee)
- **API Mensajería .NET**: [http://localhost:5000/swagger](http://localhost:5000/swagger) (o según configuración)
- **Panel RabbitMQ**: [http://localhost:15672](http://localhost:15672) (guest / guest)
- **Mailpit (Correos simulados)**: [http://localhost:8025](http://localhost:8025)

## Apagado

Para detener todos los servicios y la infraestructura:

1. Detén el script o el proceso de Spring Boot (`Ctrl + C` en la terminal).
2. Detén los contenedores de Docker:
```bash
cd Taller3JEE-main
docker-compose down
```
