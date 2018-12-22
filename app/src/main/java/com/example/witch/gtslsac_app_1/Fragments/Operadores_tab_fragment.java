package com.example.witch.gtslsac_app_1.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.witch.gtslsac_app_1.R;
import com.example.witch.gtslsac_app_1.mDatos.Operador;
import com.example.witch.gtslsac_app_1.mRecycler.OperadoresRecyclerViewAdapter;
import com.example.witch.gtslsac_app_1.mVolley.Config;
import com.example.witch.gtslsac_app_1.mVolley.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class Operadores_tab_fragment extends Fragment {

    private OperadoresRecyclerViewAdapter rAdapter;
    private ArrayList<Operador> operadores;

    RecyclerView rv;

    EditText edTxtNombreOperador, edTxtApellidoOperador;
    CheckBox chkEstadoOperador;
    private int PICK_IMAGE_REQUEST = 1;
    Button btnInsertar, btnSeleccionarImagen;
    private Bitmap bitmap;
    ImageView imgView;
    SearchView sv;
    String nombresOperador, apellidosOperador, opcion, image;
    boolean estadoOperador;
    AlertDialog.Builder builder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView;
        myView = inflater.inflate(R.layout.operadores_fragment, container, false);

        FloatingActionButton fab = (FloatingActionButton) myView.findViewById(R.id.fabOperadores);

        rv = (RecyclerView) myView.findViewById(R.id.recicladorOperadores);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setItemAnimator(new DefaultItemAnimator());
        builder = new AlertDialog.Builder(getContext());
        operadores = new ArrayList<>();
        cargarDatosRecyclerView();
        sv = (SearchView) myView.findViewById(R.id.searchViewOperadores);
        sv.setQueryHint("BUSCAR OPERADORES");
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String txt) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                ArrayList<Operador> newList = new ArrayList<>();
                for (Operador operador : operadores) {
                    String nombres = operador.getNombresOperador().toLowerCase();
                    String apellidos = operador.getApellidosOperador().toLowerCase();
                    if (nombres.contains(newText) || apellidos.contains(newText))
                        newList.add(operador);
                    rAdapter.setFilter(newList);
                }
                return false;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDialog();
            }
        });
        return myView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                imgView.setVisibility(View.VISIBLE);
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imgView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void seleccionarImagen() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadImage() {
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(getContext(), "Subiendo Imagen...", "Por Favor Espere...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.UPLOAD_URL_IMG_OPERADORES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        //Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
                        Log.e("UPLOAD IMAGE RESPONSE: ", s);
                        try {
                            JSONArray jsonArray = new JSONArray(s);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String codigo = jsonObject.getString("codigo");
                            String mensaje = jsonObject.getString("mensaje");
                            if (codigo.equals("reg_failed")) {
                                Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
                                imgView.setImageResource(0);
                                imgView.setVisibility(View.GONE);
                                edTxtNombreOperador.setText("");
                                edTxtApellidoOperador.setText("");
                                cargarDatosRecyclerView();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                if(bitmap!=null){
                    image = imageToString(bitmap);
                }
                //Getting Image Name
                String nombre1 = edTxtNombreOperador.getText().toString().trim() + " " + edTxtApellidoOperador.getText().toString().trim();
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();
                //Adding parameters
                params.put("Foto", image);
                params.put("Nombre", nombre1);
                //returning parameters
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getContext()).addRequestQueue(stringRequest);
    }

    public String imageToString(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    //DISPLAY DIALOG
    private void displayDialog() {

        final Dialog d = new Dialog(getContext());
        d.setTitle("AGREGAR DATOS");
        d.setContentView(R.layout.operadores_layout_dialog);

        edTxtNombreOperador = (EditText) d.findViewById(R.id.edtNombresOperadorDialog);
        edTxtApellidoOperador = (EditText) d.findViewById(R.id.edtApellidosOperadorDialog);
        chkEstadoOperador = (CheckBox) d.findViewById(R.id.chkEstadoOperadorDialog);
        btnInsertar = (Button) d.findViewById(R.id.btnGuardarOperador);
        btnSeleccionarImagen = (Button) d.findViewById(R.id.BtnChooseOperador);
        imgView = (ImageView) d.findViewById(R.id.imageviewFotoOperador);

        Log.e("ESTADO DE DIALOG: ", "MOSTRANDO DIALOG!!");
        d.show();

        btnSeleccionarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombresOperador = edTxtNombreOperador.getText().toString();
                if (nombresOperador.equals("")) {
                    builder.setMessage("Algo salió mal");
                    builder.setMessage("INGRESE ANTES EL NOMBRE DEl OPERADOR");
                    displayAlert("input_error");
                } else {
                    seleccionarImagen();
                }
            }

        });
        btnInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombresOperador = edTxtNombreOperador.getText().toString();
                apellidosOperador = edTxtApellidoOperador.getText().toString();
                estadoOperador = chkEstadoOperador.isChecked() ? true : false;
                Log.e("ENVIANDO NOMBRE: ", nombresOperador);
                if (nombresOperador.equals("")) {
                    builder.setMessage("Algo salió mal");
                    builder.setMessage("Debe Ingresar por lo menos el nombre del Operador");
                    displayAlert("input_error");
                } else {
                    //enviamos una peticion antes de la insercion -- REVISAR PORQUE SIN ESTO NO INSERTAN LAS IMAGENES
                    cargarDatosRecyclerView();
                    insertarOperador();
                }
            }
        });
    }

    private void insertarOperador() {
        //PARA AGREGAR A LA DB
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ADAPTADOR_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(bitmap!=null) {
                            uploadImage();
                        }else{
                            edTxtNombreOperador.setText("");
                            edTxtNombreOperador.requestFocus();
                            edTxtApellidoOperador.setText("");
                            cargarDatosRecyclerView();
                        }

                        //uploadImage();
                        /*try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String codigo = jsonObject.getString("codigo");
                            String mensaje = jsonObject.getString("mensaje");
                            if (codigo.equals("reg_failed")) {
                                Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
                                imgView.setImageResource(0);
                                edTxtNombreOperador.setText("");
                                edTxtNombreOperador.requestFocus();
                                edTxtApellidoOperador.setText("");
                                cargarDatosRecyclerView();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                //Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ERROR UPLOADING IMAGE:", e.getMessage().toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                opcion = "InsertarOperador";
                String valorEstado = (estadoOperador) ? "1" : "0";
                Map<String, String> params = new HashMap<String, String>();
                params.put("Nombres", nombresOperador);
                params.put("Apellidos", apellidosOperador);
                //ojo hardcodeando estado
                params.put("Estado", valorEstado);
                params.put("Opcion", opcion);
                Log.e("ENVIANDO PETICION: ", opcion + "DATO: " + nombresOperador + " " + apellidosOperador + " " + estadoOperador);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getContext()).addRequestQueue(stringRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("ESTADO DE ONRESUME: ", "ONRESUME LLAMADO!!");
        //llamamos a shared preferences cuando se resume el fragment para saber si ha sido eliminado o modificado algun registro
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        int modificacion = sharedPref.getInt(Config.REGISTRO_CAMBIADO, 0);
        Log.e("ANTES DE CARGAR DATOS", String.valueOf(modificacion));
        if (modificacion == 1) {
            //si fue modificado actualizamos el recycler view
            cargarDatosRecyclerView();
        }

    }

    private void displayAlert(final String codigo) {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (codigo.equals("input_error")) {
                    //nombreEditTxt.setText("");
                } else if (codigo.equals("reg_success")) {
                    //Toast.makeText(getContext(), "REGISTRO COMPLETADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                    //nombreEditTxt.setText("");
                } else if (codigo.equals("reg_failed")) {
                    //nombreEditTxt.setText("");
                    //Toast.makeText(getContext(), "REGISTRO NO COMPLETADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void cargarDatosRecyclerView() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Cargando Informacion...");
        progressDialog.show();


        StringRequest stringRequest1 = new StringRequest(Request.Method.POST,
                Config.ADAPTADOR_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("operadores");
                            operadores.clear();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                boolean valor = (o.getString("Estado").equals("1"));
                                Operador item = new Operador(
                                        o.getInt("Id_Operador"),
                                        o.getString("Nombres"),
                                        o.getString("Apellidos"),
                                        o.getString("Foto"),
                                        valor
                                );
                                operadores.add(item);
                            }
                            rAdapter = new OperadoresRecyclerViewAdapter(operadores, getContext());
                            rv.setAdapter(rAdapter);
                            //retornamos a 0 el valor de REGISTRO_CAMBIADO
                            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
                            int modificacion = sharedPref.getInt(Config.REGISTRO_CAMBIADO, 0);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putInt(Config.REGISTRO_CAMBIADO, 0);
                            editor.commit();
                            modificacion = sharedPref.getInt(Config.REGISTRO_CAMBIADO,0);
                            Log.e("DESPUES DE CARGAR DATOS", String.valueOf(modificacion));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getContext(), volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                opcion = "CargarOperadores";
                Map<String, String> params = new HashMap<String, String>();
                params.put("Opcion", opcion);
                return params;
            }
        };
        stringRequest1.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getContext()).addRequestQueue(stringRequest1);
    }
}