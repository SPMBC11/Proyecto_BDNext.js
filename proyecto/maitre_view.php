<?php
/**
 * maitre_view.php - Vista del Panel del Maitre
 */
$estados_label = [
    0 => ['texto' => 'Cancelada',  'clase' => 'estado-cancelada'],
    1 => ['texto' => 'Reservada',  'clase' => 'estado-reservada'],
    2 => ['texto' => 'Ocupada',    'clase' => 'estado-ocupada'],
    3 => ['texto' => 'Liberada',   'clase' => 'estado-liberada'],
    4 => ['texto' => 'Cancelada',  'clase' => 'estado-cancelada'],
];
?>
<!DOCTYPE html>
<html lang="es-CO">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel del Maitre</title>
    <link rel="stylesheet" href="css/estilo.css">
</head>
<body>

<div class="panel-wrapper">

    <div class="panel-header">
        <h1>PANEL DE MAITRE</h1>
        <a href="pagina_principal.php" class="btn-volver">Volver al Menu</a>
    </div>

    <div class="panel-body">

        <?php if($mensaje): ?>
            <div class="panel-alerta <?= (strpos($mensaje, 'Error') !== false) ? 'err' : 'ok' ?>">
                <?= h($mensaje) ?>
            </div>
        <?php endif; ?>

        <div class="panel-layout">

            <!-- COLUMNA IZQUIERDA: Formulario de reserva -->
            <div class="panel-col-1">
                <div class="panel-card">
                    <h3><?= $reserva_editar ? 'Modificar Reserva' : 'Nueva Reserva' ?></h3>
                    <form method="POST">
                        <input type="hidden" name="accion" value="guardar_reserva">
                        <input type="hidden" name="reservacion_id" value="<?= h($reserva_editar['res_id'] ?? '') ?>">

                        <div class="form-grupo">
                            <label>Cliente:</label>
                            <select name="cliente_id" required>
                                <?php foreach($clientes->datos as $c): ?>
                                    <?php $sel = ($reserva_editar && $reserva_editar['cliente_id'] == $c['id']) ? 'selected' : ''; ?>
                                    <option value="<?= h($c['id']) ?>" <?= $sel ?>><?= h($c['nombre']) ?></option>
                                <?php endforeach; ?>
                            </select>
                        </div>

                        <div class="form-grupo">
                            <label>Mesa:</label>
                            <select name="mesa_id" required>
                                <?php foreach($mesas->datos as $m): ?>
                                    <?php $sel = ($reserva_editar && $reserva_editar['mesa_id'] == $m['id']) ? 'selected' : ''; ?>
                                    <option value="<?= h($m['id']) ?>" <?= $sel ?>>Mesa <?= h($m['id']) ?> (<?= h($m['sillas']) ?> sillas)</option>
                                <?php endforeach; ?>
                            </select>
                        </div>

                        <?php
                            $fecha_valor = $reserva_editar ? date('Y-m-d', strtotime($reserva_editar['inicio'])) : '';
                            $hora_valor  = $reserva_editar ? date('H:i',   strtotime($reserva_editar['inicio'])) : '';
                        ?>

                        <div class="form-grupo">
                            <label>Personas:</label>
                            <input type="number" name="cantidad" placeholder="N° de personas"
                                   value="<?= h($reserva_editar['cantidad'] ?? '') ?>" min="1" required>
                        </div>

                        <div class="form-grupo">
                            <label>Fecha:</label>
                            <input type="date" name="fecha" value="<?= h($fecha_valor) ?>" required>
                        </div>

                        <div class="form-grupo">
                            <label>Hora:</label>
                            <input type="time" name="hora" value="<?= h($hora_valor) ?>" required>
                        </div>

                        <button type="submit" class="btn-primario">
                            <?= $reserva_editar ? 'Guardar Cambios' : 'Reservar' ?>
                        </button>
                        <?php if ($reserva_editar): ?>
                            <button type="button" onclick="location.href='gestionar_reservas.php'" class="btn-secundario">
                                Cancelar edicion
                            </button>
                        <?php endif; ?>
                    </form>
                </div>
            </div><!-- /col-1 -->

            <!-- COLUMNA DERECHA: Tabla de reservas -->
            <div class="panel-col-2">
                <div class="panel-card">
                    <h3>Estado de Reservas</h3>
                    <table class="panel-tabla">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Cliente</th>
                                <th>Pers.</th>
                                <th>Mesa</th>
                                <th>Inicio</th>
                                <th>Estado</th>
                                <th>Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <?php if (empty($listado->datos)): ?>
                                <tr><td colspan="7" class="vacio">No hay reservas registradas.</td></tr>
                            <?php else: ?>
                                <?php foreach($listado->datos as $l): ?>
                                <?php $est_info = $estados_label[$l['estado']] ?? ['texto' => 'Otro', 'clase' => '']; ?>
                                <tr>
                                    <td><?= h($l['res_id']) ?></td>
                                    <td><?= h($l['cliente']) ?></td>
                                    <td><strong><?= h($l['cantidad']) ?></strong></td>
                                    <td>Mesa <?= h($l['mesa_n']) ?></td>
                                    <td><?= h(date('d/m/Y H:i', strtotime($l['inicio']))) ?></td>
                                    <td>
                                        <span class="estado-badge <?= $est_info['clase'] ?>">
                                            <?= $est_info['texto'] ?>
                                        </span>
                                    </td>
                                    <td>
                                        <?php if($l['estado'] == 1): ?>
                                            <a href="?sentar=<?= h($l['res_id']) ?>"   class="btn-accion btn-sentar">SENTAR</a>
                                            <a href="?editar=<?= h($l['res_id']) ?>"   class="btn-accion btn-editar">EDITAR</a>
                                            <a href="?cancelar=<?= h($l['res_id']) ?>" class="btn-accion btn-cancelar"
                                               onclick="return confirm('Cancelar esta reservacion?')">CANCELAR</a>
                                        <?php elseif($l['estado'] == 2): ?>
                                            <a href="?liberar=<?= h($l['res_id']) ?>" class="btn-accion btn-liberar">LIBERAR</a>
                                        <?php else: ?>
                                            -
                                        <?php endif; ?>
                                    </td>
                                </tr>
                                <?php endforeach; ?>
                            <?php endif; ?>
                        </tbody>
                    </table>
                </div>
            </div><!-- /col-2 -->

        </div><!-- /panel-layout -->

    </div><!-- /panel-body -->

    <div class="panel-footer">
        <span>Panel Maitre</span>
        <span>Sistema Gastronomico v1.0</span>
    </div>

</div><!-- /panel-wrapper -->

</body>
</html>
