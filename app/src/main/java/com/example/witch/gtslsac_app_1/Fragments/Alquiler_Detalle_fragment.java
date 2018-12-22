package com.example.witch.gtslsac_app_1.Fragments;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.witch.gtslsac_app_1.R;
import com.example.witch.gtslsac_app_1.mDatos.Alquiler;
import com.example.witch.gtslsac_app_1.mDatos.AlquileresCRUD;
import com.example.witch.gtslsac_app_1.mDatos.AlquileresCollection;
import com.example.witch.gtslsac_app_1.mDatos.DetalleAlquiler;
import com.example.witch.gtslsac_app_1.mDatos.DetalleAlquileresCRUD;
import com.example.witch.gtslsac_app_1.mDatos.DetalleAlquileresCollection;
import com.example.witch.gtslsac_app_1.mDatos.Equipo;
import com.example.witch.gtslsac_app_1.mDatos.EquiposCRUD;
import com.example.witch.gtslsac_app_1.mDatos.EquiposCollection;
import com.example.witch.gtslsac_app_1.mDatos.Operador;
import com.example.witch.gtslsac_app_1.mDatos.OperadoresCRUD;
import com.example.witch.gtslsac_app_1.mDatos.OperadoresCollection;
import com.example.witch.gtslsac_app_1.mVolley.Config;
import com.example.witch.gtslsac_app_1.mVolley.OperacionesVolley;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Alquiler_Detalle_fragment extends Fragment {

    Button btnSeleccionarEquipo, btnSeleccionarTracto, btnSeleccionarOperador, btnSeleccionarAyudante, btnAgregarAlAlquiler, btnCancelar;
    CheckBox chkObservacionDetalle;
    EditText edtxtObservacionDetalle;
    TextView txtFechaInicio, txtFechaFin;
    String opcionFecha;

    String sqlDateInicio, sqlDateFin;

    EquiposCRUD equiposCRUD = new EquiposCRUD(EquiposCollection.getEquipos());
    OperadoresCRUD operadoresCRUD = new OperadoresCRUD(OperadoresCollection.getOperadores());
    DetalleAlquileresCRUD detalleAlquileresCRUD = new DetalleAlquileresCRUD(DetalleAlquileresCollection.getDetalleAlquileres());
    AlquileresCRUD alquilerCrud = new AlquileresCRUD(AlquileresCollection.getAlquileres());

    ArrayList<Alquiler> alquileres;
    ArrayList<DetalleAlquiler> detalleAlquileres;
    ArrayList<Equipo> equipos;
    ArrayList<Operador> operadores;

    ArrayAdapter<Equipo> adaptadorEquipos;
    ArrayAdapter<Equipo> adaptadorTractos;
    ArrayAdapter<Operador> adaptadorOperadores;
    ArrayAdapter<Operador> adaptadorAyudantes;

    DetalleAlquiler detalleAlquiler = new DetalleAlquiler();

    int idEquipo = 0, idTracto = 0, idOperador = 0, idAyudante = 0;
    String nombreEquipo, nombreTracto, nombreOperador, nombreAyudante;

    AutoCompleteTextView autoCompleteTextViewEquipos, autoCompleteTextViewOperadores, autoCompleteTextViewTractos, autoCompleteTextViewAyudantes;
    OperacionesVolley operacionesVolley = new OperacionesVolley();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.alquiler_detalle_fragment, container, false);
        Log.e("EVENTO:::::::::::::>", "ALQUILER_DETALLE_FRAGMENT ONCREATEVIEW LLAMADO!!");
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String opcion = sharedPref.getString(Config.OPCION_DETALLE_ALQUILER, "");


        alquileres = new ArrayList<>();
        alquileres = alquilerCrud.getAlquileres();

        detalleAlquileres = new ArrayList<>();
        detalleAlquileres = detalleAlquileresCRUD.getDetalleAlquileres();

        equipos = new ArrayList<>();
        equipos = equiposCRUD.getEquipos();

        operadores = new ArrayList<>();
        operadores = operadoresCRUD.getOperadores();

        operacionesVolley.cargarDatosOperadoresRecyclerView(getContext());
        operacionesVolley.cargarDatosEquiposRecyclerView(getContext());

        Log.e("INFORMACION ALQUILER", "DETALLE PERTENECE AL ALQUILER: " + "ID CLIENTE: " + alquileres.get(0).getIdCliente() + " LUGAR " + alquileres.get(0).getLugarAlquiler());

        btnSeleccionarEquipo = (Button) myView.findViewById(R.id.btn_seleccionar_equipo_alquiler);
        btnSeleccionarTracto = (Button) myView.findViewById(R.id.btn_seleccionar_tracto_alquiler);
        btnSeleccionarOperador = (Button) myView.findViewById(R.id.btn_seleccionar_operador_alquiler);
        btnSeleccionarAyudante = (Button) myView.findViewById(R.id.btn_seleccionar_ayudante_alquiler);
        /*btnFechaInicio = (Button) myView.findViewById(R.id.btn_fecha_inicio);
        btnFechaFin = (Button) myView.findViewById(R.id.btn_fecha_fin);*/

        btnAgregarAlAlquiler = (Button) myView.findViewById(R.id.btn_agregar_detalle_alquiler);
        btnCancelar = (Button) myView.findViewById(R.id.btn_cancelar_detalle_alquiler);

        txtFechaInicio = (TextView) myView.findViewById(R.id.txt_fecha_inicio);
        txtFechaFin = (TextView) myView.findViewById(R.id.txt_fecha_fin);

        chkObservacionDetalle = (CheckBox) myView.findViewById(R.id.chk_observacion_detalle);
        chkObservacionDetalle.setChecked(false);
        edtxtObservacionDetalle = (EditText) myView.findViewById(R.id.edtxt_observacion_Detalle);
        edtxtObservacionDetalle.setVisibility(View.GONE);

        autoCompleteTextViewEquipos = (AutoCompleteTextView) myView.findViewById(R.id.autocompleteAlquilerEquipos);
        autoCompleteTextViewEquipos.setThreshold(1);
        adaptadorEquipos = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, operacionesVolley.equipos);
        autoCompleteTextViewEquipos.setAdapter(adaptadorEquipos);

        autoCompleteTextViewTractos = (AutoCompleteTextView) myView.findViewById(R.id.autocompleteAlquilerTracto);
        autoCompleteTextViewTractos.setThreshold(1);
        adaptadorTractos = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, operacionesVolley.equipos);
        autoCompleteTextViewTractos.setAdapter(adaptadorTractos);

        operacionesVolley.cargarDatosOperadoresRecyclerView(getContext());
        autoCompleteTextViewOperadores = (AutoCompleteTextView) myView.findViewById(R.id.autocompleteAlquilerOperador);
        autoCompleteTextViewOperadores.setThreshold(1);
        adaptadorOperadores = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, operacionesVolley.operadores);
        autoCompleteTextViewOperadores.setAdapter(adaptadorOperadores);

        autoCompleteTextViewAyudantes = (AutoCompleteTextView) myView.findViewById(R.id.autocompleteAlquilerAyudante);
        autoCompleteTextViewAyudantes.setThreshold(1);
        adaptadorAyudantes = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, operacionesVolley.operadores);
        autoCompleteTextViewAyudantes.setAdapter(adaptadorAyudantes);

        int equipoSeleccionado = sharedPref.getInt(Config.ID_EQUIPO_ALQUILER, 0);
        int tractoSeleccionado = sharedPref.getInt(Config.ID_TRACTO_ALQUILER, 0);
        int operadorSeleccionado = sharedPref.getInt(Config.ID_OPERADOR_ALQUILER, 0);
        int ayudanteSeleccionado = sharedPref.getInt(Config.ID_AYUDANTE_ALQUILER, 0);

        //ESTABLECEMOS LOS VALORES SI HAN SIDO AÑADIDOS ANTERIORMENTE
        if (equipoSeleccionado != 0)
            autoCompleteTextViewEquipos.setText(sharedPref.getString(Config.NOMBRE_EQUIPO_ALQUILER, ""));
        if (tractoSeleccionado != 0)
            autoCompleteTextViewTractos.setText(sharedPref.getString(Config.NOMBRE_TRACTO_ALQUILER, ""));
        if (operadorSeleccionado != 0)
            autoCompleteTextViewOperadores.setText(sharedPref.getString(Config.NOMBRE_OPERADOR_ALQUILER, ""));
        if (ayudanteSeleccionado != 0)
            autoCompleteTextViewAyudantes.setText(sharedPref.getString(Config.NOMBRE_AYUDANTE_ALQUILER, ""));
        //SI EL BUNDLE NO ESTÁ VACIO, ES DECIR, SI YA SE SELECCIONÓ EL EQUIPO DESDE EL RECYCLERVIEW
        Bundle b = this.getArguments();
        if (b != null) {
            switch (opcion) {
                case "Equipo":
                    idEquipo = b.getInt("idEquipo");
                    Log.e("ID EQUIPO SELECCIONADO", String.valueOf(idEquipo));
                    Log.e("TAMAÑO DE EQUIPOS", String.valueOf(equipos.size()));
                    for (int i = 0; i < equipos.size(); i++) {
                        if (idEquipo == equipos.get(i).getIdEquipo()) {
                            Log.e("SE ENCONTRÓ EL EQUIPO", String.valueOf(idEquipo));
                            nombreEquipo = equipos.get(i).getNombreEquipo() + " " + equipos.get(i).getModeloEquipo()+ " " + equipos.get(i).getMarcaEquipo();
                            //GUARDAMOS EL ID DEL EQUIPO
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putInt(Config.ID_EQUIPO_ALQUILER, idEquipo);
                            editor.putString(Config.NOMBRE_EQUIPO_ALQUILER, nombreEquipo);
                            editor.commit();
                            Log.e("DATOS EQUIPO", "ID: " + equipos.get(i).getIdEquipo() + " EQUIPO: " + equipos.get(i).getNombreEquipo() + " MODELO: " + equipos.get(i).getModeloEquipo());
                        }
                    }
                    autoCompleteTextViewEquipos.setText(nombreEquipo);
                    autoCompleteTextViewEquipos.setFocusable(false);
                    break;
                case "Tracto":
                    idTracto = b.getInt("idEquipo");
                    Log.e("ID TRACTO SELECCIONADO", String.valueOf(idTracto));
                    Log.e("TAMAÑO DE EQUIPOS", String.valueOf(equipos.size()));
                    for (int i = 0; i < equipos.size(); i++) {
                        if (idTracto == equipos.get(i).getIdEquipo()) {
                            Log.e("SE ENCONTRÓ EL EQUIPO", String.valueOf(idTracto));
                            nombreTracto = equipos.get(i).getNombreEquipo() + " " + equipos.get(i).getModeloEquipo()+ " " + equipos.get(i).getMarcaEquipo();
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putInt(Config.ID_TRACTO_ALQUILER, idTracto);
                            editor.putString(Config.NOMBRE_TRACTO_ALQUILER, nombreTracto);
                            editor.commit();
                            Log.e("DATOS EQUIPO", "ID: " + equipos.get(i).getIdEquipo() + " EQUIPO: " + equipos.get(i).getNombreEquipo() + " MODELO: " + equipos.get(i).getModeloEquipo());
                        }
                    }
                    autoCompleteTextViewTractos.setText(nombreTracto);
                    autoCompleteTextViewTractos.setFocusable(false);
                    break;
                case "Operador":
                    idOperador = b.getInt("idOperador");
                    Log.e("ID RECIBIDO", String.valueOf(b.getInt("IdOperador")));
                    Log.e("OPERADOR SELECCIONADO", String.valueOf(idOperador));
                    Log.e("TAMAÑO DE OPERADORES", String.valueOf(operadores.size()));
                    for (int i = 0; i < operadores.size(); i++) {
                        if (idOperador == operadores.get(i).getIdOperador()) {
                            Log.e("SE ENCONTRÓ EL OPERADOR", String.valueOf(idOperador));
                            nombreOperador = operadores.get(i).getNombresOperador()+ " " + operadores.get(i).getApellidosOperador();
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putInt(Config.ID_OPERADOR_ALQUILER, idOperador);
                            editor.putString(Config.NOMBRE_OPERADOR_ALQUILER, nombreOperador);
                            editor.commit();
                            Log.e("DATOS OPERADOR", "ID: " + operadores.get(i).getNombresOperador() + " NOMBRES: " + operadores.get(i).getNombresOperador() + " APELLIDOS: " + operadores.get(i).getApellidosOperador());
                        }
                    }
                    autoCompleteTextViewOperadores.setText(nombreOperador);
                    autoCompleteTextViewOperadores.setFocusable(false);
                    break;
                case "Ayudante":
                    idAyudante = b.getInt("idOperador");
                    Log.e("AYUDANTE SELECCIONADO", String.valueOf(idAyudante));
                    Log.e("TAMAÑO DE AYUDANTES", String.valueOf(operadores.size()));
                    for (int i = 0; i < operadores.size(); i++) {
                        if (idAyudante == operadores.get(i).getIdOperador()) {
                            Log.e("SE ENCONTRÓ EL AYUDANTE", String.valueOf(idAyudante));
                            nombreAyudante = operadores.get(i).getNombresOperador()+ " " + operadores.get(i).getApellidosOperador();
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putInt(Config.ID_AYUDANTE_ALQUILER, idAyudante);
                            editor.putString(Config.NOMBRE_AYUDANTE_ALQUILER, nombreAyudante);
                            editor.commit();
                            Log.e("DATOS AYUDANTE", "ID: " + operadores.get(i).getIdOperador() + " NOMBRES: " + operadores.get(i).getNombresOperador() + " APELLIDOS: " + operadores.get(i).getApellidosOperador());
                        }
                    }
                    autoCompleteTextViewAyudantes.setText(nombreAyudante);
                    autoCompleteTextViewAyudantes.setFocusable(false);
                    break;
            }

        }
        autoCompleteTextViewEquipos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Object item = parent.getItemAtPosition(position);
                if (item instanceof Equipo) {
                    Equipo equipo = (Equipo) item;
                    idEquipo = equipo.getIdEquipo();
                    nombreEquipo = equipo.getNombreEquipo() + " " + equipo.getModeloEquipo()+ " " + equipo.getMarcaEquipo();
                }
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(Config.ID_EQUIPO_ALQUILER, idEquipo);
                editor.putString(Config.NOMBRE_EQUIPO_ALQUILER, nombreEquipo);
                editor.commit();
                Log.e("ID EQUIPO", String.valueOf(idEquipo));
                Log.e("HAY ", String.valueOf(operacionesVolley.equipos.size()) + " EQUIPOS");
                autoCompleteTextViewEquipos.setText(nombreEquipo);
                autoCompleteTextViewEquipos.setFocusable(false);
            }
        });
        autoCompleteTextViewTractos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Object item = parent.getItemAtPosition(position);
                if (item instanceof Equipo) {
                    Equipo tracto = (Equipo) item;
                    idTracto = tracto.getIdEquipo();
                    nombreTracto = tracto.getNombreEquipo()+ " " + tracto.getModeloEquipo()+ " " + tracto.getMarcaEquipo();
                }
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(Config.ID_TRACTO_ALQUILER, idTracto);
                editor.putString(Config.NOMBRE_TRACTO_ALQUILER, nombreTracto);
                editor.commit();
                Log.e("ID TRACTO", String.valueOf(idTracto));
                Log.e("HAY ", String.valueOf(operacionesVolley.equipos.size()) + " TRACTOS");
                autoCompleteTextViewTractos.setText(nombreTracto);
                autoCompleteTextViewTractos.setFocusable(false);
            }
        });
        autoCompleteTextViewOperadores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Object item = parent.getItemAtPosition(position);
                if (item instanceof Operador) {
                    Operador operador = (Operador) item;
                    idOperador = operador.getIdOperador();
                    nombreOperador = operador.getNombresOperador() + " " + operador.getApellidosOperador();
                }
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(Config.ID_OPERADOR_ALQUILER, idOperador);
                editor.putString(Config.NOMBRE_OPERADOR_ALQUILER, nombreOperador);
                editor.commit();
                Log.e("ID OPERADOR", String.valueOf(idOperador));
                Log.e("HAY ", String.valueOf(operacionesVolley.operadores.size()) + " OPERADORES");
                autoCompleteTextViewOperadores.setText(nombreOperador);
                autoCompleteTextViewOperadores.setFocusable(false);
            }
        });
        autoCompleteTextViewAyudantes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                if (item instanceof Operador) {
                    Operador ayudante = (Operador) item;
                    idAyudante = ayudante.getIdOperador();
                    nombreAyudante = ayudante.getNombresOperador()+ " " + ayudante.getApellidosOperador();
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt(Config.ID_AYUDANTE_ALQUILER, idAyudante);
                    editor.putString(Config.NOMBRE_AYUDANTE_ALQUILER, nombreAyudante);
                    editor.commit();
                    Log.e("ID AYUDANTE", String.valueOf(sharedPref.getInt(Config.ID_AYUDANTE_ALQUILER, 0)));
                    Log.e("HAY ", String.valueOf(operacionesVolley.operadores.size()) + " AYUDANTES");
                    autoCompleteTextViewAyudantes.setText(nombreAyudante);
                    autoCompleteTextViewAyudantes.setFocusable(false);
                }
            }
        });
        chkObservacionDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chkObservacionDetalle.isChecked()) {
                    edtxtObservacionDetalle.setVisibility(View.VISIBLE);

                } else {
                    edtxtObservacionDetalle.setVisibility(View.GONE);
                }
            }
        });
        btnSeleccionarEquipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(Config.OPCION_DETALLE_ALQUILER, "Equipo");
                editor.commit();
                seleccionarEquipo();
            }
        });
        btnSeleccionarTracto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(Config.OPCION_DETALLE_ALQUILER, "Tracto");
                editor.commit();
                seleccionarEquipo();
            }
        });
        btnSeleccionarOperador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(Config.OPCION_DETALLE_ALQUILER, "Operador");
                editor.commit();
                seleccionarOperador();
            }
        });
        btnSeleccionarAyudante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(Config.OPCION_DETALLE_ALQUILER, "Ayudante");
                editor.commit();
                seleccionarOperador();
            }
        });
        txtFechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opcionFecha = "Inicio";
                new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        txtFechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opcionFecha = "Fin";
                new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        btnAgregarAlAlquiler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtFechaInicio.getText().equals("FECHA INICIO") || txtFechaFin.getText().equals("FECHA FIN")) {
                    Toast.makeText(getContext(), "INGRESE LA FECHA DE INICIO Y FIN DEL ALQUILER", Toast.LENGTH_LONG).show();
                } else {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    int equipoSeleccionado = sharedPref.getInt(Config.ID_EQUIPO_ALQUILER, 0);
                                    int tractoSeleccionado = sharedPref.getInt(Config.ID_TRACTO_ALQUILER, 0);
                                    int operadorSeleccionado = sharedPref.getInt(Config.ID_OPERADOR_ALQUILER, 0);
                                    int ayudanteSeleccionado = sharedPref.getInt(Config.ID_AYUDANTE_ALQUILER, 0);

                                    Log.e("EQUIPO SELECCIONADO", String.valueOf(equipoSeleccionado));
                                    Log.e("TRACTO SELECCIONADO", String.valueOf(tractoSeleccionado));
                                    Log.e("OPERADOR SELECCIONADO", String.valueOf(operadorSeleccionado));
                                    Log.e("AYUDANTE SELECCIONADO", String.valueOf(ayudanteSeleccionado));


                                    if (equipoSeleccionado!=0) {
                                        detalleAlquiler.setIdEquipo(equipoSeleccionado);
                                    }else{
                                        detalleAlquiler.setIdEquipo(0);
                                    }
                                    if (tractoSeleccionado!=0) {
                                        detalleAlquiler.setIdEquipo2(tractoSeleccionado);
                                    }else{
                                        detalleAlquiler.setIdEquipo2(0);
                                    }
                                    if (operadorSeleccionado!=0) {
                                        detalleAlquiler.setIdOperador(operadorSeleccionado);
                                    }else{
                                        detalleAlquiler.setIdOperador(0);
                                    }
                                    if (ayudanteSeleccionado!=0) {
                                        detalleAlquiler.setIdOperador2(ayudanteSeleccionado);
                                    }else{
                                        detalleAlquiler.setIdOperador2(0);
                                    }
                                    detalleAlquiler.setFechaInicio(sqlDateInicio);
                                    detalleAlquiler.setFechaFin(sqlDateFin);
                                    detalleAlquiler.setObservacionDetalle(edtxtObservacionDetalle.getText().toString());
                                    detalleAlquileresCRUD.addNew(detalleAlquiler);

                                    //PARA LLAMAR AL FRAGMENT ALQUILER
                                    getActivity().onBackPressed();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    //BOTON "NO" PRESIONADO
                                    break;
                            }
                        }

                    };
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                    builder.setMessage("DESEA AGREGAR AL ALQUILER?").setPositiveButton("SI", dialogClickListener).setNegativeButton("NO", dialogClickListener).show();
                }
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        return myView;
    }

    private void seleccionarEquipo() {
        //PARA LLAMAR AL FRAGMENT EQUIPOS
        Equipos_fragment fragmentEquipos = new Equipos_fragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contenedor, fragmentEquipos);
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void seleccionarOperador() {
        //PARA LLAMAR AL FRAGMENT OPERADORES
        Operadores_fragment fragmentOperadores = new Operadores_fragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contenedor, fragmentOperadores);
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public static int restarFecha(Date fechaMayor, Date fechaMenor) {
        long diferenciaEn_ms = fechaMayor.getTime() - fechaMenor.getTime();
        long horas = diferenciaEn_ms / (1000 * 60 * 60);
        return (int) horas;
    }

    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            colocarFecha(opcionFecha);
        }

    };

    private void colocarFecha(String opcion) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        SimpleDateFormat tdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fechaSeleccionada = new Date();
        if (opcion.equals("Inicio")) {
            txtFechaInicio.setText(sdf.format(myCalendar.getTime()));
            try {
                fechaSeleccionada = sdf.parse(txtFechaInicio.getText().toString());
                sqlDateInicio = tdf.format(fechaSeleccionada);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            txtFechaFin.setText(sdf.format(myCalendar.getTime()));
            try {
                fechaSeleccionada = sdf.parse(txtFechaFin.getText().toString());
                sqlDateFin = tdf.format(fechaSeleccionada);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
