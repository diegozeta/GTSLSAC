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
import com.example.witch.gtslsac_app_1.Fragments.Alquiler_fragment;
import com.example.witch.gtslsac_app_1.R;
import com.example.witch.gtslsac_app_1.mDatos.Cliente;
import com.example.witch.gtslsac_app_1.mDatos.ClientesCRUD;
import com.example.witch.gtslsac_app_1.mDatos.ClientesCollection;
import com.example.witch.gtslsac_app_1.mDetalle.ClientesDetalleActivity;
import com.example.witch.gtslsac_app_1.mVolley.Config;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by witch on 05/06/2017.
 */

public class ClientesRecyclerViewAdapter extends RecyclerView.Adapter<ClientesRecyclerViewHolder> {
    ClientesCRUD crud = new ClientesCRUD(ClientesCollection.getClientes());
    Cliente cliente = new Cliente();
    private ArrayList<Cliente> clientes;
    private Context context;

    public ClientesRecyclerViewAdapter(ArrayList<Cliente> listItems, Context context) {
        this.clientes = listItems;
        this.context = context;
    }

    @Override
    public ClientesRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.clientes_card, parent, false);
        return new ClientesRecyclerViewHolder(v);
    }

    public void setFilterRecycler(ArrayList<Cliente> newList) {
        clientes = new ArrayList<>();
        clientes.addAll(newList);
        //Log.e("CLIENTE FILTRADO: ", clientes.get(0).getNombreEmpresa());
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ClientesRecyclerViewHolder holder, int position) {

        final String empresa = clientes.get(position).getNombreEmpresa();
        final String logo = clientes.get(position).getLogoEmpresa();
        final int id = clientes.get(position).getIdEmpresa();

        Picasso.with(context).load(clientes.get(position).getLogoEmpresa()).into(holder.logo);
        holder.nameTxt.setText(clientes.get(position).getNombreEmpresa());

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
                                    Alquiler_fragment alquiler_fragment = new Alquiler_fragment();
                                    Bundle bundle=new Bundle();
                                    bundle.putInt("idEmpresa", id);
                                    cliente.setIdEmpresa(id);
                                    cliente.setNombreEmpresa(empresa);
                                    cliente.setLogoEmpresa(logo);
                                    crud.deleteAll();
                                    crud.addNew(cliente);
                                    alquiler_fragment.setArguments(bundle);
                                    Toast.makeText(context, "CLIENTE SELECCIONADO: " + empresa, Toast.LENGTH_LONG).show();
                                    ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, alquiler_fragment).commit();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    //BOTON "NO" PRESIONADO
                                    break;
                            }
                        }

                    };
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                    builder.setMessage("DESEA SELECCIONAR ESTE CLIENTE").setPositiveButton("SI", dialogClickListener).setNegativeButton("NO", dialogClickListener).show();

                } else {
                    //CUANDO SE QUIERE VER LOS DETALLES DEL CLIENTE
                    openDetailActivity(id, empresa, logo, pos);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return clientes.size();
    }

    private void openDetailActivity(int id, String empresa, String logo, int position) {
        Intent i = new Intent(context, ClientesDetalleActivity.class);
        //enviando datos a la activityClientesDetalleActivity
        i.putExtra("ID_KEY", id);
        i.putExtra("EMPRESA_KEY", empresa);
        i.putExtra("LOGO_KEY", logo);
        i.putExtra("POSICION_KEY", position);
        context.startActivity(i);
    }
}