package com.amtodev.bitcoinconvert;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Add_Contact extends AppCompatActivity {
    final String NOMBRE_BASE_DE_DATOS = "miagenda";
    EditText nombre, telefono;
    Button botonAgregar, botonRegresar;
    Configuraciones objConfiguracion = new Configuraciones();
    String URL = objConfiguracion.urlWebServices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

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

    @SuppressLint("NewApi")
    private void registrar(){
        try{
            ArrayList<String> milista = new ArrayList<String>();
            RequestQueue objetoPeticio = Volley.newRequestQueue(Add_Contact.this);
            StringRequest peticion = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject objJSONResultado = new JSONObject(response.toString());
                        String estado = objJSONResultado.getString("estado");
                        if(estado.equals("1")){
                            Toast.makeText(Add_Contact.this, "Contacto Registrado Con Exito", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Add_Contact.this, "Error" + estado, Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Add_Contact.this, "error" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                protected Map<String, String> getParams() throws AuthFailureError{
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("accion", "registrar");
                    params.put("nombre", nombre.getText().toString());
                    params.put("telefono", telefono.getText().toString());
                    return params;
                }
            };
            objetoPeticio.add(peticion);
        }catch (Exception error){
            Toast.makeText(Add_Contact.this, "Error en: " + error.getMessage() , Toast.LENGTH_SHORT).show();
        }
    }

    private void regresar(){
        Intent actividad = new Intent(Add_Contact.this, Contact.class);
        startActivity(actividad);
        Add_Contact.this.finish();
    }
}