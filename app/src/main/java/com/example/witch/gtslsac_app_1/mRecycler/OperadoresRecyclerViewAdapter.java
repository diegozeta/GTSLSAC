package com.example.witch.gtslsac_app_1.mRecycler;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.witch.gtslsac_app_1.Activities.MainActivity;
import com.example.witch.gtslsac_app_1.Fragments.Alquiler_Detalle_fragment;
import com.example.witch.gtslsac_app_1.R;
import com.example.witch.gtslsac_app_1.mDatos.Operador;
import com.example.witch.gtslsac_app_1.mDatos.OperadoresCRUD;
import com.example.witch.gtslsac_app_1.mDatos.OperadoresCollection;
import com.example.witch.gtslsac_app_1.mDetalle.OperadoresDetalleActivity;
import com.example.witch.gtslsac_app_1.mVolley.Config;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by witch on 05/06/2017.
 */

public class OperadoresRecyclerViewAdapter extends RecyclerView.Adapter<OperadoresRecyclerViewHolder> {
    OperadoresCRUD crud = new OperadoresCRUD(OperadoresCollection.getOperadores());
    Operador operador = new Operador();
    private ArrayList<Operador> operadores;
    private Context context;

    public OperadoresRecyclerViewAdapter(ArrayList<Operador> listItems, Context context) {
        this.operadores = listItems;
        this.context = context;
    }

    public void setFilter(ArrayList<Operador> newList) {
        operadores = new ArrayList<>();
        operadores.addAll(newList);
        //Log.e("OPERADOR FILTRADO: ", operadores.get(0).getNombresOperador());
        notifyDataSetChanged();
    }


    @Override
    public OperadoresRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.operadores_card, parent, false);
        return new OperadoresRecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(OperadoresRecyclerViewHolder holder, int position) {

        final String nombreOperador = operadores.get(position).getNombresOperador();
        final String apellidosOperador = operadores.get(position).getApellidosOperador();
        final boolean estadoOperador = operadores.get(position).isEstadoOperador();
        final String foto = operadores.get(position).getFotoOperador();
        final int id = operadores.get(position).getIdOperador();

        holder.txtNombreOpe.setText(operadores.get(position).getNombresOperador());
        holder.txtApellidosOpe.setText(operadores.get(position).getApellidosOperador());
        Picasso.with(context).load(R.drawable.operador).into(holder.foto);
        //Picasso.with(context).load(operadores.get(position).getFotoOperador()).into(holder.foto);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                int enAlquiler = sharedPreferences.getInt(Config.PROCESO_DE_ALQUILER, 0);
                Log.e("PROCESO DE ALQUILER", String.valueOf(enAlquiler));
                if (enAlquiler == 1) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    Alquiler_Detalle_fragment alquiler_detalle_fragment = new Alquiler_Detalle_fragment();

                                    operador.setIdOperador(id);
                                    operador.setNombresOperador(nombreOperador);
                                    operador.setApellidosOperador(apellidosOperador);

                                    crud.addNew(operador);

                                    Bundle bundle = new Bundle();
                                    bundle.putInt("idOperador", id);
                                    alquiler_detalle_fragment.setArguments(bundle);
                                    Toast.makeText(context, "OPERADOR SELECCIONADO: " + id + " NOMBRES: " + nombreOperador + " APELLIDOS: " + apellidosOperador, Toast.LENGTH_LONG).show();
                                    ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, alquiler_detalle_fragment).commit();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    //BOTON "NO" PRESIONADO
                                    break;
                            }
                        }
                    };
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                    builder.setMessage("DESEA SELECCIONAR ESTE OPERADOR").setPositiveButton("SI", dialogClickListener).setNegativeButton("NO", dialogClickListener).show();

                } else {

                    openDetailActivity(id, nombreOperador, apellidosOperador, foto, estadoOperador, pos);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return operadores.size();
    }

    private void openDetailActivity(int id, String nombres, String apellidos, String foto, boolean estado, int position) {
        Intent i = new Intent(context, OperadoresDetalleActivity.class);
        //pack data
        i.putExtra("ID_KEY", id);
        i.putExtra("NOMBRES_KEY", nombres);
        i.putExtra("APELLIDOS_KEY", apellidos);
        i.putExtra("FOTO_KEY", foto);
        i.putExtra("ESTADO_KEY", estado);
        i.putExtra("POSICION_KEY", position);
        context.startActivity(i);
    }
}