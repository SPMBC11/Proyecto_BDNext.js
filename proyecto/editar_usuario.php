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

$id = intval($_GET['id'] ?? 0);
$mensaje = "";

if ($id <= 0) {
    header("Location: gestionar_usuarios.php");
    exit;
}

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $nombre = trim($_POST['nombre']);
    $rol_id = intval($_POST['rol_id']);

    $res_nombre = pg_query_params($conn, "SELECT 1 FROM usuarios WHERE lower(nombre) = lower($1) AND id <> $2", array($nombre, $id));
    $res_rol = pg_query_params($conn, "SELECT 1 FROM roles WHERE id = $1", array($rol_id));

    if ($nombre == "") {
        $mensaje = "El nombre es obligatorio.";
    } elseif ($res_nombre && pg_num_rows($res_nombre) > 0) {
        $mensaje = "Ya existe otro usuario con ese nombre.";
    } elseif (!$res_rol || pg_num_rows($res_rol) == 0) {
        $mensaje = "Debe seleccionar un rol valido.";
    } elseif (!empty($_POST['clave']) && strlen($_POST['clave']) < 4) {
        $mensaje = "La nueva contrasena debe tener minimo 4 caracteres.";
    } else {
        pg_query($conn, "BEGIN");

        $res_upd = pg_query_params($conn, "UPDATE usuarios SET nombre = $1 WHERE id = $2", array($nombre, $id));

        if (!empty($_POST['clave'])) {
            $nueva_clv = hash('sha256', $_POST['clave']);

            $res_clave = pg_query_params($conn, "UPDATE usuarios SET clave = $1, fecha_clave = CURRENT_TIMESTAMP WHERE id = $2", array($nueva_clv, $id));
        } else {
            $res_clave = true;
        }

        $res_check = pg_query_params($conn, "SELECT 1 FROM actuaciones WHERE usuario_id = $1", array($id));

        if ($res_check && pg_num_rows($res_check) > 0) {
            $res_rol_upd = pg_query_params($conn, "UPDATE actuaciones SET rol_id = $1 WHERE usuario_id = $2", array($rol_id, $id));
        } else {
            $res_rol_upd = pg_query_params($conn, "INSERT INTO actuaciones (rol_id, usuario_id) VALUES ($1, $2)", array($rol_id, $id));
        }

        if ($res_upd && $res_clave && $res_rol_upd) {
            pg_query($conn, "COMMIT");
            header("Location: gestionar_usuarios.php?msg=Usuario actualizado");
            exit;
        }

        pg_query($conn, "ROLLBACK");
        $mensaje = "Error al actualizar usuario: " . pg_last_error($conn);
    }
}

// Cargar datos actuales con LEFT JOIN para traer el rol si existe
$sql_user = "SELECT u.*, a.rol_id FROM usuarios u LEFT JOIN actuaciones a ON u.id = a.usuario_id WHERE u.id = $1";
$res_user = pg_query_params($conn, $sql_user, array($id));
$u = pg_fetch_assoc($res_user);

if (!$u) {
    die("Usuario no encontrado.");
}

$roles = procesar_query("SELECT * FROM roles ORDER BY nombre", $conn);

$opciones = "";
foreach ($roles->datos as $r) {
    $sel = ($r['id'] == $u['rol_id']) ? "selected" : "";
    $opciones .= "<option value='" . h($r['id']) . "' $sel>" . h($r['nombre']) . "</option>";
}

$contenido = "
    <h2>Editar Usuario #" . h($id) . "</h2>
    " . ($mensaje ? "<p style='color:#b00020;'>" . h($mensaje) . "</p>" : "") . "
    <form method='POST' style='display:grid; gap:10px; max-width:400px;'>
        <label>Nombre:</label>
        <input type='text' name='nombre' value='" . h($u['nombre']) . "' required>
        
        <label>Nueva Clave (opcional):</label>
        <input type='password' name='clave' placeholder='Dejar vacío para no cambiar'>
        
        <label>Rol asignado:</label>
        <select name='rol_id' required>
            <option value='' disabled " . ($u['rol_id'] == "" ? "selected" : "") . ">-- Seleccione un Rol --</option>
            $opciones
        </select>
        
        <button type='submit' style='background: #004488; color: white; padding: 10px; cursor: pointer;'>Actualizar Datos</button>
        <button type='button' onclick=\"location.href='gestionar_usuarios.php'\" style='padding: 10px;'>Volver</button>
    </form>
";

// Asegúrate de que el nombre del archivo HTML sea correcto
$esqueleto = file_get_contents("editar_usuario.html");
echo sprintf($esqueleto, $contenido);
?>
