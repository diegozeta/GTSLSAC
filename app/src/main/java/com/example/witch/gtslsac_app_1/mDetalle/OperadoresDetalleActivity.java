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

public class OperadoresDetalleActivity extends AppCompatActivity {

    TextView txtNombresOperador, txtApellidosOperador;
    String nombres,apellidos, foto;
    boolean estado;

    ImageView imgViewDetalle;
    CheckBox chkEstadoCliente;
    int id, pos;
    EditText edTxtNombreOperador, edTxtApellidoOperador;
    Button btnModificar, btnEliminar, btnSeleccionarImagen;
    String opcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operadores_detail_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarOp);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabDetalleOperadores);

        txtNombresOperador = (TextView) findViewById(R.id.txtNombresOperadorDetail);
        txtApellidosOperador = (TextView) findViewById(R.id.txtApellidosOperadorDetail);
        imgViewDetalle = (ImageView) findViewById(R.id.imgview_foto_operador);

        //RECIBIENDO LOS DATOS
        Intent i = this.getIntent();
        id = i.getExtras().getInt("ID_KEY");
        nombres = i.getExtras().getString("NOMBRES_KEY");
        apellidos = i.getExtras().getString("APELLIDOS_KEY");
        estado = i.getExtras().getBoolean("ESTADO_KEY");
        foto = i.getExtras().getString("FOTO_KEY");
        pos = i.getExtras().getInt("POSICION_KEY");
        //PARA TESTING
        //Toast.makeText(getApplicationContext(), "ID: " + String.valueOf(id) + " NOMBRE: " + nombres + " APELLIDOS: " + apellidos + "ESTADO: " + estado, Toast.LENGTH_SHORT).show();
        Log.e("SELECCIONASTE", "ID: " + String.valueOf(id) + " NOMBRE: " + nombres + " APELLIDOS: " + apellidos + " ESTADO: " + estado );
        //BINDEANDO
        txtNombresOperador.setText(nombres);
        txtApellidosOperador.setText(apellidos);
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
        d.setContentView(R.layout.operadores_layout_dialog);

        edTxtNombreOperador = (EditText) d.findViewById(R.id.edtNombresOperadorDialog);
        edTxtApellidoOperador = (EditText) d.findViewById(R.id.edtApellidosOperadorDialog);
        chkEstadoCliente = (CheckBox) d.findViewById(R.id.chkEstadoOperadorDialog);
        btnSeleccionarImagen = (Button) d.findViewById(R.id.BtnChooseOperador);
        btnSeleccionarImagen.setVisibility(View.GONE);
        btnModificar = (Button) d.findViewById(R.id.btnGuardarOperador);
        btnModificar.setText("ACTUALIZAR");
        btnEliminar = (Button) d.findViewById(R.id.btnEliminarOperador);
        btnEliminar.setVisibility(Button.VISIBLE);

        edTxtNombreOperador.setText(nombres);
        edTxtApellidoOperador.setText(apellidos);
        if (estado == true) {
            chkEstadoCliente.setChecked(true);
        } else {
            chkEstadoCliente.setChecked(false);
        }
        d.show();
        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombres = edTxtNombreOperador.getText().toString();
                apellidos = edTxtApellidoOperador.getText().toString();
                estado = chkEstadoCliente.isChecked() ? true : false;
                Toast.makeText(getApplicationContext(), "ID: " + String.valueOf(id) + " NOMBRE: " + nombres + " APELLIDOS: " + apellidos + " ESTADO: " + estado, Toast.LENGTH_SHORT).show();
                if (nombres.equals("")) {
                    Toast.makeText(getApplicationContext(), "ERROR: El Nombre del Operador no puede estar vacio", Toast.LENGTH_SHORT).show();
                } else {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ADAPTADOR_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        edTxtNombreOperador.setText(nombres);
                                        txtNombresOperador.setText(nombres);

                                        edTxtApellidoOperador.setText(apellidos);
                                        txtApellidosOperador.setText(apellidos);

                                        JSONArray jsonArray = new JSONArray(response);
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        String codigo = jsonObject.getString("codigo");
                                        String mensaje = jsonObject.getString("mensaje");
                                        Toast.makeText(getApplicationContext(), "MENSAJE: " + mensaje, Toast.LENGTH_SHORT).show();
                                        //Para cerrar el dialog cuando la modificacion haya sido exitosa
                                        if (codigo.equals("reg_success")) {
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
                            opcion = "ActualizarOperador";
                            String valorEstado = (estado) ? "1" : "0";
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Id_Operador", String.valueOf(id));
                            params.put("Nombres", nombres);
                            params.put("Apellidos", apellidos);
                            params.put("Foto", foto);
                            params.put("Estado", valorEstado);
                            params.put("Opcion", opcion);
                            return params;
                        }
                    };
                    MySingleton.getInstance(OperadoresDetalleActivity.this).addRequestQueue(stringRequest);
                }
            }
        });
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (id == 0) {
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
                                            OperadoresDetalleActivity.this.finish();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                            error.printStackTrace();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            opcion = "EliminarOperador";
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Id_Operador", String.valueOf(id));
                            params.put("Opcion", opcion);
                            return params;
                        }
                    };
                    MySingleton.getInstance(OperadoresDetalleActivity.this).addRequestQueue(stringRequest);
                }

            }
        });
    }
}
