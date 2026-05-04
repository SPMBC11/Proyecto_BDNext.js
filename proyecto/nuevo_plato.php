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

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $tipo_id     = intval($_POST['tipo_id']);
    $nombre      = $_POST['nombre'];
    $descripcion = $_POST['descripcion'];
    $tiempo      = $_POST['tiempo']; // Formato intervalo: '15 min' o '00:15:00'
    $precio      = floatval($_POST['precio']);

    $sql = "INSERT INTO platos (tipo_id, nombre, descripcion, tiempo, precio) VALUES ($1, $2, $3, $4, $5)";
    $res = pg_query_params($conn, $sql, array($tipo_id, $nombre, $descripcion, $tiempo, $precio));

    if ($res) {
        header("Location: gestionar_menu.php");
        exit;
    } else {
        $mensaje = "<p style='color:red;'>Error al guardar: " . pg_last_error($conn) . "</p>";
    }
}

$contenido = "
    <h2>Añadir Nuevo Plato</h2>
    <form method='POST' style='display: grid; gap: 10px; max-width: 450px; background: #f4f4f4; padding: 20px; border-radius: 8px;'>
        <label>Categoría:</label>
        <select name='tipo_id' required>
            <option value='1'>Entrada</option>
            <option value='2'>Plato Fuerte</option>
            <option value='3'>Bebida</option>
        </select>

        <label>Nombre del Plato:</label>
        <input type='text' name='nombre' required placeholder='Ej: Ajiaco'>

        <label>Descripción:</label>
        <textarea name='descripcion' placeholder='Ingredientes o detalles...'></textarea>

        <label>Tiempo de preparación (ej: 20 min):</label>
        <input type='text' name='tiempo' required placeholder='00:20:00'>

        <label>Precio ($):</label>
        <input type='number' step='0.01' name='precio' required>

        <div style='margin-top:10px;'>
            <button type='submit' style='background: #007bff; color: white; padding: 10px; border: none; cursor: pointer;'>Guardar Plato</button>
            <button type='button' onclick='window.location.href=\"gestionar_menu.php\"' style='padding: 10px;'>Cancelar</button>
        </div>
    </form>
    $mensaje
";

$esqueleto = file_get_contents("editar_plato.html");
echo sprintf($esqueleto, $contenido);
?>
