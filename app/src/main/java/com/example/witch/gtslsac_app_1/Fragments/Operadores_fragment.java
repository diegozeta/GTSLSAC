package com.example.witch.gtslsac_app_1.Fragments;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.witch.gtslsac_app_1.R;

public class Operadores_fragment extends Fragment {

    private AppBarLayout appBar;
    private TabLayout tabs;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        // agregando componentes al layout "operadores_fragment" para el fragment "Operadores_fragment"
        View view = inflater.inflate(R.layout.operadores_fragment, container, false);
        View contenedor = (View) container.getParent();
        appBar = (AppBarLayout) contenedor.findViewById(R.id.appbar);
        tabs = new TabLayout(getActivity());
        appBar.addView(tabs);

        viewPager= (ViewPager)view.findViewById(R.id.pagerOperadores);
        Operadores_fragment.ViewPagerAdapter pagerAdapter = new Operadores_fragment.ViewPagerAdapter(getFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabs.setupWithViewPager(viewPager);
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        appBar.removeView(tabs);
    }


    public class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        String[] tituloTabs = {"OPERADORES", "AGREGAR OPERADORES"};

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:

                    return new Operadores_tab_fragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tituloTabs[position];
        }
    }

}