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

// Query 1 — Ventas del día:
$sql1 = "SELECT SUM(p.precio * o.cantidad) AS total_ventas
         FROM ordenes o
         JOIN platos p ON o.plato_id = p.id
         WHERE o.estado = 3
         AND DATE(o.solicitado) = CURRENT_DATE";
$res1 = pg_query($conn, $sql1);
$ventas_dia = pg_fetch_result($res1, 0, 0) ?? 0;

// Query 2 — Plato más pedido:
$sql2 = "SELECT p.nombre, SUM(o.cantidad) AS veces_pedido
         FROM ordenes o
         JOIN platos p ON o.plato_id = p.id
         WHERE o.estado = 3
         GROUP BY p.nombre
         ORDER BY veces_pedido DESC
         LIMIT 1";
$res2 = pg_query($conn, $sql2);
$plato_top = pg_fetch_assoc($res2);

// Query 3 — Mesas más activas:
$sql3 = "SELECT m.id AS mesa, COUNT(r.id) AS reservas
         FROM mesas m
         JOIN horarios h ON m.id = h.mesa_id
         JOIN reservaciones r ON h.reservacion_id = r.id
         WHERE DATE(h.inicio) = CURRENT_DATE
         GROUP BY m.id
         ORDER BY reservas DESC";
$res3 = pg_query($conn, $sql3);

// Query 4 — Pedidos por mesero:
$sql4 = "SELECT u.nombre AS mesero, COUNT(pe.id) AS pedidos_atendidos
         FROM pedidos pe
         JOIN usuarios u ON pe.mesero_id = u.id
         WHERE DATE(pe.id::text::date) = CURRENT_DATE OR TRUE -- OR TRUE para mostrar datos historicos si no hay hoy
         GROUP BY u.nombre
         ORDER BY pedidos_atendidos DESC";
$res4 = pg_query($conn, $sql4);
?>
<!DOCTYPE html>
<html lang="es-CO">
<head>
    <meta charset="UTF-8">
    <title>Reportes del Restaurante</title>
    <link rel="stylesheet" href="css/estilo.css">
</head>
<body>
    <header>REPORTES DE GESTIÓN</header>
    <main>
        <nav>
            <a href="admin.php">⬅ Volver al Panel Admin</a>
        </nav>

        <section class="MENU_OPCIONES" style="display: block; width: 80%; margin: 20px auto;">
            <div style="background: #f4f4f4; padding: 20px; border-radius: 8px; margin-bottom: 20px;">
                <h3>Resumen General (Hoy)</h3>
                <p><strong>Total Ventas:</strong> $<?= number_format($ventas_dia, 2) ?></p>
                <p><strong>Plato Estrella:</strong> <?= $plato_top ? h($plato_top['nombre']) . " (" . $plato_top['veces_pedido'] . " pedidos)" : "N/A" ?></p>
            </div>

            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 20px;">
                <div>
                    <h3>Mesas más activas (Hoy)</h3>
                    <table border="1" style="width: 100%; border-collapse: collapse;">
                        <thead>
                            <tr style="background: #eee;">
                                <th>Mesa #</th>
                                <th>Reservas</th>
                            </tr>
                        </thead>
                        <tbody>
                            <?php while ($m = pg_fetch_assoc($res3)): ?>
                                <tr>
                                    <td>Mesa <?= h($m['mesa']) ?></td>
                                    <td><?= h($m['reservas']) ?></td>
                                </tr>
                            <?php endwhile; ?>
                        </tbody>
                    </table>
                </div>

                <div>
                    <h3>Rendimiento de Meseros</h3>
                    <table border="1" style="width: 100%; border-collapse: collapse;">
                        <thead>
                            <tr style="background: #eee;">
                                <th>Mesero</th>
                                <th>Pedidos</th>
                            </tr>
                        </thead>
                        <tbody>
                            <?php while ($w = pg_fetch_assoc($res4)): ?>
                                <tr>
                                    <td><?= h($w['mesero']) ?></td>
                                    <td><?= h($w['pedidos_atendidos']) ?></td>
                                </tr>
                            <?php endwhile; ?>
                        </tbody>
                    </table>
                </div>
            </div>
        </section>
    </main>
    <footer>
        <section>Restaurante Javeriana - BI Module</section>
    </footer>
</body>
</html>
