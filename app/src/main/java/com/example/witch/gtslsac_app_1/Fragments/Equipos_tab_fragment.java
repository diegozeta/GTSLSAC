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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.witch.gtslsac_app_1.R;
import com.example.witch.gtslsac_app_1.mDatos.Equipo;
import com.example.witch.gtslsac_app_1.mRecycler.EquiposRecyclerViewAdapter;
import com.example.witch.gtslsac_app_1.mVolley.Config;
import com.example.witch.gtslsac_app_1.mVolley.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class Equipos_tab_fragment extends Fragment {
    private EquiposRecyclerViewAdapter rAdapter;
    private ArrayList<Equipo> equipos;
    RecyclerView rv;
    EditText nombreEditTxt,marcaEditTxt,modeloEditTxt,capacidadEditTxt,anioEditTxt,placaEditTxt,colorEditTxt,codigoEditTxt;
    private int PICK_IMAGE_REQUEST = 1;
    Button btnInsertar, btnSeleccionarImagen;
    private Bitmap bitmap;
    ImageView imgView;
    String nombre, marca, modelo, capacidad, anio, placa, color, codigo, foto, opcion;
    AlertDialog.Builder builder;
    SearchView sv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView;
        myView = inflater.inflate(R.layout.equipos_fragment, container, false);

        FloatingActionButton fab = (FloatingActionButton) myView.findViewById(R.id.fabEquipos);

        rv = (RecyclerView) myView.findViewById(R.id.recicladorEquipos);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setItemAnimator(new DefaultItemAnimator());
        builder = new AlertDialog.Builder(getContext());
        equipos = new ArrayList<>();
        cargarDatosRecyclerView();
        sv = (SearchView) myView.findViewById(R.id.searchViewEquipos);
        sv.setQueryHint("BUSCAR EQUIPOS");
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String txt) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                ArrayList<Equipo> newList = new ArrayList<>();
                for (Equipo equipo : equipos) {
                    String nombre = equipo.getNombreEquipo().toLowerCase();
                    String marca = equipo.getMarcaEquipo().toLowerCase();
                    String modelo = equipo.getModeloEquipo().toLowerCase();
                    String capacidad = equipo.getCapacidadEquipo().toString().toLowerCase();
                    String anio = equipo.getAnioEquipo().toString().toLowerCase();
                    String placa = equipo.getPlacaEquipo().toLowerCase();
                    String color = equipo.getColorEquipo().toLowerCase();
                    String codigo = equipo.getCodigoEquipo().toLowerCase();

//                    String completo = nombre + " " + marca + " " +  modelo + " " +  capacidad + " " + anio + " " +  placa + " " +  color + " " +  codigo;
//                    if (completo.contains(newText))

                    if (nombre.contains(newText)||marca.contains(newText)||modelo.contains(newText)||capacidad.contains(newText)||anio.contains(newText)||placa.contains(newText)||color.contains(newText)||codigo.contains(newText))
                        newList.add(equipo);
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
        final ProgressDialog loading = ProgressDialog.show(getContext(), "Uploading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.UPLOAD_URL_IMG_EQUIPOS,
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
                                nombreEditTxt.setText("");
                                marcaEditTxt.setText("");
                                modeloEditTxt.setText("");
                                capacidadEditTxt.setText("");
                                anioEditTxt.setText("");
                                placaEditTxt.setText("");
                                colorEditTxt.setText("");
                                codigoEditTxt.setText("");
                                nombreEditTxt.requestFocus();
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
                String image = imageToString(bitmap);
                //Getting Image Name
                String name1 = nombreEditTxt.getText().toString().trim();
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();
                //Adding parameters
                params.put("Foto", image);
                params.put("Nombre", name1);
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
        d.setContentView(R.layout.equipos_layout_dialog);
        nombreEditTxt = (EditText) d.findViewById(R.id.EditTxtNombreEquipoDialog);
        marcaEditTxt = (EditText) d.findViewById(R.id.EditTxtMarcaEquipoDialog);
        modeloEditTxt = (EditText) d.findViewById(R.id.EditTxtModeloEquipoDialog);
        capacidadEditTxt = (EditText) d.findViewById(R.id.EditTxtCapacidadEquipoDialog);
        anioEditTxt = (EditText) d.findViewById(R.id.EditTxtAnioEquipoDialog);
        placaEditTxt = (EditText) d.findViewById(R.id.EditTxtPlacaEquipoDialog);
        colorEditTxt = (EditText) d.findViewById(R.id.EditTxtColorEquipoDialog);
        codigoEditTxt = (EditText) d.findViewById(R.id.EditTxtCodigoEquipoDialog);

        btnInsertar = (Button) d.findViewById(R.id.btnGuardarEquipo);
        btnSeleccionarImagen = (Button) d.findViewById(R.id.BtnChooseImagenEquipo);
        imgView = (ImageView) d.findViewById(R.id.imageViewFotoEquipo);
        Log.e("ESTADO DE DIALOG: ", "MOSTRANDO DIALOG!!");
        d.show();

        btnSeleccionarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombre = nombreEditTxt.getText().toString();
                if (nombre.equals("")) {
                    builder.setMessage("Algo salió mal");
                    builder.setMessage("INGRESE ANTES EL NOMBRE DEL EQUIPO");
                    displayAlert("input_error");
                } else {
                    seleccionarImagen();
                }
            }

        });
        btnInsertar.setOnClickListener(new View.OnClickListener() {
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
                Log.e("ENVIANDO NOMBRE: ", nombre);
                if (nombre.equals("")) {
                    builder.setMessage("Algo salió mal");
                    builder.setMessage("Debe Ingresar por lo menos el nombre del Equipo");
                    displayAlert("input_error");
                } else {
                    //enviamos una peticion antes de la insercion -- REVISAR PORQUE SIN ESTO NO INSERTAN LAS IMAGENES
                    cargarDatosRecyclerView();
                    insertarEquipo();
                }
            }
        });
    }

    private void insertarEquipo() {
        //PARA AGREGAR A LA DB
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ADAPTADOR_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //uploadImage();
                        cargarDatosRecyclerView();
                        /*try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String codigo = jsonObject.getString("codigo");
                            if (codigo.equals("reg_failed")) {
                                builder.setTitle("Error en el Registro....");
                                displayAlert(jsonObject.getString("mensaje"));
                            } else {
                                String mensaje = jsonObject.getString("mensaje");
                                builder.setTitle("Respuesta del Servidor...");
                                builder.setMessage(mensaje);
                                displayAlert(codigo);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                //Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ERROR UPLOADING IMAGE: ", e.getMessage().toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                opcion = "InsertarEquipo";
                Map<String, String> params = new HashMap<>();
                params.put("Nombre", nombre);
                params.put("Marca", marca);
                params.put("Modelo", modelo);
                params.put("Capacidad", capacidad);
                params.put("Anio", anio);
                params.put("Placa", placa);
                params.put("Color", color);
                params.put("Codigo", codigo);
                params.put("Opcion", opcion);
                Log.e("ENVIANDO PETICION", opcion + " Nombre " + nombre + " Marca " + marca + " Modelo " + modelo +
                        " Capacidad " + capacidad + " Año " + anio + " Placa " + placa + " Color " + color + " Codigo " + codigo );
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getContext()).addRequestQueue(stringRequest);
    }

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
                            JSONArray array = jsonObject.getJSONArray("equipos");
                            equipos.clear();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                boolean valor = (o.getString("Estado").equals("1"));
                                Equipo item = new Equipo(
                                        o.getInt("Id_Equipo"),
                                        o.getString("Nombre"),
                                        o.getString("Marca"),
                                        o.getString("Modelo"),
                                        o.getString("Capacidad"),
                                        o.getString("Anio"),
                                        o.getString("Placa"),
                                        o.getString("Color"),
                                        o.getString("Codigo"),
                                        o.getString("Foto"),
                                        valor
                                );
                                equipos.add(item);
                            }
                            rAdapter = new EquiposRecyclerViewAdapter(equipos, getContext());
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
                        //Toast.makeText(getContext(), volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }

                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                opcion = "CargarEquipos";
                Map<String, String> params = new HashMap<>();
                params.put("Opcion", opcion);
                Log.e("ENVIANDO PETICION: ", "CARGAR EQUIPOS " + opcion);
                return params;
            }
        };
        stringRequest1.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getContext()).addRequestQueue(stringRequest1);
    }
}