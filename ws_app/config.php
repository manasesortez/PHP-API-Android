<?php

global $idConexion;

function conectar(){
    //credenciales de xampp
    $server = "127.0.0.1";
    $username = "root";
    $password = "";
    $bd = "miagenda";


    $idConexion = true;

    try{
        $idConexion = mysqli_connect($server, $username, $password, $bd);

        mysqli_set_charset ($idConexion, 'utf8');
        // ======= chequear la conexion ========
        if (!$idConexion) {
            $data = array("error" => 'Ya no se armo la conexion Dude'); //Esto es solo mensaje de error personalizado.
            die(json_encode($data));
        }

    }catch(Exception $e){
        mysqli_error($idConexion);
    }
    return $idConexion;

}

function desconectar($idConexion){
    try{
        mysqli_close($idConexion);
        $estado = 1;
    }catch(Exception $e){
        $estado = 0;
    }
    return $estado;
}

function agregarContacto($nombre,$telefono){
    $idConexion = conectar();
    $sql = "INSERT INTO contactos (nombre, telefono) VALUES ('$nombre', '$telefono')";
    if(mysqli_query($idConexion, $sql)){
        $estado = 1;
    }else{
        $estado ="Error: " . mysqli_error($idConexion);
    }
    desconectar($idConexion);
    return $estado;
}

function listarContacto($filtro){
    $idConexion = conectar();
    $datosFila = array();
    $consulta = "SELECT id_contacto, nombre, telefono FROM contactos WHERE (nombre LIKE '%$filtro%' OR  telefono LIKE '%$filtro') ORDER BY nombre ASC";
    $query = mysqli_query($idConexion, $consulta);
    $nfilas = mysqli_num_rows($query);
    if($nfilas != 0){
        while($aDatos = mysqli_fetch_array($query)){
            $jsonfila = array();

            $id_contacto = $aDatos["id_contacto"];
            $nombre = $aDatos["nombre"];
            $telefono = $aDatos["telefono"];

            $jsonfila["id_contacto"] = $id_contacto;
            $jsonfila["nombre"] = $nombre;
            $jsonfila["telefono"] = $telefono;

            $datosFila[] = $jsonfila;
        }
    }
    desconectar($idConexion);
    return array_values($datosFila);
}

function modificarContacto($id_contacto, $nombre,$telefono){
    $idConexion = conectar();
    $sql = "UPDATE contactos SET nombre='$nombre', telefono='$telefono' WHERE id_contacto = '$id_contacto";

    
    if(mysqli_query($idConexion, $sql)){
        $estado = 1;
    }else{
        $estado ="Error: " . mysqli_error($idConexion);
    }
    desconectar($idConexion);
    return $estado;
}

function eliminarContacto($id_contacto){
    $idConexion = conectar();
    $sql = "DELETE FROM contactos WHERE id_contacto = '$id_contacto'";
    if(mysqli_query($idConexion, $sql)){
        $estado = 1;
    }else{
        $estado ="Error: " . mysqli_error($idConexion);
    }
    desconectar($idConexion);
    return $estado;
}



?>