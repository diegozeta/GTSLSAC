package com.example.witch.gtslsac_app_1.mRecycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.witch.gtslsac_app_1.R;

/**
 * Created by witch on 14/06/2017.
 */

public class AlquilerDetalleRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView equipo, tracto, operador, ayudante;

    ItemClickListener itemClickListener;

    public AlquilerDetalleRecyclerViewHolder(View itemView) {
        super(itemView);

        equipo = (TextView) itemView.findViewById(R.id.nombre_equipo_card);
        tracto = (TextView) itemView.findViewById(R.id.nombre_tracto_card);
        operador = (TextView) itemView.findViewById(R.id.nombre_operador_card);
        ayudante = (TextView) itemView.findViewById(R.id.nombre_ayudante_card);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){

        this.itemClickListener=itemClickListener;
    }
    @Override
    public void onClick(View v) {
        this.itemClickListener.onItemClick(getLayoutPosition());
    }
}
