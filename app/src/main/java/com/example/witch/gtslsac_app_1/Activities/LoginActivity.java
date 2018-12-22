package com.example.witch.gtslsac_app_1.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    final String opcion = "Login";
    TextView textView;
    Button login_button;
    EditText Correo, Password;
    AlertDialog.Builder builder;

    //Definimos las vistas
    private EditText editTextEmail;
    private EditText editTextPassword;
    private AppCompatButton buttonLogin;

    //La variable boolean sirve para verificar si estamos logueados o no
    //initially it is false
    private boolean loggedIn = false;

    //---------------------

    //Textview para mostrar el usuario actualmente logueado
    private TextView tvCurrentUser;


    //---------------------


    @Override
    protected void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        //seteando el toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textView = (TextView) findViewById(R.id.reg_txt);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, Register.class));
            }
        });

        builder = new AlertDialog.Builder(LoginActivity.this);
        login_button = (Button) findViewById(R.id.btn_login);
        Correo = (EditText) findViewById(R.id.login_correo);
        Password = (EditText) findViewById(R.id.login_password);

        login_button.setOnClickListener(this);
    }

    private void login() {
        final String correo = Correo.getText().toString();
        final String password = Password.getText().toString();
        if (correo.equals("") || password.equals("")) {
            builder.setTitle("Algo salió mal...");
            displayAlert("Ingrese un usuario y Password válidos");
        } else {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ADAPTADOR_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String codigo = jsonObject.getString("codigo");
                                if (codigo.equals("login_failed")) {
                                    builder.setTitle("Error en el Login....");
                                    displayAlert(jsonObject.getString("mensaje"));
                                } else {
                                    int idUsuario = jsonObject.getInt("id_usuario");
                                    String nombresUsuario = jsonObject.getString("nombres_usuario");
                                    String apellidosUsuario = jsonObject.getString("apellidos_usuario");
                                    String correo = jsonObject.getString("correo");

                                    Toast.makeText(getApplicationContext(), "BIENVENIDO: " +  nombresUsuario + " " + apellidosUsuario, Toast.LENGTH_LONG).show();

                                    Log.e("DATOS USUARIO", " ID: " + idUsuario +  " NOMBRES: " +  nombresUsuario + " " + apellidosUsuario + " CORREO: " + correo);

                                    SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putInt(Config.ID_USUARIO_ALQUILER, idUsuario);
                                    editor.putString(Config.NOMBRE_USUARIO_ALQUILER, nombresUsuario +  " " + apellidosUsuario);
                                    editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
                                    editor.putString(Config.EMAIL_SHARED_PREF, correo);
                                    editor.commit();

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("usuario", correo);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Correo", correo);
                    params.put("Password", password);
                    params.put("Opcion", opcion);
                    return params;
                }
            };
            MySingleton.getInstance(LoginActivity.this).addRequestQueue(stringRequest);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //In onresume fetching value from sharedpreference
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //Fetching the boolean value form sharedpreferences
        loggedIn = sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);
        String email = sharedPreferences.getString(Config.EMAIL_SHARED_PREF, null);

        //SI ESTAMOS YA LOGUEADOS
        if (loggedIn) {
            //EMPEZAREMOS DESDE MainActivity

            Bundle bundle = new Bundle();
            bundle.putString("usuario", email);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        //Calling the login function
        login();
    }


    private void displayAlert(String mensaje) {
        builder.setMessage(mensaje);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Correo.setText("");
                Password.setText("");
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}


