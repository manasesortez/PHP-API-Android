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
import com.amtodev.bitcoinconvert.Clases.Configuraciones;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class modificar_contacto extends AppCompatActivity {
    EditText nombre;
    EditText telefono;
    Button botonAgregar, botonRegresar, botonEliminar, botonLlamar;
    int id_contacto = 0;
    String nombre_contacto, telefono_contacto;

    Configuraciones objConfiguracion = new Configuraciones();
    String URL = objConfiguracion.urlWebServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_contacto);

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
            ArrayList<String> milista = new ArrayList<String>();
            RequestQueue objetoPeticio = Volley.newRequestQueue(modificar_contacto.this);
            StringRequest peticion = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject objJSONResultado = new JSONObject(response.toString());
                        String estado = objJSONResultado.getString("estado");
                        if(estado.equals("1")){
                            Toast.makeText(modificar_contacto.this, "Contacto Registrado Con Exito", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(modificar_contacto.this, "Error" + estado, Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(modificar_contacto.this, "error" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("accion", "modificar");
                    params.put("id_contacto", String.valueOf(id_contacto));
                    params.put("nombre", nombre.getText().toString());
                    params.put("telefono", telefono.getText().toString());
                    return params;
                }
            };
            objetoPeticio.add(peticion);
        }catch (Exception error){
            Toast.makeText(modificar_contacto.this, "Error: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void eliminar(){
        try{
            ArrayList<String> milista = new ArrayList<String>();
            RequestQueue objetoPeticio = Volley.newRequestQueue(modificar_contacto.this);
            StringRequest peticion = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject objJSONResultado = new JSONObject(response.toString());
                        String estado = objJSONResultado.getString("estado");
                        if(estado.equals("1")){
                            Toast.makeText(modificar_contacto.this, "Contacto Eliminado Con Exito", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(modificar_contacto.this, "Error" + estado, Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(modificar_contacto.this, "error" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("accion", "modificar");
                    params.put("id_contacto", String.valueOf(id_contacto));
                    return params;
                }
            };
            objetoPeticio.add(peticion);
        }catch (Exception error){
            Toast.makeText(modificar_contacto.this, "Error: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle valoresAdicionales = getIntent().getExtras();
        if (valoresAdicionales == null){
            Toast.makeText(modificar_contacto.this, "Debes enviar el ID de contacto", Toast.LENGTH_SHORT).show();
            id_contacto = 0;
        }else{
            id_contacto = Integer.parseInt(valoresAdicionales.getString("id_contacto"));
            nombre_contacto = valoresAdicionales.getString("nombre");
            telefono_contacto = valoresAdicionales.getString("telefono");
            verContact();
        }
    }

    private void verContact(){
        nombre.setText(nombre_contacto);
        telefono.setText(telefono_contacto);
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