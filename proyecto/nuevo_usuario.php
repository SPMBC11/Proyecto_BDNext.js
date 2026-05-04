<?php
session_start();
require_once("etc/parametros.php");
require_once("lib/libreria.php");

if (!isset($_SESSION['id_usuario'])) {
    header("Location: pagina_principal.php");
    exit;
}

$roles_permitidos = ['Administrador'];
if (!in_array($_SESSION['rol'], $roles_permitidos)) {
    header("Location: pagina_principal.php");
    exit();
}

$conn = pg_conectar($conn_string);

$mensaje = "";

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $nombre = trim($_POST['nombre']);
    $clave  = hash('sha256', $_POST['clave']);

    $rol_id = intval($_POST['rol_id']);

    $res_nombre = pg_query_params($conn, "SELECT 1 FROM usuarios WHERE lower(nombre) = lower($1)", array($nombre));
    $res_rol = pg_query_params($conn, "SELECT 1 FROM roles WHERE id = $1", array($rol_id));

    if ($nombre == "" || strlen($_POST['clave']) < 4) {
        $mensaje = "Nombre y contrasena son obligatorios; la contrasena debe tener minimo 4 caracteres.";
    } elseif ($res_nombre && pg_num_rows($res_nombre) > 0) {
        $mensaje = "Ya existe un usuario con ese nombre.";
    } elseif (!$res_rol || pg_num_rows($res_rol) == 0) {
        $mensaje = "Debe seleccionar un rol valido.";
    } else {
        pg_query($conn, "BEGIN");
        $sql_u = "INSERT INTO usuarios (nombre, clave, fecha_clave) VALUES ($1, $2, CURRENT_TIMESTAMP) RETURNING id";
        $res_u = pg_query_params($conn, $sql_u, array($nombre, $clave));

        if ($res_u) {
            $fila = pg_fetch_assoc($res_u);
            $nuevo_id = $fila['id'];

            $sql_a = "INSERT INTO actuaciones (rol_id, usuario_id) VALUES ($1, $2)";
            $res_a = pg_query_params($conn, $sql_a, array($rol_id, $nuevo_id));

            if ($res_a) {
                pg_query($conn, "COMMIT");
                header("Location: gestionar_usuarios.php?msg=Usuario creado");
                exit;
            }
        }

        pg_query($conn, "ROLLBACK");
        $mensaje = "Error al crear usuario: " . pg_last_error($conn);
    }
}

// Obtener roles para el select
$roles = procesar_query("SELECT * FROM roles ORDER BY nombre", $conn);

$opciones = "";
foreach ($roles->datos as $r) {
    $opciones .= "<option value='" . h($r['id']) . "'>" . h($r['nombre']) . "</option>";
}

$contenido = "
    <h2>Registrar Nuevo Personal</h2>
    " . ($mensaje ? "<p style='color:#b00020;'>" . h($mensaje) . "</p>" : "") . "
    <form method='POST' style='display:grid; gap:10px; max-width:400px;'>
        <label>Nombre Completo:</label>
        <input type='text' name='nombre' required>
        
        <label>Contraseña:</label>
        <input type='password' name='clave' required>

        <label>Asignar Rol:</label>
        <select name='rol_id'>$opciones</select>

        <button type='submit' style='background:green; color:white;'>Guardar</button>
        <button type='button' onclick=\"location.href='gestionar_usuarios.php'\">Cancelar</button>
    </form>
";

$esqueleto = file_get_contents("editar_usuario.html");
echo sprintf($esqueleto, $contenido);
?>
