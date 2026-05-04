<?php
/**
 * cocina_view.php - Vista del Panel de Cocina
 * Muestra las comandas pendientes ordenadas por tiempo de preparacion.
 */
?>
<!DOCTYPE html>
<html lang="es-CO">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel de Cocina</title>
    <link rel="stylesheet" href="css/estilo.css">
</head>
<body>

<div class="panel-wrapper">

    <div class="panel-header">
        <h1>PANEL DE COCINA</h1>
        <a href="pagina_principal.php" class="btn-volver">Volver al Menu</a>
    </div>

    <div class="panel-body">

        <?php if (!empty($mensaje)): ?>
            <div class="panel-alerta warn"><?= h($mensaje) ?></div>
        <?php endif; ?>

        <div class="panel-card" style="background:#fff8e1; border-left:6px solid #ffc107; margin-bottom:18px; padding:10px 16px;">
            COMANDAS PENDIENTES - Ordenadas por tiempo de preparacion
        </div>

        <div class="cocina-grid">
            <?php
            if (!isset($ordenes_pendientes->datos) || count($ordenes_pendientes->datos) == 0):
            ?>
                <div class="comanda-vacia">
                    NO HAY PEDIDOS PENDIENTES PARA SUS ESPECIALIDADES.
                </div>
            <?php else: ?>
                <?php foreach($ordenes_pendientes->datos as $o): ?>
                    <div class="comanda-card">
                        <div class="comanda-plato">
                            <?= h($o['cantidad']) ?>x <?= h($o['plato']) ?>
                        </div>

                        <div class="comanda-tiempo">
                            Prep: <?= h($o['tiempo']) ?>
                        </div>

                        <div class="comanda-meta">
                            <strong>Orden #<?= h($o['orden_id']) ?></strong><br>
                            Solicitado: <?= h(date('H:i:s', strtotime($o['solicitado']))) ?>
                        </div>

                        <a href="?listo=<?= h($o['orden_id']) ?>" class="btn-accion btn-listo">
                            MARCAR COMO LISTO
                        </a>
                    </div>
                <?php endforeach; ?>
            <?php endif; ?>
        </div>

    </div><!-- /panel-body -->

    <div class="panel-footer">
        <span>Panel Cocina</span>
        <span>Sistema Gastronomico v1.0</span>
    </div>

</div><!-- /panel-wrapper -->

</body>
</html>
