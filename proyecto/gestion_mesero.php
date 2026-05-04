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

$roles_permitidos = ['Administrador', 'Mesero'];
if (!in_array($_SESSION['rol'], $roles_permitidos)) {
    header("Location: pagina_principal.php");
    exit();
}

$conn = pg_conectar($conn_string);

$mensaje = "";
$mesero_id = intval($_SESSION['id_usuario']);

// 1. REGISTRAR PEDIDO con multiples platos en una sola transaccion
if (isset($_POST['nuevo_pedido'])) {
    $cliente_id = intval($_POST['cliente_id']);
    $platos_ids = $_POST['plato_id']   ?? [];
    $cantidades  = $_POST['cantidad']   ?? [];

    // Filtrar filas vacias o invalidas
    $lineas = [];
    foreach ($platos_ids as $i => $pid) {
        $pid  = intval($pid);
        $cant = intval($cantidades[$i] ?? 0);
        if ($pid > 0 && $cant > 0) {
            $lineas[] = ['plato_id' => $pid, 'cantidad' => $cant];
        }
    }

    if ($cliente_id < 1) {
        $mensaje = "Debe seleccionar una mesa ocupada.";
    } elseif (empty($lineas)) {
        $mensaje = "Debe agregar al menos un plato con cantidad mayor a cero.";
    } else {
        pg_query($conn, "BEGIN");

        // Crear cabecera del pedido
        $res_ped = pg_query_params($conn,
            "INSERT INTO pedidos (cliente_id, mesero_id) VALUES ($1, $2) RETURNING id",
            array($cliente_id, $mesero_id));

        $ok = false;
        if ($res_ped) {
            $pedido_id = pg_fetch_assoc($res_ped)['id'];
            $ok = true;

            // Insertar cada linea de la orden
            foreach ($lineas as $linea) {
                $res_ord = pg_query_params($conn,
                    "INSERT INTO ordenes (plato_id, pedido_id, estado, cantidad, solicitado)
                     VALUES ($1, $2, 1, $3, CURRENT_TIMESTAMP)",
                    array($linea['plato_id'], $pedido_id, $linea['cantidad']));

                if (!$res_ord) {
                    $ok = false;
                    $mensaje = "Error al registrar plato: " . pg_last_error($conn);
                    break;
                }
            }
        } else {
            $mensaje = "Error al crear pedido: " . pg_last_error($conn);
        }

        if ($ok) {
            pg_query($conn, "COMMIT");
            $n = count($lineas);
            $mensaje = "Pedido #$pedido_id enviado a cocina ($n plato" . ($n > 1 ? 's' : '') . ").";
        } else {
            pg_query($conn, "ROLLBACK");
        }
    }
}

// 2. ENTREGAR PLATOS LISTOS. Solo el mesero del pedido puede cerrar la entrega.
if (isset($_GET['cambiar_estado'])) {
    $orden_id = intval($_GET['orden_id']);
    pg_query_params(
        $conn,
        "UPDATE ordenes o
         SET estado = 3
         FROM pedidos p
         WHERE p.id = o.pedido_id
           AND o.id = $1
           AND p.mesero_id = $2
           AND o.estado = 2",
        array($orden_id, $mesero_id)
    );
    header("Location: gestion_mesero.php");
    exit;
}

// --- CONSULTAS PARA LOS SELECTORES ---

// Solo platos disponibles
$platos = procesar_query("SELECT id, nombre, precio FROM platos ORDER BY nombre", $conn);

// Mesas que el Maitre ya puso en estado 2 (Ocupadas/Sentadas).
$sql_mesas = "SELECT m.id     AS mesa_id,
                     u.id     AS cliente_id,
                     u.nombre AS cliente_nom
              FROM reservaciones r
              JOIN horarios h  ON h.reservacion_id = r.id
              JOIN mesas m     ON m.id = h.mesa_id
              JOIN usuarios u  ON u.id = r.cliente_id
              WHERE r.estado = 2
              ORDER BY m.id ASC";
$mesas_activas = procesar_query($sql_mesas, $conn);

// Platos listos para entregar
$ordenes_listas = procesar_query("
    SELECT o.id, p.id as pedido_n, pl.nombre as plato, o.cantidad, o.estado, u.nombre AS cliente
    FROM ordenes o
    JOIN pedidos p  ON o.pedido_id = p.id
    JOIN usuarios u ON u.id = p.cliente_id
    JOIN platos pl  ON o.plato_id = pl.id
    WHERE o.estado = 2
      AND p.mesero_id = $mesero_id
    ORDER BY o.solicitado ASC", $conn);

// Pedidos en cocina para este mesero
$ordenes_pendientes = procesar_query("
    SELECT o.id, p.id as pedido_n, pl.nombre as plato, o.cantidad, o.estado, u.nombre AS cliente
    FROM ordenes o
    JOIN pedidos p  ON o.pedido_id = p.id
    JOIN usuarios u ON u.id = p.cliente_id
    JOIN platos pl  ON o.plato_id = pl.id
    WHERE o.estado = 1
      AND p.mesero_id = $mesero_id
    ORDER BY o.id DESC", $conn);

include("mesero_view.php");
?>
