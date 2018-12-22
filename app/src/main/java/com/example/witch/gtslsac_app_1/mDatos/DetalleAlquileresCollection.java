package com.example.witch.gtslsac_app_1.mDatos;

import java.util.ArrayList;

/**
 * Created by witch on 14/06/2017.
 */

public class DetalleAlquileresCollection {
    static ArrayList<DetalleAlquiler> detalleAlquileres = new ArrayList<>();

    public static ArrayList<DetalleAlquiler> getDetalleAlquileres(){
        return detalleAlquileres;
    }
    public static void deleteAll(){
        detalleAlquileres.clear();
    }
}
