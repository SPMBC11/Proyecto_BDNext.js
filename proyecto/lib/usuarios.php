<?php
function fn_formulario_login(){
	return '
	<h2>Inicio de Sesión</h2>
	<form action="?opcion=validar_login" method="POST">
		<div class="PLATO"><span class="NOMBRE">Nombre:</span><input type="text" name="txt_user" required></div>
		<div class="PLATO"><span class="NOMBRE">Clave:</span><input type="password" name="txt_pass" required></div>
		<input type ="submit" value="Entrar">
	</form>
	<br><a href ="?opcion=registro">¿No tienes cuenta? Registrate aqui</a>';
	
}

function fn_formulario_registro($conn){
	$res = pg_query($conn, "SELECT * FROM Roles");
	$opciones ="";
	while($r = pg_fetch_assoc($res)){
		$opciones .= "<option value='".h($r['id'])."'>".h($r['nombre'])."</option>";
	}
	return '
	<h2>Registro de Usuario</h2>
	<form action ="?opcion=guardar_usuario" method="POST">
	<div class="PLATO"><span class="NOMBRE">Nombre:</span><input type="text" name="txt_nombre" required></div>
	<div class="PLATO"><span class="NOMBRE">Clave:</span><input type="password" name="txt_pass" required></div>
	<div class="PLATO"><span class="NOMBRE">Rol:</span><select name="sel_rol">'.$opciones.'</select></div>
	<input type="submit" value="Crear Cuenta">
	</form>';
}

function fn_guardar_usuario($conn){
    $nombre = trim($_POST['txt_nombre']);
    $pass_hash = hash('sha256', $_POST['txt_pass']);
    $rol_id = intval($_POST['sel_rol']);

    $existe = pg_query_params($conn, "SELECT 1 FROM usuarios WHERE lower(nombre) = lower($1)", array($nombre));
    if ($nombre == "" || ($existe && pg_num_rows($existe) > 0)) {
        return "Nombre invalido o ya registrado.";
    }

    pg_query($conn, "BEGIN");
    $sql = "INSERT INTO Usuarios (nombre, clave, fecha_clave)
            VALUES ($1, $2, NOW()) RETURNING id";

    $res = pg_query_params($conn, $sql, array($nombre, $pass_hash));
    if($res){
        $id_nuevo = pg_fetch_result($res, 0, 0);
        $res_act = pg_query_params($conn, "INSERT INTO Actuaciones (rol_id, usuario_id) VALUES ($1, $2)", array($rol_id, $id_nuevo));
        if ($res_act) {
            pg_query($conn, "COMMIT");
            return "Usuario registrado. <a href='pagina_principal.php'>Iniciar Sesion</a>";
        }
    }
    pg_query($conn, "ROLLBACK");
    return "Error: " . pg_last_error($conn);
}

function fn_validar_login($conn){
    $user = $_POST['txt_user'];
    $pass = $_POST['txt_pass'];
    
    $sql = "SELECT U.id, U.nombre, U.clave, R.nombre AS rol_nombre
            FROM Usuarios U
            JOIN Actuaciones A ON U.id = A.usuario_id
            JOIN Roles R ON A.rol_id = R.id
            WHERE U.nombre = $1";
    
    $res = pg_query_params($conn, $sql, array($user));

    if($res && $fila = pg_fetch_assoc($res)){
        $pass_hash = hash('sha256', $pass);
        
        if($pass_hash === $fila['clave']){
            $_SESSION['id_usuario'] = $fila['id'];
            $_SESSION['nombre'] = $fila['nombre'];
            $_SESSION['rol'] = $fila['rol_nombre'];
            header("Location: pagina_principal.php");
            exit();
        }
    }
    return "<p style='color:red'>Usuario o contraseña incorrectos.</p>" . fn_formulario_login();
}

?>
