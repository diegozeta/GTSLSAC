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
import com.example.witch.gtslsac_app_1.Fragments.Alquiler_fragment;
import com.example.witch.gtslsac_app_1.Fragments.Mantenimiento_fragment;
import com.example.witch.gtslsac_app_1.R;
import com.example.witch.gtslsac_app_1.mDatos.Equipo;
import com.example.witch.gtslsac_app_1.mDatos.EquiposCRUD;
import com.example.witch.gtslsac_app_1.mDatos.EquiposCollection;
import com.example.witch.gtslsac_app_1.mDetalle.EquiposDetalleActivity;
import com.example.witch.gtslsac_app_1.mVolley.Config;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by witch on 05/06/2017.
 */

public class EquiposRecyclerViewAdapter extends RecyclerView.Adapter<EquiposRecyclerViewHolder> {
    EquiposCRUD crud = new EquiposCRUD(EquiposCollection.getEquipos());
    Equipo equipo = new Equipo();
    private ArrayList<Equipo> equipos;
    private Context context;

    public EquiposRecyclerViewAdapter(ArrayList<Equipo> listItems, Context context) {
        this.equipos = listItems;
        this.context = context;
    }

    @Override
    public EquiposRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.equipos_card, parent, false);
        return new EquiposRecyclerViewHolder(v);
    }

    public void setFilter(ArrayList<Equipo> newList) {
        equipos = new ArrayList<>();
        equipos.addAll(newList);
        //Log.e("EQUIPO FILTRADO: ", equipos.get(0).getNombreEquipo());
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(EquiposRecyclerViewHolder holder, int position) {
        final int id = equipos.get(position).getIdEquipo();
        final String nombre = equipos.get(position).getNombreEquipo();
        final String marca = equipos.get(position).getMarcaEquipo();
        final String modelo = equipos.get(position).getModeloEquipo();
        final String capacidad = equipos.get(position).getCapacidadEquipo();
        final String anio = equipos.get(position).getAnioEquipo();
        final String placa = equipos.get(position).getPlacaEquipo();
        final String color = equipos.get(position).getColorEquipo();
        final String codigo = equipos.get(position).getCodigoEquipo();
        final String foto = equipos.get(position).getFotoEquipo();
        final boolean estado = equipos.get(position).isEstadoEquipo();


        //Picasso.with(context).load(equipos.get(position).getLogoEmpresa()).into(holder.logo);
        holder.nombreTxt.setText(equipos.get(position).getNombreEquipo());
        holder.marcaTxt.setText(equipos.get(position).getMarcaEquipo());
        holder.modeloTxt.setText(equipos.get(position).getModeloEquipo());
        holder.capacidadTxt.setText(String.valueOf(equipos.get(position).getCapacidadEquipo()));
        holder.anioTxt.setText((equipos.get(position).getAnioEquipo()));
        holder.placaTxt.setText(equipos.get(position).getPlacaEquipo());
        holder.colorTxt.setText(equipos.get(position).getColorEquipo());
        holder.codigoTxt.setText(equipos.get(position).getCodigoEquipo());

        Picasso.with(context).load(R.drawable.grua).into(holder.foto);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                final int enAlquiler = sharedPreferences.getInt(Config.PROCESO_DE_ALQUILER, 0);
                final int enMantenimiento = sharedPreferences.getInt(Config.PROCESO_DE_MANTENIMIENTO, 0);
                Log.e("PROCESO DE ALQUILER", String.valueOf(enAlquiler));
                Log.e("PROCESO MANTENIMIENTO", String.valueOf(enMantenimiento));
                if (enAlquiler == 1 || enMantenimiento == 1) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:


                                    equipo.setIdEquipo(id);
                                    equipo.setNombreEquipo(nombre);
                                    equipo.setMarcaEquipo(marca);
                                    equipo.setModeloEquipo(modelo);
                                    equipo.setPlacaEquipo(placa);
                                    equipo.setColorEquipo(color);
                                    equipo.setCodigoEquipo(codigo);

                                    crud.addNew(equipo);

                                    Bundle bundle=new Bundle();
                                    bundle.putInt("idEquipo", id);
                                    Toast.makeText(context, "EQUIPO SELECCIONADO: " + id + " " + nombre + " " + marca + " "+ modelo + " "+ placa + " "+ color + " "+ codigo, Toast.LENGTH_LONG).show();
                                    if(enAlquiler==1) {
                                        Alquiler_Detalle_fragment alquiler_detalle_fragment = new Alquiler_Detalle_fragment();
                                        alquiler_detalle_fragment.setArguments(bundle);
                                        ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, alquiler_detalle_fragment).commit();
                                    }
                                    if(enMantenimiento==1){
                                        Mantenimiento_fragment mantenimiento_fragment = new Mantenimiento_fragment();
                                        mantenimiento_fragment.setArguments(bundle);
                                        ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, mantenimiento_fragment).commit();
                                    }

                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    //BOTON "NO" PRESIONADO
                                    break;
                            }
                        }

                    };
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                    builder.setMessage("DESEA SELECCIONAR ESTE EQUIPO").setPositiveButton("SI", dialogClickListener).setNegativeButton("NO", dialogClickListener).show();

                } else {
                    //CUANDO SE QUIERE VER LOS DETALLES DEL CLIENTE
                    openDetailActivity(id, nombre, marca, modelo, capacidad, anio, placa, color, codigo, foto, estado, pos);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return equipos.size();
    }

    private void openDetailActivity(int id, String nombre, String marca, String modelo, String capacidad, String anio, String placa, String color, String codigo, String foto, boolean estado, int position) {
        Intent i = new Intent(context, EquiposDetalleActivity.class);
        //pack data
        i.putExtra("ID_KEY", id);
        i.putExtra("NOMBRE_KEY", nombre);
        i.putExtra("MARCA_KEY", marca);
        i.putExtra("MODELO_KEY", modelo);
        i.putExtra("CAPACIDAD_KEY", capacidad);
        i.putExtra("ANIO_KEY", anio);
        i.putExtra("PLACA_KEY", placa);
        i.putExtra("COLOR_KEY", color);
        i.putExtra("CODIGO_KEY", codigo);
        //i.putExtra("FOTO_KEY", foto);
        i.putExtra("ESTADO_KEY", estado);
        i.putExtra("POSICION_KEY", position);
        context.startActivity(i);
    }
}