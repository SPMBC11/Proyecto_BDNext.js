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

$conn = pg_conectar($conn_string);

$mensaje = "";

if (!isset($_GET['id'])) {
    header("Location: gestionar_menu.php");
    exit;
}
$id = intval($_GET['id']);

// 1. Procesar la actualización
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $tipo_id     = intval($_POST['tipo_id']);
    $nombre      = $_POST['nombre'];
    $descripcion = $_POST['descripcion'];
    $tiempo      = $_POST['tiempo'];
    $precio      = floatval($_POST['precio']);

    $sql_upd = "UPDATE platos SET tipo_id=$1, nombre=$2, descripcion=$3, tiempo=$4, precio=$5 WHERE id=$6";
    $res = pg_query_params($conn, $sql_upd, array($tipo_id, $nombre, $descripcion, $tiempo, $precio, $id));

    if ($res) {
        header("Location: gestionar_menu.php");
        exit;
    } else {
        $mensaje = "<p style='color:red;'>Error: " . pg_last_error($conn) . "</p>";
    }
}

// 2. Obtener datos actuales del plato
$res_query = pg_query_params($conn, "SELECT * FROM platos WHERE id = $1", array($id));
$plato = pg_fetch_assoc($res_query);

if (!$plato) { die("Plato no encontrado."); }

// 3. Formulario de edición
$contenido = "
    <h2>Editar Plato #$id</h2>
    <form method='POST' style='display: grid; gap: 10px; max-width: 450px; background: #fffbe6; padding: 20px; border: 1px solid #ffe58f; border-radius: 8px;'>
        <label>Categoría:</label>
        <select name='tipo_id'>
            <option value='1' ".($plato['tipo_id']==1?'selected':'').">Entrada</option>
            <option value='2' ".($plato['tipo_id']==2?'selected':'').">Plato Fuerte</option>
            <option value='3' ".($plato['tipo_id']==3?'selected':'').">Bebida</option>
        </select>

        <label>Nombre:</label>
        <input type='text' name='nombre' value='".htmlspecialchars($plato['nombre'])."' required>

        <label>Descripción:</label>
        <textarea name='descripcion'>".htmlspecialchars($plato['descripcion'])."</textarea>

        <label>Tiempo (Intervalo):</label>
        <input type='text' name='tiempo' value='{$plato['tiempo']}' required>

        <label>Precio ($):</label>
        <input type='number' step='0.01' name='precio' value='{$plato['precio']}' required>

        <div style='margin-top:10px;'>
            <button type='submit' style='background: #28a745; color: white; padding: 10px; border: none;'>Actualizar</button>
            <button type='button' onclick='window.location.href=\"gestionar_menu.php\"' style='padding: 10px;'>Cancelar</button>
        </div>
    </form>
    $mensaje
";

$esqueleto = file_get_contents("editar_plato.html");
echo sprintf($esqueleto, $contenido);
?>
