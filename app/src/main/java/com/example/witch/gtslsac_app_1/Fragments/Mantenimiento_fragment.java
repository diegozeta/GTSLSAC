package com.example.witch.gtslsac_app_1.Fragments;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.witch.gtslsac_app_1.R;
import com.example.witch.gtslsac_app_1.mDatos.Alquiler;
import com.example.witch.gtslsac_app_1.mDatos.AlquileresCRUD;
import com.example.witch.gtslsac_app_1.mDatos.AlquileresCollection;
import com.example.witch.gtslsac_app_1.mDatos.Cliente;
import com.example.witch.gtslsac_app_1.mDatos.DetalleAlquiler;
import com.example.witch.gtslsac_app_1.mDatos.Equipo;
import com.example.witch.gtslsac_app_1.mDatos.EquiposCRUD;
import com.example.witch.gtslsac_app_1.mDatos.EquiposCollection;
import com.example.witch.gtslsac_app_1.mRecycler.AlquilerDetalleRecyclerViewAdapter;
import com.example.witch.gtslsac_app_1.mVolley.Config;
import com.example.witch.gtslsac_app_1.mVolley.OperacionesVolley;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class Mantenimiento_fragment extends Fragment {
    Button btnSeleccionarEquipo, btnConfirmarMantenimiento;
    CheckBox chkObservacionMantenimiento;
    EditText edtxtObservacionMantenimiento;

    TextView txtFechaInicio, txtFechaFin;
    String opcionFecha;

    String sqlDateInicio, sqlDateFin;

    EquiposCRUD equiposCRUD = new EquiposCRUD(EquiposCollection.getEquipos());
    AlquileresCRUD alquilerCrud = new AlquileresCRUD(AlquileresCollection.getAlquileres());

    ArrayList<Equipo> equipos;
    ArrayAdapter<Equipo> adaptador;

    int idEquipo = 0, idUsuario=0;
    String nombreEquipo;

    AutoCompleteTextView autoCompleteTextView;
    OperacionesVolley operacionesVolley = new OperacionesVolley();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.mantenimiento_fragment, container, false);
        Log.e("EVENTO:::::::::::::>", "MANTENIMIENTO_FRAGMENT ONCREATEVIEW LLAMADO!!");
        operacionesVolley.cargarDatosEquiposRecyclerView(getContext());

        Log.e("TAMAÑO VOLLEY EQUIPOS", String.valueOf(operacionesVolley.equipos.size()));

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(Config.PROCESO_DE_MANTENIMIENTO, 1);
        editor.putInt(Config.ID_EQUIPO_MANTENIMIENTO, 0);
        editor.putString(Config.NOMBRE_EQUIPO_MANTENIMIENTO, "");
        editor.commit();

        equipos = new ArrayList<>();
        equipos = equiposCRUD.getEquipos();


        btnSeleccionarEquipo = (Button) myView.findViewById(R.id.btn_seleccionar_equipo_mantenimiento);
        btnConfirmarMantenimiento = (Button) myView.findViewById(R.id.btn_confirmar_mantenimiento);
        btnConfirmarMantenimiento.setVisibility(View.GONE);
        chkObservacionMantenimiento = (CheckBox) myView.findViewById(R.id.chk_observacion_mantenimiento);
        chkObservacionMantenimiento.setChecked(false);
        chkObservacionMantenimiento.setVisibility(View.GONE);
        edtxtObservacionMantenimiento = (EditText) myView.findViewById(R.id.edtxt_observacion_manteninimiento);
        edtxtObservacionMantenimiento.setVisibility(View.GONE);

        txtFechaInicio = (TextView) myView.findViewById(R.id.txt_fecha_inicio_mantenimiento);
        txtFechaFin = (TextView) myView.findViewById(R.id.txt_fecha_fin_mantenimiento);
        txtFechaInicio.setVisibility(View.GONE);
        txtFechaFin.setVisibility(View.GONE);

        autoCompleteTextView = (AutoCompleteTextView) myView.findViewById(R.id.autocompletetextview_equipos_mantenimiento);
        autoCompleteTextView.setThreshold(1);
        adaptador = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, operacionesVolley.equipos);
        autoCompleteTextView.setAdapter(adaptador);

        //SI EL BUNDLE NO ESTÁ VACIO, ES DECIR, SI YA SE SELECCIONÓ EL EQUIPO
        Bundle b = this.getArguments();
        if (b != null) {
            idEquipo = b.getInt("idEquipo");
            Log.e("ID EQUIPO SELECCIONADO", String.valueOf(idEquipo));
            Log.e("DATOS EQUIPO", "ID: " + equipos.get(0).getIdEquipo() + " NOMBRE: " + equipos.get(0).getNombreEquipo() + " MARCA: " + equipos.get(0).getMarcaEquipo() + " MODELO: " + equipos.get(0).getModeloEquipo());
            nombreEquipo = equipos.get(0).getNombreEquipo() + " " + equipos.get(0).getModeloEquipo()+ " " + equipos.get(0).getMarcaEquipo();
            autoCompleteTextView.setText(nombreEquipo);
            autoCompleteTextView.setFocusable(false);

        }
        if (idEquipo == 0) {

        } else {
            // Crear un nuevo adaptador
            chkObservacionMantenimiento.setVisibility(View.VISIBLE);
            btnConfirmarMantenimiento.setVisibility(View.VISIBLE);
            txtFechaInicio.setVisibility(View.VISIBLE);
            txtFechaFin.setVisibility(View.VISIBLE);
        }
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Object item = parent.getItemAtPosition(position);
                if (item instanceof Equipo) {
                    Equipo equipo = (Equipo) item;
                    idEquipo = equipo.getIdEquipo();
                    nombreEquipo = equipo.getNombreEquipo()+ " " + equipo.getModeloEquipo()+ " " + equipo.getMarcaEquipo();
                    if (idEquipo == 0) {

                    } else {
                        // Crear un nuevo adaptador
                        chkObservacionMantenimiento.setVisibility(View.VISIBLE);
                        btnConfirmarMantenimiento.setVisibility(View.VISIBLE);
                        txtFechaInicio.setVisibility(View.VISIBLE);
                        txtFechaFin.setVisibility(View.VISIBLE);
                    }
                }
                Log.e("ID EQUIPO", String.valueOf(idEquipo));
                Log.e("HAY ", String.valueOf(operacionesVolley.equipos.size()) + " EQUIPOS");
                autoCompleteTextView.setText(nombreEquipo);
                autoCompleteTextView.setFocusable(false);
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
        btnSeleccionarEquipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //txtLugar.setVisibility(View.GONE);
                Equipos_fragment equipos_fragment = new Equipos_fragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.contenedor, equipos_fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        chkObservacionMantenimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chkObservacionMantenimiento.isChecked()) {
                    edtxtObservacionMantenimiento.setVisibility(View.VISIBLE);

                } else {
                    edtxtObservacionMantenimiento.setVisibility(View.GONE);
                }
            }
        });
        btnConfirmarMantenimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                idUsuario = sharedPreferences1.getInt(Config.ID_USUARIO_ALQUILER, 0);
                Log.e("ID USUARIO", String.valueOf(idUsuario));
                if (idEquipo == 0) {
                    Toast.makeText(getContext(), "POR FAVOR SELECCIONE UN EQUIPO PARA EL MANTENIMIENTO", Toast.LENGTH_LONG).show();
                } else {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    operacionesVolley.insertarMantenimiento(getContext(), idEquipo, idUsuario, sqlDateInicio, sqlDateFin);
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    //BOTON "NO" PRESIONADO
                                    break;
                            }
                        }

                    };
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                    builder.setMessage("PROCEDER CON MANTENIMIENTO?").setPositiveButton("SI", dialogClickListener).setNegativeButton("NO", dialogClickListener).show();
                }

            }
        });
        return myView;
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
