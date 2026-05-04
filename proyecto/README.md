# Restaurante Web — Proyecto Framework PHP

Sistema integral de gestión de restaurantes desarrollado como proyecto final para la asignatura de Bases de Datos Administración y Aplicaciones.

## Requisitos
- PHP 8.x con extensión `pgsql` activa.
- PostgreSQL 14 o superior.
- Servidor web (Apache/Nginx) o uso del servidor embebido de PHP.

## Instalación

1.  **Clonar o Descomprimir**: Coloque los archivos en su directorio raíz de servidor web (ej. `public_html/proyecto/`).
2.  **Configurar Base de Datos**:
    ```bash
    psql -U postgres -p 5434 -c "CREATE DATABASE restaurante2;"
    psql -U postgres -p 5434 -d restaurante2 < respaldo.sql
    ```
3.  **Editar Credenciales**: Modifique `etc/parametros.php` con sus datos locales.
4.  **Acceso**: Navegue a `http://localhost/proyecto/pagina_principal.php`.

## Usuarios de Prueba

| Usuario | Contraseña | Rol |
| :--- | :--- | :--- |
| `admin` | `P123` | Administrador |
| `maitre` | `P123` | Maitre |
| `mesero` | `P123` | Mesero |
| `cocina` | `P123` | Cocinero |

*Nota: Todas las contraseñas han sido migradas a SHA-256 por seguridad.*

## Estructura de Archivos
- `etc/`: Parámetros de conexión.
- `lib/`: Lógica de negocio y funciones base (usuarios, restaurante).
- `css/`: Estilos globales.
- `*.php`: Vistas y controladores por rol.
- `respaldo.sql`: Esquema completo de la base de datos.
