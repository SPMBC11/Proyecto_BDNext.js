<?php
//------------------------------------------------------------
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

require_once("etc/parametros.php");
require_once("lib/libreria.php");
require_once("lib/restaurante.php");

//------------------------------------------------------------
$conn = pg_conectar($host, $dbname, $user, $password);
$contenido = "";

if(isset($_GET['borrar'])){
	$id_a_borrar = intval($_GET['borrar']);
	// Eliminar la mesa
    	pg_query($conn, "DELETE FROM mesas WHERE id = $id_a_borrar");
    	
    	// REAJUSTE DE IDs (Opcional: Compacta los números si hay huecos)
    	$sql_reajuste = "
        	BEGIN;
        	CREATE TEMP TABLE temp_mesas AS SELECT * FROM mesas ORDER BY id;
        	TRUNCATE TABLE mesas RESTART IDENTITY CASCADE;
        	INSERT INTO mesas (sillas) SELECT sillas FROM temp_mesas;
        	COMMIT;
    	";
    	pg_query($conn, $sql_reajuste);
    	// Redirigir para limpiar la URL y refrescar la lista
    	header("Location: gestion_mesas.php");
    	exit;
}

// 3. Consultar las mesas existentes
$sql = "SELECT id, sillas FROM mesas ORDER BY id";
$res = pg_query($conn, $sql);

if (!$res) {
    $contenido = "Error en la consulta: " . pg_last_error($conn);
} else {
    // 4. Construcción de la tabla HTML
    $contenido = "<table border='1' style='width:100%; border-collapse: collapse; text-align: center;'>
                    <thead>
                        <tr style='background-color: #eee;'>
                            <th>ID de Mesa</th>
                            <th>Número de Sillas</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>";

    while ($fila = pg_fetch_assoc($res)) {
        $id = $fila['id'];
        $sillas = $fila['sillas'];

        $contenido .= "<tr>
            <td>{$id}</td>
            <td>{$sillas} sillas</td>
            <td>
                <a href='editar_mesa.php?id={$id}' style='margin-right:10px;'>[Modificar]</a>
                <a href='?borrar={$id}' onclick='return confirm(\"¿Eliminar mesa {$id}?\")' style='color:red;'>[Borrar]</a>
            </td>
        </tr>";
    }
    
    if (pg_num_rows($res) == 0) {
        $contenido .= "<tr><td colspan='3'>No hay mesas registradas</td></tr>";
    }

    $contenido .= "</tbody></table>";
    
}//------------------------------------------------------------
$esqueleto = file_get_contents("gestion_mesas.html");
$html = sprintf($esqueleto, $contenido);
print $html;

//------------------------------------------------------------

?>
