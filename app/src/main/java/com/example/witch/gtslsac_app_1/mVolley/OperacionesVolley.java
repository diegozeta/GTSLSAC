package com.example.witch.gtslsac_app_1.mVolley;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.witch.gtslsac_app_1.Activities.MainActivity;
import com.example.witch.gtslsac_app_1.Fragments.Alquiler_fragment;
import com.example.witch.gtslsac_app_1.Fragments.Mantenimiento_fragment;
import com.example.witch.gtslsac_app_1.R;
import com.example.witch.gtslsac_app_1.mDatos.Cliente;
import com.example.witch.gtslsac_app_1.mDatos.ClientesCRUD;
import com.example.witch.gtslsac_app_1.mDatos.ClientesCollection;
import com.example.witch.gtslsac_app_1.mDatos.DetalleAlquiler;
import com.example.witch.gtslsac_app_1.mDatos.DetalleAlquileresCRUD;
import com.example.witch.gtslsac_app_1.mDatos.DetalleAlquileresCollection;
import com.example.witch.gtslsac_app_1.mDatos.Equipo;
import com.example.witch.gtslsac_app_1.mDatos.EquiposCRUD;
import com.example.witch.gtslsac_app_1.mDatos.EquiposCollection;
import com.example.witch.gtslsac_app_1.mDatos.Operador;
import com.example.witch.gtslsac_app_1.mDatos.OperadoresCRUD;
import com.example.witch.gtslsac_app_1.mDatos.OperadoresCollection;
import com.example.witch.gtslsac_app_1.mDatos.ReporteNotificacion;
import com.example.witch.gtslsac_app_1.mDatos.ReporteNotificacionCRUD;
import com.example.witch.gtslsac_app_1.mDatos.ReporteNotificacionCollection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by witch on 26/06/2017.
 */

public class OperacionesVolley {
    private String opcion;
    private int id_alquiler;

    DetalleAlquileresCRUD detalleAlquileresCRUD = new DetalleAlquileresCRUD(DetalleAlquileresCollection.getDetalleAlquileres());
    public ArrayList<DetalleAlquiler> detalleAlquileres = new ArrayList<>();

    ReporteNotificacionCRUD reporteNotificacionCRUD = new ReporteNotificacionCRUD(ReporteNotificacionCollection.getReporteNotificaciones());
    public ArrayList<ReporteNotificacion> reporteNotificaciones = new ArrayList<>();

    ClientesCRUD clientesCRUD = new ClientesCRUD(ClientesCollection.getClientes());
    public ArrayList<Cliente> clientes = new ArrayList<>();

    EquiposCRUD equiposCRUD = new EquiposCRUD(EquiposCollection.getEquipos());
    public ArrayList<Equipo> equipos = new ArrayList<>();

    OperadoresCRUD operadoresCRUD = new OperadoresCRUD(OperadoresCollection.getOperadores());
    public ArrayList<Operador> operadores = new ArrayList<>();

