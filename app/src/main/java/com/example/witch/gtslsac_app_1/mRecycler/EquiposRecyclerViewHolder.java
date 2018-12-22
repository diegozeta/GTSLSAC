package com.example.witch.gtslsac_app_1.mRecycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.witch.gtslsac_app_1.R;

/**
 * Created by witch on 14/06/2017.
 */

public class EquiposRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView nombreTxt, marcaTxt, modeloTxt,capacidadTxt,anioTxt,placaTxt,colorTxt,codigoTxt;
    ImageView foto;


    ItemClickListener itemClickListener;

    public EquiposRecyclerViewHolder(View itemView) {
        super(itemView);

        nombreTxt = (TextView) itemView.findViewById(R.id.nombreEquipoCard);
        marcaTxt = (TextView) itemView.findViewById(R.id.marcaEquipoCard);
        modeloTxt = (TextView) itemView.findViewById(R.id.modeloEquipoCard);
        capacidadTxt = (TextView) itemView.findViewById(R.id.capacidadEquipoCard);
        anioTxt = (TextView) itemView.findViewById(R.id.anioEquipoCard);
        placaTxt = (TextView) itemView.findViewById(R.id.placaEquipoCard);
        colorTxt = (TextView) itemView.findViewById(R.id.colorEquipoCard);
        codigoTxt = (TextView) itemView.findViewById(R.id.codigoEquipoCard);

        foto = (ImageView) itemView.findViewById(R.id.fotoEquiposCard);

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
