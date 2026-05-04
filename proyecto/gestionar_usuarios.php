<?php
session_start();
require_once("etc/parametros.php");
require_once("lib/libreria.php");
require_once("lib/restaurante.php");

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

$contenido = "";
$mensaje = "";

// --- LÓGICA DE BORRADO ---
if (isset($_GET['borrar'])) {
    $id_usr = intval($_GET['borrar']);
    $id_sesion = intval($_SESSION['id_usuario']);

    if ($id_usr == $id_sesion) {
        $mensaje = "No puede eliminar el usuario con la sesion activa.";
    } else {
        $refs = pg_query_params($conn, "
            SELECT
                (SELECT COUNT(*) FROM pedidos WHERE cliente_id = $1 OR mesero_id = $1) AS pedidos,
                (SELECT COUNT(*) FROM reservaciones WHERE cliente_id = $1) AS reservaciones,
                (SELECT COUNT(*) FROM especialidades WHERE cocinero_id = $1) AS especialidades,
                (SELECT COUNT(*) FROM preparaciones WHERE cocinero_id = $1) AS preparaciones",
            array($id_usr));
        $uso = $refs ? pg_fetch_assoc($refs) : array();
        $total_uso = array_sum(array_map('intval', $uso));

        if ($total_uso > 0) {
            $mensaje = "No se puede eliminar: el usuario tiene registros asociados en pedidos, reservas, especialidades o preparaciones.";
        } else {
            pg_query($conn, "BEGIN");
            $res_act = pg_query_params($conn, "DELETE FROM actuaciones WHERE usuario_id = $1", array($id_usr));
            $res_usr = pg_query_params($conn, "DELETE FROM usuarios WHERE id = $1", array($id_usr));

            if ($res_act && $res_usr && pg_affected_rows($res_usr) > 0) {
                pg_query($conn, "COMMIT");
                header("Location: gestionar_usuarios.php?msg=Usuario eliminado");
                exit;
            } else {
                pg_query($conn, "ROLLBACK");
                $mensaje = "Error al eliminar usuario: " . pg_last_error($conn);
            }
        }
    }
}

if (isset($_GET['msg'])) {
    $mensaje = $_GET['msg'];
}

// --- CONSULTA PARA EL LISTADO ---
$sql = "SELECT u.id, u.nombre, r.nombre AS nombre_rol 
        FROM usuarios u
        LEFT JOIN actuaciones a ON u.id = a.usuario_id
        LEFT JOIN roles r ON a.rol_id = r.id
        ORDER BY r.id NULLS LAST, u.nombre";

$res = procesar_query($sql, $conn);

if ($res->cantidad == 0) {
    $contenido = "<p>No hay usuarios.</p>";
} else {
    if ($mensaje != "") {
        $contenido .= "<div style='background:#fff3cd; color:#664d03; padding:10px; border-radius:4px; margin-bottom:12px;'>" . h($mensaje) . "</div>";
    }

    $rol_actual = "SIN_ROL_DEFINIDO";
    foreach ($res->datos as $u) {
        $rol_nombre = $u['nombre_rol'] ?? "Sin Rol Asignado";
        
        if ($rol_actual != $rol_nombre) {
            $rol_actual = $rol_nombre;
            $color = ($rol_actual == "Sin Rol Asignado") ? "#666" : "#004488";
            $contenido .= "<h2 style='background:$color; color:white; padding:10px;'>" . h($rol_actual) . "</h2>";
        }

        $contenido .= "
        <div style='border-bottom:1px solid #ccc; padding:10px; display:flex; justify-content:space-between; align-items:center;'>
            <span><strong>" . h($u['id']) . "</strong> - " . h($u['nombre']) . "</span>
            <div>
                <a href='editar_usuario.php?id=" . h($u['id']) . "'><button>Editar</button></a>
                <a href='?borrar=" . h($u['id']) . "' onclick='return confirm(\"¿Eliminar este usuario?\")'><button style='color:red;'>Borrar</button></a>
            </div>
        </div>";
    }
}

$esqueleto = file_get_contents("gestionar_usuarios.html");
echo sprintf($esqueleto, $contenido);
?>
