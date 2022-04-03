package com.amtodev.bitcoinconvert;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amtodev.bitcoinconvert.Clases.ConexionSQLite;

public class modificar_contacto extends AppCompatActivity {
    ConexionSQLite objConexion;
    final String NOMBRE_BASE_DATOS = "miagenda";
    EditText nombre, telefono;
    Button botonAgregar, botonRegresar, botonEliminar, botonLlamar;
    int id_contacto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_contacto);

        objConexion = new ConexionSQLite(modificar_contacto.this, NOMBRE_BASE_DATOS, null, 1);
        nombre = (EditText) findViewById(R.id.txtNombreCompletoEditar);
        telefono = (EditText) findViewById(R.id.txtTelefonoEditar);

        botonAgregar = (Button) findViewById(R.id.btnGuardarContactoEditar);
        botonRegresar = (Button) findViewById(R.id.btnRegresarEditar);
        botonEliminar = (Button) findViewById(R.id.btnEliminarEditar);
        botonLlamar = (Button) findViewById(R.id.btnllamar);

        botonLlamar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (nombre.getText().toString().isEmpty()){
                    Toast.makeText(modificar_contacto.this, "No deje ningun campo Vacio",
                            Toast.LENGTH_LONG).show();
                }else if (telefono.getText().toString().isEmpty()){
                    Toast.makeText(modificar_contacto.this, "No deje ningun campo Vacio",
                            Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(modificar_contacto.this, "Llamada Realizada Exitosamente al Numero: " + telefono.getText().toString() + " Contacto: " + nombre.getText().toString(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        botonRegresar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                regresar();
            }
        });
        botonAgregar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (nombre.getText().toString().isEmpty()){
                    Toast.makeText(modificar_contacto.this, "No deje ningun campo Vacio",
                            Toast.LENGTH_LONG).show();
                }else if (telefono.getText().toString().isEmpty()){
                    Toast.makeText(modificar_contacto.this, "No deje ningun campo Vacio",
                            Toast.LENGTH_LONG).show();
                }else {
                    modificar();
                    nombre.getText().clear();
                    telefono.getText().clear();

                }
            }
        });
        botonEliminar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
    }

    private void modificar(){
        try{
            SQLiteDatabase miBaseDatos = objConexion.getWritableDatabase();
            String comando = "UPDATE contactos SET nombre='"+ nombre.getText() + "'," + "telefono='"+ telefono.getText() + "' WHERE id_contacto= '" + id_contacto + "'";
            miBaseDatos.execSQL(comando);
            miBaseDatos.close();
            Toast.makeText(modificar_contacto.this, "Datos Modificados con Exito", Toast.LENGTH_SHORT).show();
        }catch (Exception error){
            Toast.makeText(modificar_contacto.this, "Error: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void eliminar(){
        try{
            SQLiteDatabase miBaseDatos = objConexion.getWritableDatabase();
            String comando = "DELETE FROM contactos WHERE id_contacto='"+ id_contacto + "'";
            miBaseDatos.execSQL(comando);
            miBaseDatos.close();
            Toast.makeText(modificar_contacto.this, "Datos ELiminados con Exito", Toast.LENGTH_SHORT).show();
        }catch (Exception error){
            Toast.makeText(modificar_contacto.this, "Error: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void regresar(){
        Intent actividad = new Intent(modificar_contacto.this, Contact.class);
        startActivity(actividad);
        modificar_contacto.this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle valoresAdicionales = getIntent().getExtras();
        if (valoresAdicionales == null){
            Toast.makeText(modificar_contacto.this, "Debes enviar el ID de contacto", Toast.LENGTH_SHORT).show();
            id_contacto = 0;
            regresar();
        }else{
            id_contacto = valoresAdicionales.getInt("id_contacto");
            verContacto();
        }
    }

    private void verContacto(){
        try{
            SQLiteDatabase base = objConexion.getReadableDatabase();
            String consulta = "SELECT id_contacto,nombre,telefono from contactos " + " WHERE id_contacto ='"+ id_contacto +"'";
            @SuppressLint("Recycle") Cursor cadaRegistro = base.rawQuery(consulta, null);
            if (cadaRegistro.moveToFirst()){
                do{
                   nombre.setText(cadaRegistro.getString(1));
                   telefono.setText(cadaRegistro.getString(2));
                }while(cadaRegistro.moveToNext());
            }
        }catch (Exception error){
            Toast.makeText(modificar_contacto.this, "Error: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void openDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(modificar_contacto.this);
        builder.setTitle("Confirmar");
        builder.setMessage("¿Deseas eliminar este Contacto?");
        builder.setPositiveButton("Si, eliminar", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                eliminar();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(modificar_contacto.this, "Datos no Eliminados", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create();
        builder.show();
    }
}