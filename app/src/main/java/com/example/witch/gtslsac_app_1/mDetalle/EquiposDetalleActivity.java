package com.example.witch.gtslsac_app_1.mDetalle;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.witch.gtslsac_app_1.R;
import com.example.witch.gtslsac_app_1.mVolley.Config;
import com.example.witch.gtslsac_app_1.mVolley.MySingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EquiposDetalleActivity extends AppCompatActivity {

    TextView txtNombreEquipo,txtMarcaEquipo,txtModeloEquipo,txtCapacidadEquipo,txtAnioEquipo,txtPlacaEquipo,txtColorEquipo,txtCodigoEquipo;
    ImageView imgViewDetalle;
    String nombre, marca, modelo, capacidad, anio, placa, color, codigo, foto;
    boolean estado;
    CheckBox chkEstadoEquipo;
    int id, pos;
    EditText nombreEditTxt,marcaEditTxt,modeloEditTxt,capacidadEditTxt,anioEditTxt,placaEditTxt,colorEditTxt,codigoEditTxt;
    Button btnModificar, btnEliminar, btnSeleccionarImagen;
    String opcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equipos_detail_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabDetalleEquipos);
        txtNombreEquipo = (TextView) findViewById(R.id.nombreTxtDetail);
        txtMarcaEquipo = (TextView) findViewById(R.id.marcaTxtDetail);
        txtModeloEquipo = (TextView) findViewById(R.id.modeloTxtDetail);
        txtCapacidadEquipo = (TextView) findViewById(R.id.capacidadTxtDetail);
        txtAnioEquipo = (TextView) findViewById(R.id.anioTxtDetail);
        txtPlacaEquipo = (TextView) findViewById(R.id.placaTxtDetail);
        txtColorEquipo = (TextView) findViewById(R.id.colorTxtDetail);
        txtCodigoEquipo = (TextView) findViewById(R.id.codigoTxtDetail);


        imgViewDetalle = (ImageView) findViewById(R.id.fotoEquipoImageDetail);

        //RECIBIENDO LOS DATOS
        Intent i = this.getIntent();
        id = i.getExtras().getInt("ID_KEY");
        nombre = i.getExtras().getString("NOMBRE_KEY");
        marca = i.getExtras().getString("MARCA_KEY");
        modelo = i.getExtras().getString("MODELO_KEY");
        capacidad = i.getExtras().getString("CAPACIDAD_KEY");
        anio = i.getExtras().getString("ANIO_KEY");
        placa = i.getExtras().getString("PLACA_KEY");
        color = i.getExtras().getString("COLOR_KEY");
        codigo = i.getExtras().getString("CODIGO_KEY");
        foto = i.getExtras().getString("FOTO_KEY");
        estado = i.getExtras().getBoolean("ESTADO_KEY");
        pos = i.getExtras().getInt("POSICION_KEY");
        //PARA TESTING
        //Toast.makeText(getApplicationContext(), "ID: " + String.valueOf(id) + " NOMBRE: " + nombre + " LOGO: " + logo, Toast.LENGTH_SHORT).show();
        Log.e("SELECCIONASTE", " ID: " + String.valueOf(id) + " NOMBRE: " + nombre + " MARCA: " + marca +" MODELO: " + modelo +" CAPACIDAD: " + capacidad +" AÑO: " + anio +
                " PLACA: " + placa +" COLOR: " + color +" CODIGO: " + codigo +" ESTADO: " + estado +" FOTO: " + foto);
        //BINDEANDO
        txtNombreEquipo.setText(nombre);
        txtMarcaEquipo.setText(marca);
        txtModeloEquipo.setText(modelo);
        txtCapacidadEquipo.setText(capacidad);
        txtAnioEquipo.setText(anio);
        txtPlacaEquipo.setText(placa);
        txtColorEquipo.setText(color);
        txtCodigoEquipo.setText(codigo);

        Picasso.with(getApplicationContext()).load(foto).into(imgViewDetalle);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayUpdateDialog();
            }
        });
    }

    //UPDATE OR DELETE
    private void displayUpdateDialog() {

        final Dialog d = new Dialog(this);
        d.setTitle("MODIFICAR DATOS");
        d.setContentView(R.layout.equipos_layout_dialog);

        nombreEditTxt = (EditText) d.findViewById(R.id.EditTxtNombreEquipoDialog);
        marcaEditTxt = (EditText) d.findViewById(R.id.EditTxtMarcaEquipoDialog);
        modeloEditTxt = (EditText) d.findViewById(R.id.EditTxtModeloEquipoDialog);
        capacidadEditTxt = (EditText) d.findViewById(R.id.EditTxtCapacidadEquipoDialog);
        anioEditTxt = (EditText) d.findViewById(R.id.EditTxtAnioEquipoDialog);
        placaEditTxt = (EditText) d.findViewById(R.id.EditTxtPlacaEquipoDialog);
        colorEditTxt = (EditText) d.findViewById(R.id.EditTxtColorEquipoDialog);
        codigoEditTxt = (EditText) d.findViewById(R.id.EditTxtCodigoEquipoDialog);


        btnModificar = (Button) d.findViewById(R.id.btnGuardarEquipo);
        btnModificar.setText("ACTUALIZAR");
        btnSeleccionarImagen = (Button) d.findViewById(R.id.BtnChooseImagenEquipo);
        btnSeleccionarImagen.setVisibility(View.GONE);
        btnEliminar = (Button) d.findViewById(R.id.btnEliminarEquipo);
        btnEliminar.setVisibility(Button.VISIBLE);
        chkEstadoEquipo = (CheckBox) d.findViewById(R.id.chkEstadoEquipoDialog);

        nombreEditTxt.setText(nombre);
        marcaEditTxt.setText(marca);
        modeloEditTxt.setText(modelo);
        capacidadEditTxt.setText(capacidad);
        anioEditTxt.setText(anio);
        placaEditTxt.setText(placa);
        colorEditTxt.setText(color);
        codigoEditTxt.setText(codigo);
        if (estado == true) {
            chkEstadoEquipo.setChecked(true);
        } else {
            chkEstadoEquipo.setChecked(false);
        }
        d.show();

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombre = nombreEditTxt.getText().toString();
                marca = marcaEditTxt.getText().toString();
                modelo = modeloEditTxt.getText().toString();
                capacidad = capacidadEditTxt.getText().toString();
                anio = anioEditTxt.getText().toString();
                placa = placaEditTxt.getText().toString();
                color = colorEditTxt.getText().toString();
                codigo = codigoEditTxt.getText().toString();
                estado = chkEstadoEquipo.isChecked() ? true : false;
                if (nombre.equals("")) {
                    Toast.makeText(getApplicationContext(), "ERROR: Debe registrar al menos el NOMBRE del EQUIPO", Toast.LENGTH_SHORT).show();
                } else {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ADAPTADOR_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        nombreEditTxt.setText(nombre);
                                        txtNombreEquipo.setText(nombre);
                                        marcaEditTxt.setText(marca);
                                        txtMarcaEquipo.setText(marca);
                                        modeloEditTxt.setText(modelo);
                                        txtModeloEquipo.setText(modelo);
                                        capacidadEditTxt.setText(capacidad);
                                        txtCapacidadEquipo.setText(capacidad);
                                        anioEditTxt.setText(anio);
                                        txtAnioEquipo.setText(anio);
                                        placaEditTxt.setText(placa);
                                        txtPlacaEquipo.setText(placa);
                                        colorEditTxt.setText(color);
                                        txtColorEquipo.setText(color);
                                        codigoEditTxt.setText(codigo);
                                        txtCodigoEquipo.setText(codigo);
                                        JSONArray jsonArray = new JSONArray(response);
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        String codigo = jsonObject.getString("codigo");
                                        String mensaje = jsonObject.getString("mensaje");
                                        Toast.makeText(getApplicationContext(), "MENSAJE: " + mensaje, Toast.LENGTH_SHORT).show();
                                        //Para cerrar el dialog cuando la modificacion haya sido exitosa
                                        if (codigo.equals("reg_success")) {
                                            //Creamos un Shared Preference para anunciar el cambio de un registro
                                            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                            SharedPreferences.Editor editor = sharedPref.edit();
                                            editor.putInt(Config.REGISTRO_CAMBIADO, 1);
                                            editor.commit();
                                            int modificacion = sharedPref.getInt(Config.REGISTRO_CAMBIADO, 0);
                                            Log.e("MODIFICACION EN DISPLAY", String.valueOf(modificacion));
                                            d.dismiss();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                            error.printStackTrace();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            opcion = "ActualizarEquipo";
                            String valorEstado = (estado) ? "1" : "0";
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Id_Equipo", String.valueOf(id));
                            params.put("Nombre", nombre);
                            params.put("Marca", marca);
                            params.put("Modelo", modelo);
                            params.put("Capacidad", capacidad);
                            params.put("Anio", anio);
                            params.put("Placa", placa);
                            params.put("Color", color);
                            params.put("Codigo", codigo);
                            //params.put("Foto", foto);
                            params.put("Estado", valorEstado);
                            params.put("Opcion", opcion);
                            return params;
                        }
                    };
                    MySingleton.getInstance(EquiposDetalleActivity.this).addRequestQueue(stringRequest);
                }
            }
        });



        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (id==0) {
                    Toast.makeText(getApplicationContext(), "ERROR: NO SELECCIONASTE NINGUN EQUIPO PARA ELIMINAR", Toast.LENGTH_SHORT).show();
                } else {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ADAPTADOR_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONArray jsonArray = new JSONArray(response);
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        String codigo = jsonObject.getString("codigo");
                                        String mensaje = jsonObject.getString("mensaje");
                                        Toast.makeText(getApplicationContext(), "MENSAJE: " + mensaje, Toast.LENGTH_SHORT).show();
                                        //Para cerrar el dialog y finalizar la actividad cuando se haya eliminado correctamente
                                        if (codigo.equals("reg_success")) {
                                            //Creamos un Shared Preference para anunciar el cambio de un registro
                                            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                            SharedPreferences.Editor editor = sharedPref.edit();
                                            editor.putInt(Config.REGISTRO_CAMBIADO, 1);
                                            editor.commit();
                                            int modificacion = sharedPref.getInt(Config.REGISTRO_CAMBIADO, 0);
                                            Log.e("MODIFICACION EN DISPLAY", String.valueOf(modificacion));
                                            d.dismiss();
                                            EquiposDetalleActivity.this.finish();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                            error.printStackTrace();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            opcion = "EliminarEquipo";
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Id_Equipo", String.valueOf(id));
                            params.put("Opcion", opcion);
                            return params;
                        }
                    };
                    MySingleton.getInstance(EquiposDetalleActivity.this).addRequestQueue(stringRequest);
                }

            }
        });
    }
}
