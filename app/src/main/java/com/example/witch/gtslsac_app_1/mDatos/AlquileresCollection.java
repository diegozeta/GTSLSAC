package com.example.witch.gtslsac_app_1.mDatos;

import java.util.ArrayList;

/**
 * Created by witch on 14/06/2017.
 */

public class AlquileresCollection {
    static ArrayList<Alquiler> alquileres = new ArrayList<>();

    public static ArrayList<Alquiler> getAlquileres(){
        return alquileres;
    }
    public static void deleteAll(){
        alquileres.clear();
    }
}
