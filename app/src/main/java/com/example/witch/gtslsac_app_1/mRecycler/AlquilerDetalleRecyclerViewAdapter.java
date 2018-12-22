package com.example.witch.gtslsac_app_1.mRecycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.witch.gtslsac_app_1.R;
import com.example.witch.gtslsac_app_1.mDatos.Cliente;
import com.example.witch.gtslsac_app_1.mDatos.ClientesCRUD;
import com.example.witch.gtslsac_app_1.mDatos.ClientesCollection;
import com.example.witch.gtslsac_app_1.mDatos.DetalleAlquiler;
import com.example.witch.gtslsac_app_1.mDatos.Equipo;
import com.example.witch.gtslsac_app_1.mDatos.EquiposCRUD;
import com.example.witch.gtslsac_app_1.mDatos.EquiposCollection;
import com.example.witch.gtslsac_app_1.mDatos.Operador;
import com.example.witch.gtslsac_app_1.mDatos.OperadoresCRUD;
import com.example.witch.gtslsac_app_1.mDatos.OperadoresCollection;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by witch on 05/06/2017.
 */

public class AlquilerDetalleRecyclerViewAdapter extends RecyclerView.Adapter<AlquilerDetalleRecyclerViewHolder> {
    private ArrayList<DetalleAlquiler> detalleAlquileres;
    private Context context;
    private EquiposCRUD equiposCRUD = new EquiposCRUD(EquiposCollection.getEquipos());
    private ArrayList<Equipo> equipos;
    private OperadoresCRUD operadoresCRUD = new OperadoresCRUD(OperadoresCollection.getOperadores());
    private ArrayList<Operador> operadores;


    private String datosEquipo = "N/A", datosTracto = "N/A", datosOperador = "N/A", datosAyudante = "N/A";

    public AlquilerDetalleRecyclerViewAdapter(ArrayList<DetalleAlquiler> listItems, Context context) {
        this.detalleAlquileres = listItems;
        this.context = context;
    }

    @Override
    public AlquilerDetalleRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.alquiler_detalle_card, parent, false);
        return new AlquilerDetalleRecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AlquilerDetalleRecyclerViewHolder holder, int position) {
        final int id = detalleAlquileres.get(position).getIdDetalleAlquiler();
        final int id_alquiler = detalleAlquileres.get(position).getIdAlquiler();
        final int id_usuario = detalleAlquileres.get(position).getIdUsuario();
        final int id_cliente = detalleAlquileres.get(position).getIdCliente();
        final int id_equipo = detalleAlquileres.get(position).getIdEquipo();
        final int id_equipo2 = detalleAlquileres.get(position).getIdEquipo2();
        final int id_operador = detalleAlquileres.get(position).getIdOperador();
        final int id_operador2 = detalleAlquileres.get(position).getIdOperador2();
        final String fechaInicio = detalleAlquileres.get(position).getFechaInicio();
        final String fechaFin = detalleAlquileres.get(position).getFechaFin();
        final String estado = detalleAlquileres.get(position).getObservacionDetalle();
        Log.e("POSICION: ", String.valueOf(position));
        Log.e("TAMAÑO DETALLE ALQUILER", String.valueOf(detalleAlquileres.size()));
        Log.e("ID_EQUIPO RECUPERADO", String.valueOf(id_equipo));
        Log.e("ID_TRACTO RECUPERADO", String.valueOf(id_equipo2));
        Log.e("ID_OPERADOR RECUPERADO", String.valueOf(id_operador));
        Log.e("ID_AYUDANTE RECUPERADO", String.valueOf(id_operador2));
        //Picasso.with(context).load(equipos.get(position).getLogoEmpresa()).into(holder.logo);

        equipos = new ArrayList<>();
        equipos = equiposCRUD.getEquipos();
        operadores = new ArrayList<>();
        operadores = operadoresCRUD.getOperadores();
        Log.e("TAMAÑO DE EQUIPOS", String.valueOf(equipos.size()));
        Log.e("TAMAÑO DE OPERADORES", String.valueOf(operadores.size()));

        for (int i = 0; i < equipos.size(); i++) {
            if (id_equipo == equipos.get(i).getIdEquipo()) {
                datosEquipo = "EQUIPO: " + equipos.get(i).getNombreEquipo() + " " + equipos.get(i).getMarcaEquipo() + " " + equipos.get(i).getModeloEquipo();
            }
        }
        for (int i = 0; i < equipos.size(); i++) {
            if (id_equipo2 == equipos.get(i).getIdEquipo()) {
                datosTracto = "CAMA/TRACTO: " + equipos.get(i).getNombreEquipo() + " " + equipos.get(i).getMarcaEquipo() + " " + equipos.get(i).getModeloEquipo();
            }
        }
        for (int i = 0; i < operadores.size(); i++) {
            if (id_operador == operadores.get(i).getIdOperador()) {
                datosOperador = "OPERADOR: " + operadores.get(i).getNombresOperador() + " " + operadores.get(i).getApellidosOperador();
            }
        }
        for (int i = 0; i < operadores.size(); i++) {
            if (id_operador2 == operadores.get(i).getIdOperador()) {
                datosAyudante = "RIGGER/AYUDANTE: " + operadores.get(i).getNombresOperador() + " " + operadores.get(i).getApellidosOperador();
            }
        }
        Log.e("DATOS EQUIPO RECUPERADO", String.valueOf(datosEquipo));
        Log.e("DATOS TRACTO RECUPERADO", String.valueOf(datosTracto));
        Log.e("DATOS OPER RECUPERADO", String.valueOf(datosOperador));
        Log.e("DATOS AYUD RECUPERADO", String.valueOf(datosAyudante));

        holder.equipo.setText(String.valueOf(datosEquipo));
        holder.tracto.setText(String.valueOf(datosTracto));
        holder.operador.setText(String.valueOf(datosOperador));
        holder.ayudante.setText(String.valueOf(datosAyudante));

        datosEquipo = "N/A";
        datosTracto = "N/A";
        datosOperador = "N/A";
        datosAyudante = "N/A";

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int pos) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return detalleAlquileres.size();
    }
}