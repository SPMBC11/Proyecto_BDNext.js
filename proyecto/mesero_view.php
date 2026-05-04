<?php
/**
 * mesero_view.php - Vista del Panel Mesero
 */
?>
<!DOCTYPE html>
<html lang="es-CO">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel Mesero</title>
    <link rel="stylesheet" href="css/estilo.css">
    <style>
        /* Tabla de platos a pedir */
        #tabla-platos { width: 100%; border-collapse: collapse; margin-bottom: 10px; }
        #tabla-platos th { background: navy; color: white; padding: 8px 10px; text-align: left; font-size: 0.9em; }
        #tabla-platos td { padding: 6px 8px; border-bottom: 1px solid #dde; vertical-align: middle; }
        #tabla-platos select, #tabla-platos input[type=number] {
            padding: 5px 7px; border: 1px solid #b0bcd0; border-radius: 4px;
            font-size: 0.9em; background: white; color: #333;
        }
        #tabla-platos select { width: 100%; }
        #tabla-platos input[type=number] { width: 60px; }
        .btn-agregar-fila {
            background: #eef2ff; color: navy; border: 1px solid #b0bcd0;
            padding: 6px 14px; border-radius: 4px; cursor: pointer;
            font-size: 0.88em; margin-right: 8px;
        }
        .btn-agregar-fila:hover { background: #d8e0ff; }
        .btn-quitar-fila {
            background: none; border: none; color: #dc3545;
            cursor: pointer; font-size: 1.1em; padding: 0 4px;
        }
    </style>
</head>
<body>

<div class="panel-wrapper">

    <div class="panel-header">
        <h1>PANEL MESERO</h1>
        <a href="pagina_principal.php" class="btn-volver">Volver al Menu</a>
    </div>

    <div class="panel-body">

        <?php if($mensaje): ?>
            <div class="panel-alerta ok"><?= h($mensaje) ?></div>
        <?php endif; ?>

        <!-- TOMAR NUEVO PEDIDO -->
        <div class="panel-card">
            <h3>Tomar Nuevo Pedido</h3>

            <?php $hay_mesas = isset($mesas_activas->datos) && count($mesas_activas->datos) > 0; ?>

            <?php if (!$hay_mesas): ?>
                <div class="panel-alerta warn">
                    No hay mesas ocupadas actualmente. El Maitre debe sentar clientes primero.
                </div>
            <?php else: ?>
                <form method="POST" id="form-pedido">
                    <input type="hidden" name="nuevo_pedido" value="1">

                    <div class="form-grupo" style="max-width:360px;">
                        <label>Mesa Ocupada:</label>
                        <select name="cliente_id" required>
                            <option value="" disabled selected>Seleccione mesa...</option>
                            <?php foreach($mesas_activas->datos as $m): ?>
                                <option value="<?= h($m['cliente_id']) ?>">
                                    Mesa <?= h($m['mesa_id']) ?> - <?= h($m['cliente_nom']) ?>
                                </option>
                            <?php endforeach; ?>
                        </select>
                    </div>

                    <!-- Tabla de platos a agregar -->
                    <table id="tabla-platos">
                        <thead>
                            <tr>
                                <th>Plato</th>
                                <th style="width:80px;">Cant.</th>
                                <th style="width:36px;"></th>
                            </tr>
                        </thead>
                        <tbody id="filas-platos">
                            <!-- Primera fila por defecto -->
                            <tr>
                                <td>
                                    <select name="plato_id[]" required>
                                        <?php foreach($platos->datos as $p): ?>
                                            <option value="<?= h($p['id']) ?>">
                                                <?= h($p['nombre']) ?> ($<?= h($p['precio']) ?>)
                                            </option>
                                        <?php endforeach; ?>
                                    </select>
                                </td>
                                <td><input type="number" name="cantidad[]" value="1" min="1"></td>
                                <td></td>
                            </tr>
                        </tbody>
                    </table>

                    <button type="button" class="btn-agregar-fila" onclick="agregarFila()">
                        + Agregar otro plato
                    </button>

                    <button type="submit" class="btn-enviar" style="margin-top:12px;">
                        Enviar a Cocina
                    </button>
                </form>

                <script>
                // Opciones de platos listas para clonar
                var opcionesPlatos = <?php
                    $opts = [];
                    foreach ($platos->datos as $p) {
                        $opts[] = ['id' => $p['id'], 'texto' => $p['nombre'] . ' ($' . $p['precio'] . ')'];
                    }
                    echo json_encode($opts);
                ?>;

                function agregarFila() {
                    var tbody = document.getElementById('filas-platos');
                    var tr = document.createElement('tr');

                    // Celda plato
                    var tdPlato = document.createElement('td');
                    var sel = document.createElement('select');
                    sel.name = 'plato_id[]';
                    opcionesPlatos.forEach(function(o) {
                        var opt = document.createElement('option');
                        opt.value = o.id;
                        opt.textContent = o.texto;
                        sel.appendChild(opt);
                    });
                    tdPlato.appendChild(sel);

                    // Celda cantidad
                    var tdCant = document.createElement('td');
                    var inp = document.createElement('input');
                    inp.type = 'number';
                    inp.name = 'cantidad[]';
                    inp.value = 1;
                    inp.min = 1;
                    tdCant.appendChild(inp);

                    // Celda quitar
                    var tdQuitar = document.createElement('td');
                    var btn = document.createElement('button');
                    btn.type = 'button';
                    btn.className = 'btn-quitar-fila';
                    btn.textContent = 'x';
                    btn.onclick = function() { tbody.removeChild(tr); };
                    tdQuitar.appendChild(btn);

                    tr.appendChild(tdPlato);
                    tr.appendChild(tdCant);
                    tr.appendChild(tdQuitar);
                    tbody.appendChild(tr);
                }
                </script>
            <?php endif; ?>
        </div>

        <!-- PLATOS LISTOS PARA ENTREGAR -->
        <?php $n_listos = isset($ordenes_listas->datos) ? count($ordenes_listas->datos) : 0; ?>
        <div class="panel-card listo">
            <h3>Platos Listos para Entregar (<?= $n_listos ?>)</h3>
            <table class="panel-tabla">
                <thead>
                    <tr>
                        <th>Pedido</th>
                        <th>Cliente</th>
                        <th>Plato</th>
                        <th>Cant.</th>
                        <th>Accion</th>
                    </tr>
                </thead>
                <tbody>
                    <?php if ($n_listos > 0): ?>
                        <?php foreach($ordenes_listas->datos as $o): ?>
                        <tr>
                            <td>#<?= h($o['pedido_n']) ?></td>
                            <td><?= h($o['cliente']) ?></td>
                            <td><?= h($o['plato']) ?></td>
                            <td><?= h($o['cantidad']) ?></td>
                            <td>
                                <a href="?cambiar_estado=1&orden_id=<?= h($o['id']) ?>"
                                   class="btn-accion btn-entregar">
                                    Marcar Entregado
                                </a>
                            </td>
                        </tr>
                        <?php endforeach; ?>
                    <?php else: ?>
                        <tr><td colspan="5" class="vacio">No hay platos listos para entregar.</td></tr>
                    <?php endif; ?>
                </tbody>
            </table>
        </div>

        <!-- PEDIDOS EN COCINA -->
        <?php $n_pendientes = isset($ordenes_pendientes->datos) ? count($ordenes_pendientes->datos) : 0; ?>
        <div class="panel-card cocina">
            <h3>Pedidos en Cocina (<?= $n_pendientes ?>)</h3>
            <table class="panel-tabla">
                <thead>
                    <tr>
                        <th>Pedido</th>
                        <th>Cliente</th>
                        <th>Plato</th>
                        <th>Cant.</th>
                        <th>Estado</th>
                    </tr>
                </thead>
                <tbody>
                    <?php if ($n_pendientes > 0): ?>
                        <?php foreach($ordenes_pendientes->datos as $o): ?>
                        <tr>
                            <td>#<?= h($o['pedido_n']) ?></td>
                            <td><?= h($o['cliente']) ?></td>
                            <td><?= h($o['plato']) ?></td>
                            <td><?= h($o['cantidad']) ?></td>
                            <td><span class="status-1">Solicitado</span></td>
                        </tr>
                        <?php endforeach; ?>
                    <?php else: ?>
                        <tr><td colspan="5" class="vacio">No hay ordenes pendientes en cocina.</td></tr>
                    <?php endif; ?>
                </tbody>
            </table>
        </div>

    </div><!-- /panel-body -->

    <div class="panel-footer">
        <span>Panel Mesero</span>
        <span>Sistema Gastronomico v1.0</span>
    </div>

</div><!-- /panel-wrapper -->

</body>
</html>
