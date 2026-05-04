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

// 1. Validar que exista el ID en la URL
if (!isset($_GET['id'])) {
    header("Location: gestion_mesas.php");
    exit;
}
$id = intval($_GET['id']);

// 2. Si se envían cambios (POST)
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $nuevas_sillas = intval($_POST['sillas']);
    
    $sql_update = "UPDATE mesas SET sillas = $1 WHERE id = $2";
    $res = pg_query_params($conn, $sql_update, array($nuevas_sillas, $id));
    
    if ($res) {
        header("Location: gestion_mesas.php");
        exit;
    } else {
        $mensaje = "<p style='color:red;'>Error: " . pg_last_error($conn) . "</p>";
    }
}

// 3. Consultar datos actuales para mostrar en el formulario
$res_datos = pg_query_params($conn, "SELECT sillas FROM mesas WHERE id = $1", array($id));
$fila = pg_fetch_assoc($res_datos);

if (!$fila) {
    die("La mesa con ID $id no existe.");
}

// 4. Generar el contenido
$contenido = "
    <h2>Modificar Mesa #$id</h2>
    <form method='POST' style='border: 1px solid #ccc; padding: 20px; border-radius: 10px; display: inline-block;'>
        <label>Número de Sillas:</label><br><br>
        <input type='number' name='sillas' value='{$fila['sillas']}' required min='1' style='padding: 5px;'><br><br>
        
        <button type='submit' style='background-color: #d4edda;'>Actualizar</button>
        <button type='button' onclick='window.location.href=\"gestion_mesas.php\"'>Cancelar</button>
    </form>
    $mensaje
";

// Renderizar
$esqueleto = file_get_contents("gestion_mesas.html");
echo sprintf($esqueleto, $contenido);
?>
