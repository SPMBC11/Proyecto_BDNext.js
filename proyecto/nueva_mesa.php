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

// Si el formulario fue enviado
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $sillas = intval($_POST['sillas']);
    
    // Insertamos solo las sillas (el ID se genera solo si es SERIAL)
    $sql = "INSERT INTO mesas (sillas) VALUES ($1)";
    $res = pg_query_params($conn, $sql, array($sillas));
    
    if ($res) {
        header("Location: gestion_mesas.php");
        exit;
    } else {
        $mensaje = "<p style='color:red;'>Error al guardar: " . pg_last_error($conn) . "</p>";
    }
}

// Generamos el formulario para el %s del HTML
$contenido = "
    <h2>Registrar Nueva Mesa</h2>
    <form method='POST' style='border: 1px solid #ccc; padding: 20px; border-radius: 10px; display: inline-block;'>
        <label for='sillas'>Cantidad de Sillas:</label><br><br>
        <input type='number' name='sillas' id='sillas' required min='1' style='padding: 5px;'><br><br>
        
        <button type='submit'>Guardar Mesa</button>
        <button type='button' onclick='window.location.href=\"gestion_mesas.php\"'>Cancelar</button>
    </form>
    $mensaje
";

// Renderizar en el esqueleto
$esqueleto = file_get_contents("gestion_mesas.html");
echo sprintf($esqueleto, $contenido);
?>
