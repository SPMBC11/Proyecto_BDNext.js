<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
session_start();
require_once("etc/parametros.php");
require_once("lib/libreria.php");

if (!isset($_SESSION['id_usuario'])) {
    header("Location: pagina_principal.php");
    exit;
}

$roles_permitidos = ['Administrador', 'Maitre'];
if (!in_array($_SESSION['rol'], $roles_permitidos)) {
    header("Location: pagina_principal.php");
    exit();
}

$conn = pg_conectar($conn_string);


$mensaje = "";
$reserva_editar = null;

function validar_reserva($conn, $mesa_id, $fecha_inicio, $personas, $reservacion_id = null)
{
    if ($personas < 1) {
        return "Error: La cantidad de personas debe ser mayor a cero.";
    }

    $res_mesa = pg_query_params($conn, "SELECT sillas FROM mesas WHERE id = $1", array($mesa_id));
    $mesa = $res_mesa ? pg_fetch_assoc($res_mesa) : false;

    if (!$mesa) {
        return "Error: Mesa no encontrada.";
    }

    if ($mesa['sillas'] < $personas) {
        return "Error: Capacidad insuficiente ({$mesa['sillas']} sillas).";
    }

    $params = array($mesa_id, $fecha_inicio);
    $excluir = "";
    if ($reservacion_id !== null) {
        $params[] = $reservacion_id;
        $excluir = "AND h.reservacion_id <> $3";
    }

    $sql_cruce = "SELECT 1
                  FROM horarios h
                  JOIN reservaciones r ON r.id = h.reservacion_id
                  WHERE h.mesa_id = $1
                    AND r.estado IN (1, 2)
                    $excluir
                    AND (h.inicio, h.inicio + h.duracion)
                        OVERLAPS ($2::timestamp, $2::timestamp + interval '1 hour 59 minutes')";
    $res_cruce = pg_query_params($conn, $sql_cruce, $params);

    if ($res_cruce && pg_num_rows($res_cruce) > 0) {
        return "Error: Mesa ocupada en ese horario.";
    }

    $params_cupo = array($fecha_inicio);
    $excluir_cupo = "";
    if ($reservacion_id !== null) {
        $params_cupo[] = $reservacion_id;
        $excluir_cupo = "AND h.reservacion_id <> $2";
    }

    $res_cupo = pg_query_params($conn, "
        SELECT COALESCE(SUM(r.cantidad), 0) AS ocupadas,
               (SELECT COALESCE(SUM(sillas), 0) FROM mesas) AS cupo
        FROM horarios h
        JOIN reservaciones r ON r.id = h.reservacion_id
        WHERE r.estado IN (1, 2)
          $excluir_cupo
          AND (h.inicio, h.inicio + h.duracion)
              OVERLAPS ($1::timestamp, $1::timestamp + interval '1 hour 59 minutes')",
        $params_cupo);
    $cupo = $res_cupo ? pg_fetch_assoc($res_cupo) : array('ocupadas' => 0, 'cupo' => 0);

    if (((int)$cupo['ocupadas'] + $personas) > (int)$cupo['cupo']) {
        return "Error: Se supera el cupo total del restaurante en ese horario.";
    }

    return "";
}

// A. LÓGICA: SENTAR CLIENTE (Estado 1 -> 2)
if (isset($_GET['sentar'])) {
    $res_id = intval($_GET['sentar']);
    $sql_sentar = "UPDATE reservaciones SET estado = 2 WHERE id = $1 AND estado = 1";
    $res_upd = pg_query_params($conn, $sql_sentar, array($res_id));
    $mensaje = ($res_upd && pg_affected_rows($res_upd) > 0) ? "Cliente sentado. Mesa ahora OCUPADA." : "Error: La reserva no esta disponible para sentar.";
}

// B. LÓGICA: LIBERAR MESA (Estado 2 -> 3)
if (isset($_GET['liberar'])) {
    $res_id = intval($_GET['liberar']);
    $sql_liberar = "UPDATE reservaciones SET estado = 3 WHERE id = $1 AND estado = 2";
    $res_upd = pg_query_params($conn, $sql_liberar, array($res_id));
    $mensaje = ($res_upd && pg_affected_rows($res_upd) > 0) ? "Mesa LIBERADA y disponible." : "Error: La reserva no esta ocupada.";
}