    public void cargarDatosClienteRecyclerView(final Context context) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
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
                            JSONArray array = jsonObject.getJSONArray("clientes");
                            clientes.clear();
                            clientesCRUD.deleteAll();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                Cliente item = new Cliente(
                                        o.getInt("Id_Cliente"),
                                        o.getString("Empresa"),
                                        o.getString("Logo")
                                );
                                clientes.add(item);
                                clientesCRUD.addNew(item);
                            }
                            //retornamos a 0 el valor de REGISTRO_CAMBIADO
                            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
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
        stringRequest1.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addRequestQueue(stringRequest1);
    }

    public void cargarDatosEquiposRecyclerView(final Context context) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
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
                            equiposCRUD.deleteAll();
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
                                equiposCRUD.addNew(item);
                            }
                            //retornamos a 0 el valor de REGISTRO_CAMBIADO
                            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
                            int modificacion = sharedPref.getInt(Config.REGISTRO_CAMBIADO, 0);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putInt(Config.REGISTRO_CAMBIADO, 0);
                            editor.commit();
                            modificacion = sharedPref.getInt(Config.REGISTRO_CAMBIADO, 0);
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
        MySingleton.getInstance(context).addRequestQueue(stringRequest1);
    }

    public void cambiarDisponibilidad(final Context context, final int id, final int estado, final boolean esEquipo) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ADAPTADOR_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String codigo = jsonObject.getString("codigo");
                            String mensaje = jsonObject.getString("mensaje");
                            Log.e("MENSAJE", mensaje);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                if (esEquipo) {
                    opcion = "ActualizarDisponibilidadEquipo";
                    params.put("Id_Equipo", String.valueOf(id));
                    params.put("Estado", String.valueOf(estado));
                    params.put("Opcion", opcion);
                } else {
                    opcion = "actualizarDisponibilidadOperador";
                    params.put("Id_Operador", String.valueOf(id));
                    params.put("Estado", String.valueOf(estado));
                    params.put("Opcion", opcion);
                }
                return params;
            }
        };
        MySingleton.getInstance(context).addRequestQueue(stringRequest);
    }

    public void cargarDatosOperadoresRecyclerView(final Context context) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
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
                            operadoresCRUD.deleteAll();
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
                                operadoresCRUD.addNew(item);
                            }
                            //retornamos a 0 el valor de REGISTRO_CAMBIADO
                            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
                            int modificacion = sharedPref.getInt(Config.REGISTRO_CAMBIADO, 0);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putInt(Config.REGISTRO_CAMBIADO, 0);
                            editor.commit();
                            modificacion = sharedPref.getInt(Config.REGISTRO_CAMBIADO, 0);
                            Log.e("DESPUES DE CARGAR DATOS", String.valueOf(modificacion));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(context, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
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
        MySingleton.getInstance(context).addRequestQueue(stringRequest1);
    }

    public void cargarDatosDetalleAlquileresRecyclerView(final Context context) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
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
                            JSONArray array = jsonObject.getJSONArray("detalle_alquiler");
                            detalleAlquileres.clear();
                            detalleAlquileresCRUD.deleteAll();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                DetalleAlquiler item = new DetalleAlquiler(
                                        o.getInt("Id_Detalle_Alquiler"),
                                        o.getInt("Id_Alquiler"),
                                        o.getInt("Id_Usuario"),
                                        o.getInt("Id_Equipo"),
                                        o.getInt("Id_Equipo2"),
                                        o.getInt("Id_Operador"),
                                        o.getInt("Id_Operador2"),
                                        o.getInt("Id_Cliente"),
                                        o.getString("Fecha_Inicio"),
                                        o.getString("Fecha_Fin"),
                                        o.getString("Observacion_Detalle")
                                );
                                detalleAlquileres.add(item);
                                detalleAlquileresCRUD.addNew(item);
                            }
                            //retornamos a 0 el valor de REGISTRO_CAMBIADO
                            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
                            int modificacion = sharedPref.getInt(Config.REGISTRO_CAMBIADO, 0);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putInt(Config.REGISTRO_CAMBIADO, 0);
                            editor.commit();
                            modificacion = sharedPref.getInt(Config.REGISTRO_CAMBIADO, 0);
                            Log.e("DESPUES DE CARGAR DATOS", String.valueOf(modificacion));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(context, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                opcion = "CargarDetalleAlquileres";
                Map<String, String> params = new HashMap<String, String>();
                params.put("Opcion", opcion);
                return params;
            }
        };
        stringRequest1.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addRequestQueue(stringRequest1);
    }

    public void cargarDatosReporteNotificacionRecyclerView(final Context context, final String IdAlquiler) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
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
                            JSONArray array = jsonObject.getJSONArray("reporte_notificacion");
                            reporteNotificaciones.clear();
                            reporteNotificacionCRUD.deleteAll();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                ReporteNotificacion item = new ReporteNotificacion(
                                        o.getInt("Id_Detalle_Alquiler"),
                                        o.getInt("Id_Alquiler"),
                                        o.getString("UsuarioNombres"),
                                        o.getString("UsuarioApellidos"),
                                        o.getString("Empresa"),
                                        o.getString("NombreEquipo1"),
                                        o.getString("MarcaEquipo1"),
                                        o.getString("ModeloEquipo1"),
                                        o.getString("CodigoEquipo1"),
                                        o.getString("NombreEquipo2"),
                                        o.getString("MarcaEquipo2"),
                                        o.getString("ModeloEquipo2"),
                                        o.getString("CodigoEquipo2"),
                                        o.getString("NombresOperador1"),
                                        o.getString("ApellidosOperador1"),
                                        o.getString("NombresOperador2"),
                                        o.getString("ApellidosOperador2"),
                                        o.getString("Fecha_Inicio"),
                                        o.getString("Fecha_Fin")

                                );
                                reporteNotificaciones.add(item);
                                reporteNotificacionCRUD.addNew(item);
                            }
                            //retornamos a 0 el valor de REGISTRO_CAMBIADO
                            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
                            int modificacion = sharedPref.getInt(Config.REGISTRO_CAMBIADO, 0);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putInt(Config.REGISTRO_CAMBIADO, 0);
                            editor.commit();
                            modificacion = sharedPref.getInt(Config.REGISTRO_CAMBIADO, 0);
                            Log.e("DESPUES DE CARGAR DATOS", String.valueOf(modificacion));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(context, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                opcion = "CargarReporteNotificacion";
                Map<String, String> params = new HashMap<String, String>();
                params.put("Opcion", opcion);
                params.put("Id_Alquiler", IdAlquiler);
                return params;
            }
        };
        stringRequest1.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addRequestQueue(stringRequest1);
    }


    public void insertarAlquiler(final Context context, final int idUsuario, final int idCliente, final String lugar, final String observacionAlquiler) {
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(context, "GUARDANDO ALQUILER...", "POR FAVOR ESPERE...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ADAPTADOR_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        //Showing toast message of the response
                        //Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
                        Log.e("RESPUESTA ALQUILER: ", s);
                        try {
                            JSONArray jsonArray = new JSONArray(s);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String codigo = jsonObject.getString("codigo");
                            id_alquiler = jsonObject.getInt("id_alquiler");
                            String mensaje = jsonObject.getString("mensaje");
                            if (codigo.equals("reg_failed")) {
                                Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show();
                                detalleAlquileres = new ArrayList<>();
                                detalleAlquileres = detalleAlquileresCRUD.getDetalleAlquileres();
                                for (int i = 0; i < detalleAlquileres.size(); i++) {
                                    insertarDetalleAlquiler(context, id_alquiler, idUsuario, idCliente, detalleAlquileres.get(i).getIdEquipo(), detalleAlquileres.get(i).getIdEquipo2(), detalleAlquileres.get(i).getIdOperador(), detalleAlquileres.get(i).getIdOperador2(), detalleAlquileres.get(i).getFechaInicio().toString(), detalleAlquileres.get(i).getFechaFin().toString(), detalleAlquileres.get(i).getObservacionDetalle());
                                    if (detalleAlquileres.get(i).getIdEquipo() != 0)
                                        cambiarDisponibilidad(context, detalleAlquileres.get(i).getIdEquipo(), 0, true);
                                    if (detalleAlquileres.get(i).getIdEquipo2() != 0)
                                        cambiarDisponibilidad(context, detalleAlquileres.get(i).getIdEquipo2(), 0, true);
                                    if (detalleAlquileres.get(i).getIdOperador() != 0)
                                        cambiarDisponibilidad(context, detalleAlquileres.get(i).getIdOperador(), 0, false);
                                    if (detalleAlquileres.get(i).getIdOperador2() != 0)
                                        cambiarDisponibilidad(context, detalleAlquileres.get(i).getIdOperador2(), 0, false);
                                }
                                detalleAlquileres.clear();
                                detalleAlquileresCRUD.deleteAll();
                                //RECARGAR EL FRAGMENT
                                //((MainActivity) context).getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                                Alquiler_fragment alquiler_fragment = new Alquiler_fragment();
                                ((MainActivity) context).getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, alquiler_fragment).commit();

                                //Disimissing the progress dialog
                                loading.dismiss();
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
                Map<String, String> params = new Hashtable<>();
                //Adding parameters
                params.put("Id_Usuario", String.valueOf(idUsuario));
                params.put("Id_Cliente", String.valueOf(idCliente));
                params.put("Lugar", lugar);
                params.put("Observacion_Alquiler", observacionAlquiler);
                params.put("Opcion", ("InsertarAlquiler"));
                //returning parameters
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addRequestQueue(stringRequest);
    }

    public void cargarDatosReporteOperadoresRecyclerView(final Context context, final String IdOperador, final String FechaDesde, final String FechaHasta) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Cargando Informacion...");
        progressDialog.show();
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST,
                Config.ADAPTADOR_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("reporte_operador");
                            reporteNotificaciones.clear();
                            reporteNotificacionCRUD.deleteAll();
                            if (array.length()>0) {
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject o = array.getJSONObject(i);
                                    ReporteNotificacion item = new ReporteNotificacion(
                                            o.getInt("Id_Detalle_Alquiler"),
                                            o.getInt("Id_Alquiler"),
                                            o.getString("UsuarioNombres"),
                                            o.getString("UsuarioApellidos"),
                                            o.getString("Empresa"),
                                            o.getString("NombreEquipo1"),
                                            o.getString("MarcaEquipo1"),
                                            o.getString("ModeloEquipo1"),
                                            o.getString("CodigoEquipo1"),
                                            o.getString("NombreEquipo2"),
                                            o.getString("MarcaEquipo2"),
                                            o.getString("ModeloEquipo2"),
                                            o.getString("CodigoEquipo2"),
                                            o.getString("NombresOperador1"),
                                            o.getString("ApellidosOperador1"),
                                            o.getString("NombresOperador2"),
                                            o.getString("ApellidosOperador2"),
                                            o.getString("Fecha_Inicio"),
                                            o.getString("Fecha_Fin")
                                    );
                                    reporteNotificaciones.add(item);
                                    reporteNotificacionCRUD.addNew(item);
                                    progressDialog.dismiss();
                                }
                            }else{
                                progressDialog.dismiss();
                            }
                            Log.e("CARGARON: ", String.valueOf(reporteNotificaciones.size() + " ITEMS PARA DICHO REPORTE"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(context, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                opcion = "CargarReporteOperador";
                Map<String, String> params = new HashMap<String, String>();
                params.put("Opcion", opcion);
                params.put("Id_Operador", IdOperador);
                params.put("Fecha_Inicio", FechaDesde);
                params.put("Fecha_Fin", FechaHasta);
                return params;
            }
        };
        stringRequest1.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addRequestQueue(stringRequest1);
    }


    public void cargarDatosReporteEquiposRecyclerView(final Context context, final String IdEquipo, final String FechaDesde, final String FechaHasta) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Cargando Informacion...");
        progressDialog.show();
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST,
                Config.ADAPTADOR_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("reporte_equipo");
                            reporteNotificaciones.clear();
                            reporteNotificacionCRUD.deleteAll();
                            if (array.length()>0) {
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject o = array.getJSONObject(i);
                                    ReporteNotificacion item = new ReporteNotificacion(
                                            o.getInt("Id_Detalle_Alquiler"),
                                            o.getInt("Id_Alquiler"),
                                            o.getString("UsuarioNombres"),
                                            o.getString("UsuarioApellidos"),
                                            o.getString("Empresa"),
                                            o.getString("NombreEquipo1"),
                                            o.getString("MarcaEquipo1"),
                                            o.getString("ModeloEquipo1"),
                                            o.getString("CodigoEquipo1"),
                                            o.getString("NombreEquipo2"),
                                            o.getString("MarcaEquipo2"),
                                            o.getString("ModeloEquipo2"),
                                            o.getString("CodigoEquipo2"),
                                            o.getString("NombresOperador1"),
                                            o.getString("ApellidosOperador1"),
                                            o.getString("NombresOperador2"),
                                            o.getString("ApellidosOperador2"),
                                            o.getString("Fecha_Inicio"),
                                            o.getString("Fecha_Fin")
                                    );
                                    reporteNotificaciones.add(item);
                                    reporteNotificacionCRUD.addNew(item);
                                    progressDialog.dismiss();
                                }
                            }else{
                                progressDialog.dismiss();
                            }
                            Log.e("CARGARON: ", String.valueOf(reporteNotificaciones.size() + " ITEMS PARA DICHO REPORTE"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(context, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                opcion = "CargarReporteEquipo";
                Map<String, String> params = new HashMap<String, String>();
                params.put("Opcion", opcion);
                params.put("Id_Equipo", IdEquipo);
                params.put("Fecha_Inicio", FechaDesde);
                params.put("Fecha_Fin", FechaHasta);
                return params;
            }
        };
        stringRequest1.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addRequestQueue(stringRequest1);
    }
    private void insertarDetalleAlquiler(final Context context, final int idAlquiler, final int idUsuario, final int idCliente, final int idEquipo, final int idEquipo2, final int idOperador, final int idOperador2, final String fechaInicio, final String fechaFin, final String observacionDetalle) {
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(context, "GUARDANDO DETALLE ALQUILER...", "POR FAVOR ESPERE...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ADAPTADOR_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        //Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
                        Log.e("RPTA DETALLE_ALQUILER: ", s);
                        try {
                            JSONArray jsonArray = new JSONArray(s);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String codigo = jsonObject.getString("codigo");
                            String mensaje = jsonObject.getString("mensaje");
                            if (codigo.equals("reg_failed")) {
                                Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show();
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
                Map<String, String> params = new Hashtable<>();
                params.put("Id_Alquiler", String.valueOf(idAlquiler));
                params.put("Id_Usuario", String.valueOf(idUsuario));
                params.put("Id_Cliente", String.valueOf(idCliente));
                params.put("Id_Equipo", String.valueOf(idEquipo));
                params.put("Id_Equipo2", String.valueOf(idEquipo2));
                params.put("Id_Operador", String.valueOf(idOperador));
                params.put("Id_Operador2", String.valueOf(idOperador2));
                params.put("Fecha_Inicio", String.valueOf(fechaInicio));
                params.put("Fecha_Fin", String.valueOf(fechaFin));
                params.put("Observacion_Detalle", String.valueOf(observacionDetalle));
                params.put("Opcion", ("InsertarDetalleAlquiler"));
                //returning parameters
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addRequestQueue(stringRequest);
    }

    public void insertarMantenimiento(final Context context, final int idEquipo, final int idUsuario, final String fechaEntrada, final String fechaSalida) {
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(context, "GUARDANDO MANTENIMIENTO...", "POR FAVOR ESPERE...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ADAPTADOR_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        //Showing toast message of the response
                        //Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
                        Log.e("RESPUESTA MANTENIMIENTO", s);
                        try {
                            JSONArray jsonArray = new JSONArray(s);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String codigo = jsonObject.getString("codigo");
                            String mensaje = jsonObject.getString("mensaje");
                            if (codigo.equals("reg_failed")) {
                                Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show();
                                Mantenimiento_fragment mantenimiento_fragment = new Mantenimiento_fragment();
                                ((MainActivity) context).getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, mantenimiento_fragment).commit();

                                //Disimissing the progress dialog
                                loading.dismiss();
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
                Map<String, String> params = new Hashtable<>();
                //Adding parameters
                params.put("Id_Equipo", String.valueOf(idEquipo));
                params.put("Id_Usuario", String.valueOf(idUsuario));
                params.put("Fecha_Entrada", fechaEntrada);
                params.put("Fecha_Salida", fechaSalida);
                params.put("Opcion", ("InsertarMantenimiento"));
                //returning parameters
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addRequestQueue(stringRequest);
    }

    public void insertarToken(final Context context, final int idUsuario, final String token) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ADAPTADOR_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        //Showing toast message of the response
                        //Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
                        Log.e("RESPUESTA TOKEN", s);
                        try {
                            JSONArray jsonArray = new JSONArray(s);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String codigo = jsonObject.getString("codigo");
                            String mensaje = jsonObject.getString("mensaje");
                            if (codigo.equals("reg_failed")) {
                                Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show();
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
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<>();
                //Adding parameters
                params.put("Fcm_Token", token);
                params.put("Id_Usuario", String.valueOf(idUsuario));
                params.put("Opcion", ("IsertarFirebaseToken"));
                //returning parameters
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addRequestQueue(stringRequest);
    }
}
