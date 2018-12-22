package com.example.witch.gtslsac_app_1.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.witch.gtslsac_app_1.R;
import com.example.witch.gtslsac_app_1.mDatos.Alquiler;
import com.example.witch.gtslsac_app_1.mDatos.AlquileresCRUD;
import com.example.witch.gtslsac_app_1.mDatos.AlquileresCollection;
import com.example.witch.gtslsac_app_1.mDatos.Cliente;
import com.example.witch.gtslsac_app_1.mDatos.ClientesCRUD;
import com.example.witch.gtslsac_app_1.mDatos.ClientesCollection;
import com.example.witch.gtslsac_app_1.mDatos.DetalleAlquiler;
import com.example.witch.gtslsac_app_1.mDatos.DetalleAlquileresCollection;
import com.example.witch.gtslsac_app_1.mDatos.DetalleAlquileresCRUD;
import com.example.witch.gtslsac_app_1.mRecycler.AlquilerDetalleRecyclerViewAdapter;
import com.example.witch.gtslsac_app_1.mVolley.Config;
import com.example.witch.gtslsac_app_1.mVolley.OperacionesVolley;

import java.util.ArrayList;


public class Alquiler_fragment extends Fragment {
    Button btnSeleccionarCliente, btnDetalle, btnConfirmarAlquiler;
    EditText edtxtObservacionAlquiler;
    TextView txtLugar;
    ScrollView scrollView;
    CheckBox chkObservacionAlquiler;

    private RecyclerView rv;
    private AlquilerDetalleRecyclerViewAdapter rAdapter;

    ClientesCRUD clientesCrud = new ClientesCRUD(ClientesCollection.getClientes());
    AlquileresCRUD alquilerCrud = new AlquileresCRUD(AlquileresCollection.getAlquileres());
    DetalleAlquileresCRUD detalleAlquileresCRUD = new DetalleAlquileresCRUD(DetalleAlquileresCollection.getDetalleAlquileres());

    ArrayList<Alquiler> alquileres;
    ArrayList<Cliente> clientes;
    ArrayList<DetalleAlquiler> detalleAlquileres;

    ArrayAdapter<Cliente> adaptador;

    Alquiler alquiler = new Alquiler();

    int idCliente = 0, idUsuario=0;
    String nombreEmpresa, lugarAlquiler;

