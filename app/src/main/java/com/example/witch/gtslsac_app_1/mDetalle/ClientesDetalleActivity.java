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

public class ClientesDetalleActivity extends AppCompatActivity {

    TextView txtNombreEmpresa;
    ImageView imgViewDetalle;
    String nombre, logo;
    int id, pos;
    EditText nameEditTxt;
    Button btnModificar, btnEliminar, btnSeleccionarImagen;
    String opcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clientes_detail_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabDetalleClientes);
        txtNombreEmpresa = (TextView) findViewById(R.id.nombreTxtDetail);
        imgViewDetalle = (ImageView) findViewById(R.id.imgview_logo_empresa);

        //RECIBIENDO LOS DATOS
        Intent i = this.getIntent();
        id = i.getExtras().getInt("ID_KEY");
        nombre = i.getExtras().getString("EMPRESA_KEY");
        logo = i.getExtras().getString("LOGO_KEY");
        pos = i.getExtras().getInt("POSICION_KEY");
        //PARA TESTING
        //Toast.makeText(getApplicationContext(), "ID: " + String.valueOf(id) + " NOMBRE: " + nombre + " LOGO: " + logo, Toast.LENGTH_SHORT).show();
        Log.e("SELECCIONASTE", " ID: " + String.valueOf(id) + " NOMBRE: " + nombre + " LOGO: " + logo);
        //BINDEANDO
        txtNombreEmpresa.setText(nombre);
        Picasso.with(getApplicationContext()).load(logo).into(imgViewDetalle);
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
        d.setContentView(R.layout.clientes_layout_dialog);

        nameEditTxt = (EditText) d.findViewById(R.id.EditTxtEmpresaDialog);

        btnModificar = (Button) d.findViewById(R.id.btnGuardarCliente);
        btnModificar.setText("ACTUALIZAR");
        btnSeleccionarImagen = (Button) d.findViewById(R.id.BtnChooseCliente);
        btnSeleccionarImagen.setVisibility(View.GONE);
        btnEliminar = (Button) d.findViewById(R.id.btnEliminarCliente);
        btnEliminar.setVisibility(Button.VISIBLE);

        nameEditTxt.setText(nombre);
        d.show();
        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombre = nameEditTxt.getText().toString();
                if (nombre.equals("")) {
                    Toast.makeText(getApplicationContext(), "ERROR: Debe registrar al menos el NOMBRE de la EMPRESA", Toast.LENGTH_SHORT).show();
                } else {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ADAPTADOR_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        nameEditTxt.setText(nombre);
                                        txtNombreEmpresa.setText(nombre);
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
                                            Log.e("MODIFICACION ", String.valueOf(modificacion));
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
                            opcion = "ActualizarCliente";
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Id_Cliente", String.valueOf(id));
                            params.put("Empresa", nombre);
                            params.put("Logo", logo);
                            params.put("Opcion", opcion);
                            return params;
                        }
                    };
                    MySingleton.getInstance(ClientesDetalleActivity.this).addRequestQueue(stringRequest);
                }
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id==0) {
                    Toast.makeText(getApplicationContext(), "ERROR: NO SELECCIONASTE NINGUN CLIENTE PARA ELIMINAR", Toast.LENGTH_SHORT).show();
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
                                            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                            SharedPreferences.Editor editor = sharedPref.edit();
                                            editor.putInt(Config.REGISTRO_CAMBIADO, 1);
                                            editor.commit();
                                            d.dismiss();
                                            ClientesDetalleActivity.this.finish();
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
                            opcion = "EliminarCliente";
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Id_Cliente", String.valueOf(id));
                            params.put("Opcion", opcion);
                            return params;
                        }
                    };
                    MySingleton.getInstance(ClientesDetalleActivity.this).addRequestQueue(stringRequest);
                }

            }
        });
    }
}
