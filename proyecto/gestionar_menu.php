session_start();
if (!isset($_SESSION['id_usuario'])) {
    header("Location: pagina_principal.php");
    exit();
}

$roles_permitidos = ['Administrador'];
if (!in_array($_SESSION['rol'], $roles_permitidos)) {
    header("Location: pagina_principal.php");
    exit();
}

require_once("etc/parametros.php");
require_once("lib/libreria.php");
require_once("lib/restaurante.php");

//------------------------------------------------------------
$conn = pg_conectar($conn_string);
$contenido = "";


// 1. Lógica para Borrar un plato
if (isset($_GET['borrar'])) {
    $id_borrar = intval($_GET['borrar']);
    pg_query_params($conn, "DELETE FROM platos WHERE id = $1", array($id_borrar));
    header("Location: gestionar_menu.php");
    exit;
}

// 2. Consulta con JOIN para traer el nombre del tipo de plato
$sentencia = "
    SELECT platos.id, 
           platos.nombre, 
           platos.descripcion, 
           platos.tiempo, 
           platos.precio, 
           tipos.nombre AS nombre_tipo
    FROM platos
    LEFT JOIN tipos ON platos.tipo_id = tipos.id
    ORDER BY tipos.id, platos.nombre;
";

$resultado = procesar_query($sentencia, $conn);

// 3. Generar el HTML agrupado
if ($resultado->cantidad == 0) {
    $contenido = "<p>No hay platos registrados. <a href='nuevo_plato.php'>Agregar el primero</a></p>";
} else {
    $tipo_actual = "";
    
    foreach ($resultado->datos as $plato) {
        // Si el tipo cambia (ej. de Entrada a Bebida), creamos un nuevo encabezado
        if ($tipo_actual != $plato['nombre_tipo']) {
            $tipo_actual = $plato['nombre_tipo'];
            $contenido .= "<h2 style='background: #333; color: white; padding: 10px; margin-top: 20px;'>$tipo_actual</h2>";
        }

        // Diseño de cada fila de plato
        $contenido .= "
        <div style='border: 1px solid #ccc; margin: 5px 0; padding: 10px; display: flex; justify-content: space-between; align-items: center;'>
            <div style='flex-grow: 1;'>
                <strong style='font-size: 1.2em;'>{$plato['nombre']}</strong> 
                <span style='color: #666; margin-left: 10px;'>({$plato['tiempo']})</span><br>
                <small>{$plato['descripcion']}</small><br>
                <span style='color: green; font-weight: bold;'>$ " . number_format($plato['precio'], 2) . "</span>
            </div>
            <div style='min-width: 150px; text-align: right;'>
                <a href='editar_plato.php?id={$plato['id']}' style='text-decoration: none;'>
                    <button style='background: #ffc107; border: none; padding: 5px 10px; cursor: pointer;'>Editar</button>
                </a>
                <a href='?borrar={$plato['id']}' onclick='return confirm(\"¿Eliminar {$plato['nombre']}?\")' style='text-decoration: none;'>
                    <button style='background: #dc3545; color: white; border: none; padding: 5px 10px; cursor: pointer;'>Borrar</button>
                </a>
            </div>
        </div>";
    }
}

//------------------------------------------------------------
$esqueleto = file_get_contents("gestionar_menu.html");
$html = sprintf($esqueleto, $contenido);
print $html;

//------------------------------------------------------------

?>
