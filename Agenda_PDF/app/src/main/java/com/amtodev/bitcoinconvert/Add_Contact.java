package com.amtodev.bitcoinconvert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amtodev.bitcoinconvert.Clases.ConexionSQLite;

public class Add_Contact extends AppCompatActivity {
    ConexionSQLite objConexion;
    final String NOMBRE_BASE_DE_DATOS = "miagenda";
    EditText nombre, telefono;
    Button botonAgregar, botonRegresar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        objConexion = new ConexionSQLite(Add_Contact.this, NOMBRE_BASE_DE_DATOS, null, 1);

        nombre = (EditText) findViewById(R.id.txtNombreCompleto);
        telefono = (EditText) findViewById(R.id.txtTelefono);
        botonAgregar = (Button) findViewById(R.id.btnGuardarContacto);
        botonRegresar = (Button) findViewById(R.id.btnRegresar);

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
                    Toast.makeText(Add_Contact.this, "No deje ningun campo Vacio",
                            Toast.LENGTH_LONG).show();
                }else if (telefono.getText().toString().isEmpty()){
                    Toast.makeText(Add_Contact.this, "No deje ningun campo Vacio",
                            Toast.LENGTH_LONG).show();
                }else {
                    registrar();
                    nombre.getText().clear();
                    telefono.getText().clear();

                }
            }
        });
    }

    private void registrar(){
        try{
            SQLiteDatabase miBaseDatos = objConexion.getWritableDatabase();
            String comando = "INSERT INTO contactos (nombre, telefono) VALUES" +
                    "('" + nombre.getText() + "','"+ telefono.getText() + "')";
            miBaseDatos.execSQL(comando);
            miBaseDatos.close();
            Toast.makeText(Add_Contact.this, "Contacto "+ nombre.getText().toString() +" registrado con exito", Toast.LENGTH_LONG).show();
        }catch(Exception error){
            Toast.makeText(Add_Contact.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void regresar(){
        Intent actividad = new Intent(Add_Contact.this, Contact.class);
        startActivity(actividad);
        Add_Contact.this.finish();
    }
}