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

$conn = pg_conectar($host, $dbname, $user, $password);
$mensaje = "";

if (isset($_GET['borrar'])) {
    $id = intval($_GET['borrar']);
    $res = pg_query_params($conn, "DELETE FROM especialidades WHERE id = $1", array($id));
    $mensaje = $res ? "Especialidad eliminada." : "Error al eliminar: " . pg_last_error($conn);
}

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $cocinero_id = intval($_POST['cocinero_id'] ?? 0);
    $plato_id = intval($_POST['plato_id'] ?? 0);

    $existe = pg_query_params(
        $conn,
        "SELECT 1 FROM especialidades WHERE cocinero_id = $1 AND plato_id = $2",
        array($cocinero_id, $plato_id)
    );

    if ($existe && pg_num_rows($existe) > 0) {
        $mensaje = "Ese plato ya esta asignado a ese cocinero.";
    } else {
        $res = pg_query_params(
            $conn,
            "INSERT INTO especialidades (cocinero_id, plato_id) VALUES ($1, $2)",
            array($cocinero_id, $plato_id)
        );
        $mensaje = $res ? "Especialidad asignada." : "Error al asignar: " . pg_last_error($conn);
    }
}

$cocineros = procesar_query("
    SELECT u.id, u.nombre
    FROM usuarios u
    JOIN actuaciones a ON a.usuario_id = u.id
    JOIN roles r ON r.id = a.rol_id
    WHERE r.nombre ILIKE 'Cocinero'
    ORDER BY u.nombre", $conn);

$platos = procesar_query("
    SELECT p.id, p.nombre, t.nombre AS tipo
    FROM platos p
    LEFT JOIN tipos t ON t.id = p.tipo_id
    ORDER BY t.id, p.nombre", $conn);

$especialidades = procesar_query("
    SELECT e.id,
           c.nombre AS cocinero,
           p.nombre AS plato,
           t.nombre AS tipo
    FROM especialidades e
    JOIN usuarios c ON c.id = e.cocinero_id
    JOIN platos p ON p.id = e.plato_id
    LEFT JOIN tipos t ON t.id = p.tipo_id
    ORDER BY c.nombre, t.id, p.nombre", $conn);
?>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Especialidades de Cocineros</title>
    <link rel="stylesheet" href="css/estilo.css">
    <style>
        .contenedor { display: grid; grid-template-columns: minmax(260px, 360px) 1fr; gap: 20px; align-items: start; }
        .panel { background: white; border: 1px solid #ccc; border-radius: 6px; padding: 16px; }
        form { display: grid; gap: 10px; }
        select, button { padding: 8px; }
        table { width: 100%; border-collapse: collapse; }
        th, td { border-bottom: 1px solid #ddd; padding: 9px; text-align: left; }
        th { background: #004488; color: white; }
        .alerta { padding: 10px; margin: 12px 0; border-radius: 4px; background: #d4edda; color: #155724; }
        .borrar { color: #b00020; text-decoration: none; font-size: 1em; padding: 6px 9px; }
        @media (max-width: 800px) { .contenedor { grid-template-columns: 1fr; } }
    </style>
</head>
<body>
    <header>ESPECIALIDADES DE COCINEROS</header>
    <main>
        <nav>
            <a href="admin.html">Volver al Panel Admin</a>
        </nav>

        <?php if ($mensaje): ?>
            <div class="alerta"><?= h($mensaje) ?></div>
        <?php endif; ?>

        <div class="contenedor">
            <section class="panel">
                <h2>Asignar plato</h2>
                <form method="POST">
                    <label>Cocinero</label>
                    <select name="cocinero_id" required>
                        <option value="" disabled selected>Seleccione cocinero</option>
                        <?php foreach ($cocineros->datos as $c): ?>
                            <option value="<?= h($c['id']) ?>"><?= h($c['nombre']) ?></option>
                        <?php endforeach; ?>
                    </select>

                    <label>Plato</label>
                    <select name="plato_id" required>
                        <option value="" disabled selected>Seleccione plato</option>
                        <?php foreach ($platos->datos as $p): ?>
                            <option value="<?= h($p['id']) ?>"><?= h($p['tipo']) ?> - <?= h($p['nombre']) ?></option>
                        <?php endforeach; ?>
                    </select>

                    <button type="submit">Asignar especialidad</button>
                </form>
            </section>

            <section class="panel">
                <h2>Asignaciones actuales</h2>
                <table>
                    <thead>
                        <tr>
                            <th>Cocinero</th>
                            <th>Tipo</th>
                            <th>Plato</th>
                            <th>Accion</th>
                        </tr>
                    </thead>
                    <tbody>
                        <?php if ($especialidades->cantidad == 0): ?>
                            <tr><td colspan="4">No hay especialidades registradas.</td></tr>
                        <?php else: ?>
                            <?php foreach ($especialidades->datos as $e): ?>
                                <tr>
                                    <td><?= h($e['cocinero']) ?></td>
                                    <td><?= h($e['tipo']) ?></td>
                                    <td><?= h($e['plato']) ?></td>
                                    <td>
                                        <a class="borrar" href="?borrar=<?= h($e['id']) ?>" onclick="return confirm('Eliminar esta especialidad?')">Eliminar</a>
                                    </td>
                                </tr>
                            <?php endforeach; ?>
                        <?php endif; ?>
                    </tbody>
                </table>
            </section>
        </div>
    </main>
</body>
</html>
