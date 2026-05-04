# Documento Técnico — Sistema de Gestión de Restaurante

## 1. Descripción del framework
El sistema utiliza un **Framework PHP 8 personalizado** diseñado bajo una arquitectura de **Front Controller** simplificada. No utiliza frameworks externos (como Laravel o Symfony) para cumplir con los requisitos académicos de comprensión profunda de la lógica de negocio y manipulación directa de la base de datos PostgreSQL.

## 2. Arquitectura utilizada
- **Capa de Presentación**: Basada en HTML5 y CSS3. Utiliza plantillas dinámicas en PHP que separan la lógica de la vista mediante archivos `*_view.php`.
- **Capa de Lógica**: Centralizada en la carpeta `lib/`, donde funciones específicas manejan la autenticación (`usuarios.php`) y la lógica de restaurante (`restaurante.php`).
- **Capa de Datos**: PostgreSQL 14+. Se implementó una capa de abstracción ligera en `libreria.php` que garantiza el uso de sentencias preparadas (`pg_query_params`) para prevenir ataques de Inyección SQL.

## 3. Modelo de Datos
El sistema se basa en 11 tablas principales:
- `usuarios`, `roles`, `actuaciones`: Gestión de RBAC (Role Based Access Control).
- `mesas`, `horarios`, `reservaciones`: Control de flujo de clientes y disponibilidad.
- `platos`, `tipos`, `especialidades`: Gestión del menú y asignación de cocineros.
- `pedidos`, `ordenes`, `preparaciones`: Ciclo de vida del servicio (Solicitado -> Preparado -> Entregado).

## 4. Seguridad Implementada
- **Hashing**: Migración total de `bcrypt/bytea` a **SHA-256 VARCHAR(64)** para mayor compatibilidad y cumplimiento de requisitos.
- **Sesiones**: Protección de rutas mediante `session_start()` y verificación de privilegios por rol en cada carga de página.
- **Defensa SQL**: Eliminación de concatenación de variables en favor de parámetros posicionales.

## 5. Dificultades Encontradas
- **Manejo de estados concurrentes**: La transición entre Reservada, Ocupada y Liberada requirió una lógica estricta de validación para evitar solapamientos.
- **Cálculo de Reportes**: La agregación de ventas en tiempo real requirió el uso de funciones de fecha complejas en PostgreSQL (`date_trunc`, `interval`).
- **Migración de Contraseñas**: El cambio de esquema de datos exigió la recreación de los hashes de prueba.

## 6. Análisis del Framework
- **Ventajas**: Extrema ligereza, nulas dependencias externas, facilidad de despliegue en cualquier entorno LAMP/WAMP.
- **Desventajas**: Mayor esfuerzo en validación manual comparado con ORMs modernos.
- **Conclusión**: Ideal para sistemas críticos donde la transparencia del código y la seguridad de los datos son la prioridad absoluta.
