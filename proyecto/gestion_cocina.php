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

$roles_permitidos = ['Administrador', 'Cocinero'];
if (!in_array($_SESSION['rol'], $roles_permitidos)) {
    header("Location: pagina_principal.php");
    exit();
}

$conn = pg_conectar($conn_string);

$cocinero_id = intval($_SESSION['id_usuario']);
$mensaje = $_GET['msg'] ?? "";

// 1. LÓGICA: CAMBIAR ESTADO A "PREPARADO" (Estado 2)
if (isset($_GET['listo'])) {
    $orden_id = intval($_GET['listo']);

    pg_query($conn, "BEGIN");

    // La tabla preparaciones registra que cocinero preparo la orden.
    // El trigger de base de datos valida que el plato pertenezca a sus especialidades.
    $res_preparacion = pg_query_params(
        $conn,
        "INSERT INTO preparaciones (cocinero_id, orden_id)
         SELECT $1, o.id
         FROM ordenes o
         JOIN especialidades e ON e.plato_id = o.plato_id AND e.cocinero_id = $1
         WHERE o.id = $2
           AND o.estado = 1",
        array($cocinero_id, $orden_id)
    );

    $res = false;
    if ($res_preparacion && pg_affected_rows($res_preparacion) === 1) {
        // Actualizamos a estado 2 (Preparado).
        $res = pg_query_params($conn, "UPDATE ordenes SET estado = 2 WHERE id = $1 AND estado = 1", array($orden_id));
    }

    if ($res && pg_affected_rows($res) === 1) {
        pg_query($conn, "COMMIT");
        header("Location: gestion_cocina.php?msg=Plato listo");
    } else {
        pg_query($conn, "ROLLBACK");
        header("Location: gestion_cocina.php?msg=No se pudo marcar el plato. Revise especialidades.");
    }
    exit;
}

// 2. El cocinero solo ve platos registrados en sus especialidades.
$sql = "SELECT 
            o.id AS orden_id, 
            pl.nombre AS plato, 
            o.cantidad, 
            pl.tiempo, 
            o.solicitado
        FROM ordenes o
        JOIN platos pl ON o.plato_id = pl.id
        JOIN especialidades e ON e.plato_id = o.plato_id
        WHERE o.estado = 1
          AND e.cocinero_id = $cocinero_id
        ORDER BY pl.tiempo DESC, o.solicitado ASC";

$ordenes_pendientes = procesar_query($sql, $conn);

include("cocina_view.php");
?>
