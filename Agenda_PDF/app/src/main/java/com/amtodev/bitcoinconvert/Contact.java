package com.amtodev.bitcoinconvert;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ActionMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.amtodev.bitcoinconvert.Clases.ConexionSQLite;
import com.amtodev.bitcoinconvert.Clases.Configuraciones;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Contact extends AppCompatActivity {

    ConexionSQLite objConexion;
    final String NOMBRE_BASE_DATOS = "miagenda";
    Button botonAgregar, botonBuscar, botonExportar;
    EditText cajaBusquedaNombre;
    ListView listaContactos;
    Configuraciones objConfiguracion;
    ArrayList<String> lista;
    ArrayAdapter adaptador;
    List<Integer> arregloID = new ArrayList<Integer>();

    //permisos
    private String DIRECTORIO_PDFS = Environment.getExternalStorageDirectory().getAbsolutePath()+"/MIAPPPDFS";
    private boolean tienePermisoAlmacenamiento = false;
    private static final int CODIGO_PERMISOS_ALMACENAMIENTO = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        //Verificar Permisos
        verificarYPermisosDeAlmacenamiento();

        //Conexion a SQLITE
        objConfiguracion = new Configuraciones();
        objConexion = new ConexionSQLite(Contact.this, objConfiguracion.BD, null, 1);


        cajaBusquedaNombre = findViewById(R.id.txtCriterio);
        listaContactos = (ListView) findViewById(R.id.lvContactos);
        listaContactos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int idSeleccionado = arregloID.get(position);
                Intent ventanaModificar = new Intent(Contact.this, modificar_contacto.class);
                ventanaModificar.putExtra("id_contacto", idSeleccionado);
                startActivity(ventanaModificar);
            }
        });

        botonBuscar = findViewById(R.id.btnBuscar);
        botonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscar();
            }
        });

        botonAgregar = (Button) findViewById(R.id.btnAgregar);
        botonAgregar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent ventana = new Intent(Contact.this, Add_Contact.class);
                startActivity(ventana);
            }
        });


    }

    public void buscar(){
            lista = llenarLista();
            adaptador = new ArrayAdapter(Contact.this, android.R.layout.simple_list_item_1, lista);
            listaContactos.setAdapter(adaptador);
    }

    @Override
    protected void onResume() {
        super.onResume();
        buscar();
    }

    public ArrayList llenarLista(){
        ArrayList<String> miLista = new ArrayList<String>();
        SQLiteDatabase base = objConexion.getReadableDatabase();
        String consulta = "select id_contacto,nombre,telefono from contactos WHERE nombre LIKE '%"+ cajaBusquedaNombre.getText().toString() +"%' OR telefono LIKE '%"+ cajaBusquedaNombre.getText().toString() +"%' " + " order by nombre ASC";
        @SuppressLint("Recycle") Cursor cadaRegistro = base.rawQuery(consulta, null);

        arregloID.clear();
        if(cadaRegistro.moveToFirst()){
            do{
                miLista.add(cadaRegistro.getString(1).toString()+ " - "+cadaRegistro.getString(2).toString());
                arregloID.add(cadaRegistro.getInt(0));
            }while(cadaRegistro.moveToNext());
        }
        base.close();
        return miLista;
    }

    private void verificarYPermisosDeAlmacenamiento(){
        int estadoPermiso = ContextCompat.checkSelfPermission(Contact.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(estadoPermiso == PackageManager.PERMISSION_GRANTED){
            permisosDeAlmacenamientoConcedido();
        }else{
            //si no, entonces pedimos permisos, ahora mira onRequestPermissionResult
            ActivityCompat.requestPermissions(Contact.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODIGO_PERMISOS_ALMACENAMIENTO);
        }
    }

    private void permisosDeAlmacenamientoConcedido(){
        File carpeta = new File(DIRECTORIO_PDFS);
        if (!carpeta.exists()){
            carpeta.mkdir();
        }else{
            Log.i("Contact", "Carpeta Existente");
        }
        tienePermisoAlmacenamiento = true;
    }

    //Solicitud de Permisos
    private void permisosDeAlmacenamientoDenegado(){
        Toast.makeText(Contact.this, "El Permiso para el almacenamiento esta denegado", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CODIGO_PERMISOS_ALMACENAMIENTO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    permisosDeAlmacenamientoConcedido();
                }else{
                    permisosDeAlmacenamientoDenegado();
                }
                break;
        }
    }



    @Override
    public void onStop() {
        super.onStop();
        Contact.this.finish();
    }


}