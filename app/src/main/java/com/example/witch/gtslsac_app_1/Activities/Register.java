package com.example.witch.gtslsac_app_1.Activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.witch.gtslsac_app_1.mVolley.Config;
import com.example.witch.gtslsac_app_1.mVolley.MySingleton;
import com.example.witch.gtslsac_app_1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    Button reg_bn;
    EditText Nombres, Apellidos, Correo, Password, ConPassword;
    String nombres, apellidos, correo, password, conPassword, opcion;
    int estado;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        reg_bn = (Button)findViewById(R.id.btn_reg);
        Nombres = (EditText)findViewById(R.id.reg_nombres);
        Apellidos = (EditText)findViewById(R.id.reg_apellidos);
        estado = 1;
        Correo = (EditText)findViewById(R.id.reg_correo);
        Password = (EditText)findViewById(R.id.reg_password);
        ConPassword = (EditText)findViewById(R.id.reg_con_password);

        builder = new AlertDialog.Builder(Register.this);


        reg_bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombres = Nombres.getText().toString();
                apellidos = Apellidos.getText().toString();
                correo = Correo.getText().toString();
                password = Password.getText().toString();
                conPassword = ConPassword.getText().toString();
                if (nombres.equals("") || apellidos.equals("") || correo.equals("") || password.equals("") || conPassword.equals("")) {
                    builder.setMessage("Algo salió mal");
                    builder.setMessage("Por favor llena todos los campos");
                    displayAlert("input_error");
                } else {
                    if (!(password.equals(conPassword))) {
                        builder.setMessage("Algo salió mal");
                        builder.setMessage("Los passwords no coinciden");
                        displayAlert("input_error");
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
                                            builder.setTitle("Respuesta del Servidor...");
                                            builder.setMessage(mensaje);
                                            displayAlert(codigo);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Register.this, "Error", Toast.LENGTH_SHORT).show();
                                error.printStackTrace();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                opcion = "InsertarUsuario";
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("Nombres", nombres);
                                params.put("Apellidos", apellidos);
                                params.put("Correo", correo);
                                params.put("Password", password);
                                params.put("Estado", String.valueOf(estado));
                                params.put("Opcion", opcion);
                                return params;
                            }
                        };
                        MySingleton.getInstance(Register.this).addRequestQueue(stringRequest);
                    }
                }
            }
        });
    }
    private void displayAlert(final String codigo) {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (codigo.equals("input_error"))
                {
                    Password.setText("");
                    ConPassword.setText("");

                } else if (codigo.equals("reg_success"))
                {
                    finish();
                } else if (codigo.equals("reg_failed"))
                {
                    Nombres.setText("");
                    Apellidos.setText("");
                    Correo.setText("");
                    Password.setText("");
                    ConPassword.setText("");
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}

