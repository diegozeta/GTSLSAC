package com.example.witch.gtslsac_app_1.mRecycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.witch.gtslsac_app_1.R;

/**
 * Created by witch on 14/06/2017.
 */

public class OperadoresRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView txtNombreOpe, txtApellidosOpe,txtEstado;
    ImageView foto;


    ItemClickListener itemClickListener;

    public OperadoresRecyclerViewHolder(View itemView) {
        super(itemView);

        //AQUI EL ERROR
        /*txtNombreOpe = (TextView) itemView.findViewById(R.id.edtNombreOperador);
        txtApellidosOpe = (TextView) itemView.findViewById(R.id.edtApellidosOperador);*/

        txtNombreOpe = (TextView) itemView.findViewById(R.id.nombreOperadorCard);
        txtApellidosOpe = (TextView) itemView.findViewById(R.id.apellidoOperadorCard);
        foto = (ImageView) itemView.findViewById(R.id.fotoOperadorCard);
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
