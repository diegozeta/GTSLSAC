package com.example.witch.gtslsac_app_1.mDatos;

import java.util.ArrayList;
//CLASE MOCK PARA RETENER LOS DATOS DE FORMA LOCAL ANTES DE REALIZAR UNA OPERACION EN LA DB

/**
 * Created by witch on 14/06/2017.
 */

public class AlquileresCRUD {
    ArrayList<Alquiler> alquileres;

    public AlquileresCRUD(ArrayList<Alquiler> alquileres) {
        this.alquileres = alquileres;
    }
    //AGREGAR
    public boolean addNew(Alquiler alquiler) {
        try {
            alquileres.add(alquiler);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }
    //Listar
    public ArrayList<Alquiler> getAlquileres() {
        return alquileres;
    }
    //Actualizar
    public boolean update(int position, Alquiler newAlquiler) {
        try {
            alquileres.remove(position);
            alquileres.add(position, newAlquiler);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    //Eliminar
    public boolean delete(int position) {
        try {
            alquileres.remove(position);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }


}
