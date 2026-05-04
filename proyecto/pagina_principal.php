<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
session_start();
require_once(__DIR__ . "/etc/parametros.php");
require_once(__DIR__ . "/lib/libreria.php");
require_once(__DIR__ . "/lib/usuarios.php");

$conn = pg_conectar($conn_string);
$contenido = "";
$opcion = $_REQUEST['opcion'] ?? "";

if(!isset($_SESSION['id_usuario'])){
	switch($opcion){
		case 'registro':
			$contenido = fn_formulario_registro($conn);
			break;
		case'guardar_usuario':
			$contenido=fn_guardar_usuario($conn);
			break;
		case'validar_login':
			$contenido=fn_validar_login($conn);
			break;
		default:
			$contenido=fn_formulario_login();
			break;
	}
}else{
	switch($opcion){
		case 'salir':
			session_destroy();
			header("Location: pagina_principal.php");
			exit();
		default:
			$rol=$_SESSION['rol'];
			$contenido="<h1>Bienvenido, ". $_SESSION['nombre'] ."</h1>";
			$contenido .="<p>Tu cargo actual es: <strong>$rol</strong></p>";
			$contenido .="<div class='MENU_OPCIONES'>";
			
			
			if($rol == 'Administrador'){
				$contenido .="<button onclick=\"window.location.href='admin.php'\">Admin</button>";

			}elseif($rol == 'Maitre'){
				$contenido .="<button onclick=\"window.location.href='gestionar_reservas.php'\">Maitre</button>";
			}elseif($rol == "Mesero"){
				$contenido .="<button onclick=\"window.location.href='gestion_mesero.php'\">Mesero</button>";
			}elseif($rol == "Cocinero"){
				$contenido .="<button onclick=\"window.location.href='gestion_cocina.php'\">Cocinero</button>";
				}
			
			$contenido .= "</div>";
			$contenido .= "<br><a href='?opcion=salir' class='pESTAÑA'>Cerrar Sesión</a>";
	}
}

$esqueleto = file_get_contents(__DIR__ . "/main_esqueleto.html");
print sprintf($esqueleto, $contenido);
?>
