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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.witch.gtslsac_app_1.R;
import com.example.witch.gtslsac_app_1.mDatos.Cliente;
import com.example.witch.gtslsac_app_1.mRecycler.ClientesRecyclerViewAdapter;
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

public class Clientes_tab_fragment extends Fragment{
    private ClientesRecyclerViewAdapter rAdapter;
    private ArrayList<Cliente> clientes;
    RecyclerView rv;
    EditText edTxtNombreEmpresa;
    private int PICK_IMAGE_REQUEST = 1;
    Button btnInsertar, btnSeleccionarImagen;
    private Bitmap bitmap;
    ImageView imgView;
    String nombreEmpresa, opcion;
    AlertDialog.Builder builder;
    SearchView sv;
    String image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView;
        myView = inflater.inflate(R.layout.clientes_fragment, container, false);
        FloatingActionButton fab = (FloatingActionButton) myView.findViewById(R.id.fabClientes);
        rv = (RecyclerView) myView.findViewById(R.id.recicladorClientes);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setItemAnimator(new DefaultItemAnimator());
        builder = new AlertDialog.Builder(getContext());
        clientes = new ArrayList<>();
        cargarDatosRecyclerView();
        /*
        recibiendo los datos
        Boolean esAlquiler = getArguments().getBoolean("esAlquiler");
        if (esAlquiler) {
        onAlquiler();
        }
        */

        sv = (SearchView) myView.findViewById(R.id.searchViewClientes);
        sv.setQueryHint("BUSCAR CLIENTES");
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String txt) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                ArrayList<Cliente> newList = new ArrayList<>();
                for (Cliente cliente : clientes) {
                    String nombre = cliente.getNombreEmpresa().toLowerCase();
                    if (nombre.contains(newText))
                        newList.add(cliente);
                    rAdapter.setFilterRecycler(newList);
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

    public void onAlquiler() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt(Config.PROCESO_DE_ALQUILER, 0);
                        editor.commit();
                        int enAlquiler = sharedPref.getInt(Config.PROCESO_DE_ALQUILER, 0);
                        Log.e("ALQUILER VOLVIENDO", String.valueOf(enAlquiler));

                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("DESEAS AGREGAR ESTE CLIENTE AL ALQUILER?").setPositiveButton("SI", dialogClickListener).setNegativeButton("NO", dialogClickListener).show();

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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.UPLOAD_URL_IMG_CLIENTES,
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
                                edTxtNombreEmpresa.setText("");
                                edTxtNombreEmpresa.requestFocus();
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
                String name1 = edTxtNombreEmpresa.getText().toString().trim();
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();
                //Adding parameters
                params.put("Logo", image);
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
        d.setContentView(R.layout.clientes_layout_dialog);
        edTxtNombreEmpresa = (EditText) d.findViewById(R.id.EditTxtEmpresaDialog);
        btnInsertar = (Button) d.findViewById(R.id.btnGuardarCliente);
        btnSeleccionarImagen = (Button) d.findViewById(R.id.BtnChooseCliente);
        imgView = (ImageView) d.findViewById(R.id.imageViewLogoCliente);
        Log.e("ESTADO DE DIALOG: ", "MOSTRANDO DIALOG!!");
        d.show();

        btnSeleccionarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombreEmpresa = edTxtNombreEmpresa.getText().toString();
                if (nombreEmpresa.equals("")) {
                    builder.setMessage("Algo salió mal");
                    builder.setMessage("INGRESE ANTES EL NOMBRE DE LA EMPRESA");
                    displayAlert("input_error");
                } else {
                    seleccionarImagen();
                }
            }

        });
        btnInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombreEmpresa = edTxtNombreEmpresa.getText().toString();
                Log.e("ENVIANDO NOMBRE: ", nombreEmpresa);
                if (nombreEmpresa.equals("")) {
                    builder.setMessage("Algo salió mal");
                    builder.setMessage("Debe Ingresar por lo menos el nombre de la Empresa");
                    displayAlert("input_error");
                } else {
                    //enviamos una peticion antes de la insercion -- REVISAR PORQUE SIN ESTO NO INSERTAN LAS IMAGENES
                    cargarDatosRecyclerView();
                    insertarCliente();
                }
            }
        });
    }

    private void insertarCliente() {
        //PARA AGREGAR A LA DB
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ADAPTADOR_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(bitmap!=null) {
                            uploadImage();
                        }else{
                            edTxtNombreEmpresa.setText("");
                            edTxtNombreEmpresa.requestFocus();
                            cargarDatosRecyclerView();
                        }
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
                opcion = "InsertarCliente";
                Map<String, String> params = new HashMap<>();
                params.put("Empresa", nombreEmpresa);
                params.put("Opcion", opcion);
                Log.e("ENVIANDO PETICION: ", "INSERTAR CLIENTE " + opcion);
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Config.ADAPTADOR_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("clientes");
                            clientes.clear();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                Cliente item = new Cliente(
                                        o.getInt("Id_Cliente"),
                                        o.getString("Empresa"),
                                        o.getString("Logo")
                                );
                                clientes.add(item);
                            }
                            rAdapter = new ClientesRecyclerViewAdapter(clientes, getContext());
                            rv.setAdapter(rAdapter);
                            //retornamos a 0 el valor de REGISTRO_CAMBIADO
                            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
                            int modificacion = sharedPref.getInt(Config.REGISTRO_CAMBIADO, 0);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putInt(Config.REGISTRO_CAMBIADO, 0);
                            editor.commit();
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
                opcion = "CargarClientes";
                Map<String, String> params = new HashMap<>();
                params.put("Opcion", opcion);
                Log.e("ENVIANDO PETICION: ", "CARGAR CLIENTES " + opcion);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getContext()).addRequestQueue(stringRequest);
    }

}