// C. LÓGICA: CANCELAR RESERVA FORMALMENTE (Estado 1 -> 4)
if (isset($_GET['cancelar'])) {
    $res_id = intval($_GET['cancelar']);
    $sql_cancelar = "UPDATE reservaciones SET estado = 4 WHERE id = $1 AND estado = 1";
    $res_upd = pg_query_params($conn, $sql_cancelar, array($res_id));
    $mensaje = ($res_upd && pg_affected_rows($res_upd) > 0)
        ? "Reservación cancelada."
        : "Error: Solo se pueden cancelar reservas pendientes.";
}

// D. LÓGICA: CREAR O MODIFICAR RESERVA
if ($_SERVER['REQUEST_METHOD'] == 'POST' && ($_POST['accion'] ?? '') == 'guardar_reserva') {
    $reservacion_id = !empty($_POST['reservacion_id']) ? intval($_POST['reservacion_id']) : null;
    $es_edicion = ($reservacion_id !== null);
    $cliente_id = intval($_POST['cliente_id']);
    $mesa_id = intval($_POST['mesa_id']);
    $fecha_inicio = $_POST['fecha'] . " " . $_POST['hora'] . ":00";
    $personas = intval($_POST['cantidad']);
    $error_validacion = validar_reserva($conn, $mesa_id, $fecha_inicio, $personas, $reservacion_id);

    if ($error_validacion !== "") {
        $mensaje = $error_validacion;
    } else {
        pg_query($conn, "BEGIN");
        if (!$es_edicion) {
            $res_ins = pg_query_params($conn, "INSERT INTO reservaciones (cliente_id, cantidad, estado) VALUES ($1, $2, 1) RETURNING id", array($cliente_id, $personas));

            if ($res_ins) {
                $reservacion_id = pg_fetch_assoc($res_ins)['id'];
                $res_hor = pg_query_params($conn, "INSERT INTO horarios (mesa_id, reservacion_id, inicio, duracion) VALUES ($1, $2, $3, interval '1 hour 59 minutes')", array($mesa_id, $reservacion_id, $fecha_inicio));
            } else {
                $res_hor = false;
            }
        } else {
            $res_ins = pg_query_params($conn, "UPDATE reservaciones SET cliente_id = $1, cantidad = $2 WHERE id = $3 AND estado = 1", array($cliente_id, $personas, $reservacion_id));
            $res_hor = pg_query_params($conn, "UPDATE horarios SET mesa_id = $1, inicio = $2, duracion = interval '1 hour 59 minutes' WHERE reservacion_id = $3", array($mesa_id, $fecha_inicio, $reservacion_id));
        }

        if ($res_ins && $res_hor && (!$es_edicion || pg_affected_rows($res_ins) > 0) && pg_affected_rows($res_hor) > 0) {
            pg_query($conn, "COMMIT");
            $mensaje = $es_edicion ? "Reservación actualizada con éxito." : "Reservación creada con éxito.";
        } else {
            pg_query($conn, "ROLLBACK");
            $mensaje = "Error DB: " . pg_last_error($conn);
        }
    }
}

// CARGAR DATOS
$clientes = procesar_query("SELECT u.id, u.nombre FROM usuarios u JOIN actuaciones a ON u.id = a.usuario_id JOIN roles r ON a.rol_id = r.id WHERE r.nombre ILIKE 'cliente' ORDER BY u.nombre", $conn);
$mesas = procesar_query("SELECT id, sillas FROM mesas ORDER BY id", $conn);

if (isset($_GET['editar'])) {
    $editar_id = intval($_GET['editar']);
    $res_edit = pg_query_params($conn, "
        SELECT r.id AS res_id, r.cliente_id, r.cantidad, r.estado, h.mesa_id, h.inicio
        FROM reservaciones r
        JOIN horarios h ON h.reservacion_id = r.id
        WHERE r.id = $1
          AND r.estado = 1", array($editar_id));
    $reserva_editar = $res_edit ? pg_fetch_assoc($res_edit) : null;
    if (!$reserva_editar && $mensaje == "") {
        $mensaje = "No se puede editar una reserva que no este pendiente.";
    }
}

$listado = procesar_query("
    SELECT r.id AS res_id, u.nombre as cliente, r.cantidad, m.id as mesa_n, h.inicio, r.estado 
    FROM reservaciones r 
    JOIN usuarios u ON r.cliente_id = u.id 
    JOIN horarios h ON r.id = h.reservacion_id 
    JOIN mesas m ON h.mesa_id = m.id 
    WHERE h.inicio >= CURRENT_DATE 
    ORDER BY h.inicio ASC", $conn);

include("maitre_view.php");
?>
