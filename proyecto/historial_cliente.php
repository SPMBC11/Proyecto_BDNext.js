<?php
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

$cliente_id = intval($_GET['id'] ?? 0);
if ($cliente_id <= 0) {
    die("ID de cliente invalido.");
}

// Obtener nombre del cliente
$res_c = pg_query_params($conn, "SELECT nombre FROM usuarios WHERE id = $1", array($cliente_id));
$cliente = pg_fetch_assoc($res_c);

// Query principal:
$sql = "SELECT r.id, m.id AS mesa, r.cantidad, h.inicio, r.estado,
               COUNT(o.id) AS platos_pedidos,
               COALESCE(SUM(p.precio * o.cantidad), 0) AS total_consumido
        FROM reservaciones r
        JOIN horarios h ON r.id = h.reservacion_id
        JOIN mesas m ON h.mesa_id = m.id
        LEFT JOIN pedidos pe ON r.cliente_id = pe.cliente_id
        LEFT JOIN ordenes o ON pe.id = o.pedido_id AND o.estado = 3
        LEFT JOIN platos p ON o.plato_id = p.id
        WHERE r.cliente_id = $1
        GROUP BY r.id, m.id, r.cantidad, h.inicio, r.estado
        ORDER BY h.inicio DESC";

$res = pg_query_params($conn, $sql, array($cliente_id));

$estados = array(
    1 => 'Pendiente',
    2 => 'Ocupada',
    3 => 'Liberada',
    4 => 'Cancelada'
);
?>
<!DOCTYPE html>
<html lang="es-CO">
<head>
    <meta charset="UTF-8">
    <title>Historial de Cliente</title>
    <link rel="stylesheet" href="css/estilo.css">
</head>
<body>
    <header>HISTORIAL DEL CLIENTE: <?= $cliente ? h($cliente['nombre']) : "Desconocido" ?></header>
    <main>
        <nav>
            <a href="gestionar_reservas.php">⬅ Volver a Reservas</a>
        </nav>

        <section style="width: 90%; margin: 20px auto;">
            <table border="1" style="width: 100%; border-collapse: collapse; text-align: center;">
                <thead>
                    <tr style="background: #004488; color: white;">
                        <th>ID Reserva</th>
                        <th>Fecha y Hora</th>
                        <th>Mesa</th>
                        <th>Personas</th>
                        <th>Estado</th>
                        <th>Platos Consumidos</th>
                        <th>Total Gastado</th>
                    </tr>
                </thead>
                <tbody>
                    <?php while ($fila = pg_fetch_assoc($res)): ?>
                        <tr>
                            <td><?= h($fila['id']) ?></td>
                            <td><?= h($fila['inicio']) ?></td>
                            <td>Mesa <?= h($fila['mesa']) ?></td>
                            <td><?= h($fila['cantidad']) ?></td>
                            <td><?= h($estados[$fila['estado']] ?? 'Desconocido') ?></td>
                            <td><?= h($fila['platos_pedidos']) ?></td>
                            <td>$<?= number_format((float)$fila['total_consumido'], 2) ?></td>
                        </tr>
                    <?php endwhile; ?>
                    <?php if (pg_num_rows($res) == 0): ?>
                        <tr><td colspan="7">No hay historial para este cliente.</td></tr>
                    <?php endif; ?>
                </tbody>
            </table>
        </section>
    </main>
</body>
</html>
