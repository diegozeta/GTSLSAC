package com.example.witch.gtslsac_app_1.mRecycler;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.witch.gtslsac_app_1.Fragments.Clientes_tab_fragment;
import com.example.witch.gtslsac_app_1.R;
import com.example.witch.gtslsac_app_1.mVolley.Config;

/**
 * Created by witch on 14/06/2017.
 */

public class ClientesRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView nameTxt;
    ImageView logo;

    ItemClickListener itemClickListener;

    public ClientesRecyclerViewHolder(View itemView) {
        super(itemView);

        nameTxt = (TextView) itemView.findViewById(R.id.nombreEmpresaCard);
        logo = (ImageView) itemView.findViewById(R.id.logo);


        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {

        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.onItemClick(getLayoutPosition());
    }
}