    AutoCompleteTextView autoCompleteTextView;
    OperacionesVolley operacionesVolley = new OperacionesVolley();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.alquiler_fragment, container, false);
        Log.e("EVENTO:::::::::::::>", "ALQUILER_FRAGMENT ONCREATEVIEW LLAMADO!!");
        operacionesVolley.cargarDatosClienteRecyclerView(getContext());
        operacionesVolley.cargarDatosEquiposRecyclerView(getContext());
        operacionesVolley.cargarDatosOperadoresRecyclerView(getContext());

        Log.e("TAMAÑO VOLLEY CLIENTES", String.valueOf(operacionesVolley.clientes.size()));
        Log.e("TAMAÑO VOLLEY EQUIPOS", String.valueOf(operacionesVolley.equipos.size()));
        Log.e("TAMAÑO VOLLEY OPERADOR", String.valueOf(operacionesVolley.operadores.size()));

        //ACTUALIZAMOS LAS VARIABLES SHAREDPREFERENCES PARA INFORMAR QUE ESTAMOS EN PROCESO DE UN NUEVO ALQUILER
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(Config.PROCESO_DE_ALQUILER, 1);
        editor.putInt(Config.ID_EQUIPO_ALQUILER, 0);
        editor.putInt(Config.ID_TRACTO_ALQUILER, 0);
        editor.putInt(Config.ID_OPERADOR_ALQUILER, 0);
        editor.putInt(Config.ID_AYUDANTE_ALQUILER, 0);
        editor.putString(Config.NOMBRE_EQUIPO_ALQUILER, "");
        editor.putString(Config.NOMBRE_TRACTO_ALQUILER, "");
        editor.putString(Config.NOMBRE_OPERADOR_ALQUILER, "");
        editor.putString(Config.NOMBRE_AYUDANTE_ALQUILER, "");
        editor.commit();

        scrollView = (ScrollView) myView.findViewById(R.id.scrollAlquiler);
        scrollView.setVisibility(View.GONE);

        rv = (RecyclerView) myView.findViewById(R.id.recicladorAlquilerDetalle);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setItemAnimator(new DefaultItemAnimator());

        detalleAlquileres = new ArrayList<>();
        detalleAlquileres = detalleAlquileresCRUD.getDetalleAlquileres();

        for (int i = 0; i < detalleAlquileres.size(); i++) {
            Log.e("DETALLE_ALQUILER:::>>>>", "ID_DETALLE: " + detalleAlquileres.get(i).getIdDetalleAlquiler() + " ID EQUIPO: " + detalleAlquileres.get(i).getIdEquipo() + " ID TRACTO: " + detalleAlquileres.get(i).getIdEquipo2()
                    + " ID OPERADOR: " + detalleAlquileres.get(i).getIdOperador() + " ID AYUDANTE: " + detalleAlquileres.get(i).getIdOperador2() + " FECHA INICIO: " + detalleAlquileres.get(i).getFechaInicio()
                    + " FECHA FIN: " + detalleAlquileres.get(i).getFechaFin() + " Observación Detalle: " + detalleAlquileres.get(i).getObservacionDetalle());
        }
        clientes = new ArrayList<>();
        clientes = clientesCrud.getClientes();
        alquileres = new ArrayList<>();
        alquileres = alquilerCrud.getAlquileres();


        btnSeleccionarCliente = (Button) myView.findViewById(R.id.btnSeleccionarClienteAlquiler);
        btnDetalle = (Button) myView.findViewById(R.id.btn_detalle_alquiler);
        btnConfirmarAlquiler= (Button) myView.findViewById(R.id.btnConfirmarAlquiler);
        btnConfirmarAlquiler.setVisibility(View.GONE);
        chkObservacionAlquiler = (CheckBox) myView.findViewById(R.id.chk_observacion_alquiler);
        chkObservacionAlquiler.setChecked(false);
        chkObservacionAlquiler.setVisibility(View.GONE);
        edtxtObservacionAlquiler = (EditText) myView.findViewById(R.id.edtxt_observacion_alquiler);
        edtxtObservacionAlquiler.setVisibility(View.GONE);


        txtLugar = (TextView) myView.findViewById(R.id.EditTxtLugar);
        txtLugar.setVisibility(View.GONE);

        autoCompleteTextView = (AutoCompleteTextView) myView.findViewById(R.id.autocompleteAlquilerClientes);
        autoCompleteTextView.setThreshold(1);
        adaptador = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, operacionesVolley.clientes);
        autoCompleteTextView.setAdapter(adaptador);

        if (detalleAlquileres.size() == 0) {

        } else {
            // Crear un nuevo adaptador
            txtLugar.setVisibility(View.VISIBLE);
            chkObservacionAlquiler.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.VISIBLE);
            btnConfirmarAlquiler.setVisibility(View.VISIBLE);
            rAdapter = new AlquilerDetalleRecyclerViewAdapter(detalleAlquileres, getContext());
            rv.setAdapter(rAdapter);
        }
        //SI EL BUNDLE NO ESTÁ VACIO, ES DECIR, SI YA SE SELECCIONÓ LA EMPRESA DESDE EL RECYCLERVIEW
        Bundle b = this.getArguments();
        if (b != null) {
            idCliente = b.getInt("idEmpresa");
            Log.e("ID CLIENTE SELECCIONADO", String.valueOf(idCliente));
            Log.e("DATOS CLIENTE", "ID: " + clientes.get(0).getIdEmpresa() + " EMPRESA: " + clientes.get(0).getNombreEmpresa() + " LOGO: " + clientes.get(0).getLogoEmpresa());
            nombreEmpresa = clientes.get(0).getNombreEmpresa();
            autoCompleteTextView.setText(nombreEmpresa);
            autoCompleteTextView.setFocusable(false);

        }
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Object item = parent.getItemAtPosition(position);
                if (item instanceof Cliente) {
                    Cliente cliente = (Cliente) item;
                    idCliente = cliente.getIdEmpresa();
                    nombreEmpresa = cliente.getNombreEmpresa();
                }
                txtLugar.setVisibility(View.VISIBLE);
                Log.e("ID CLIENTE", String.valueOf(idCliente));
                Log.e("HAY ", String.valueOf(operacionesVolley.clientes.size()) + " CLIENTES");
                autoCompleteTextView.setText(nombreEmpresa);
                autoCompleteTextView.setFocusable(false);
            }
        });
        btnSeleccionarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //txtLugar.setVisibility(View.GONE);
                Clientes_fragment fragmentClientes = new Clientes_fragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.contenedor, fragmentClientes);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        btnDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ID CLIENTE", String.valueOf(idCliente));
                if (idCliente == 0) {
                    Toast.makeText(getActivity(), "AUN NO HA SELECCIONADO UN CLIENTE", Toast.LENGTH_LONG).show();
                } else {
                    //PARA LLAMAR AL FRAGMENT CLIENTES
                    lugarAlquiler = txtLugar.getText().toString();
                    Log.e("LUGAR", String.valueOf(lugarAlquiler));

                    alquiler.setIdCliente(idCliente);
                    alquiler.setLugarAlquiler(lugarAlquiler);
                    alquilerCrud.addNew(alquiler);

                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt(Config.PROCESO_DE_ALQUILER, 1);
                    editor.putInt(Config.ID_EQUIPO_ALQUILER, 0);
                    editor.putInt(Config.ID_TRACTO_ALQUILER, 0);
                    editor.putInt(Config.ID_OPERADOR_ALQUILER, 0);
                    editor.putInt(Config.ID_AYUDANTE_ALQUILER, 0);
                    editor.putString(Config.NOMBRE_EQUIPO_ALQUILER, "");
                    editor.putString(Config.NOMBRE_TRACTO_ALQUILER, "");
                    editor.putString(Config.NOMBRE_OPERADOR_ALQUILER, "");
                    editor.putString(Config.NOMBRE_AYUDANTE_ALQUILER, "");
                    editor.commit();

                    //PARA LLAMAR AL FRAGMENT DETALLE
                    Alquiler_Detalle_fragment fragmentDetalle = new Alquiler_Detalle_fragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.contenedor, fragmentDetalle);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
            }
        });
        chkObservacionAlquiler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chkObservacionAlquiler.isChecked()) {
                    edtxtObservacionAlquiler.setVisibility(View.VISIBLE);

                } else {
                    edtxtObservacionAlquiler.setVisibility(View.GONE);
                }
            }
        });
        btnConfirmarAlquiler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lugarAlquiler = txtLugar.getText().toString();
                SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                idUsuario = sharedPreferences1.getInt(Config.ID_USUARIO_ALQUILER, 0);
                Log.e("LUGAR", String.valueOf(lugarAlquiler) + " ID USUARIO: " + idUsuario);
                if (lugarAlquiler.equals("")) {
                    Toast.makeText(getContext(), "POR FAVOR SELECCIONE UN LUGAR PARA EL ALQUILER", Toast.LENGTH_LONG).show();
                } else {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    operacionesVolley.insertarAlquiler(getContext(),idUsuario,idCliente,lugarAlquiler,"");
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    //BOTON "NO" PRESIONADO
                                    break;
                            }
                        }

                    };
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                    builder.setMessage("PROCEDER CON ALQUILER?").setPositiveButton("SI", dialogClickListener).setNegativeButton("NO", dialogClickListener).show();
                }

            }
        });
        return myView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
