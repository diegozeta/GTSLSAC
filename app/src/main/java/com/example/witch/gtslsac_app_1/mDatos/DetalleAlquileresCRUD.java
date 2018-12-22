package com.example.witch.gtslsac_app_1.mDatos;

import java.util.ArrayList;
//CLASE MOCK PARA RETENER LOS DATOS DE FORMA LOCAL ANTES DE REALIZAR UNA OPERACION EN LA DB

/**
 * Created by witch on 14/06/2017.
 */

public class DetalleAlquileresCRUD {
    ArrayList<DetalleAlquiler> detalleAlquiler;

    public DetalleAlquileresCRUD(ArrayList<DetalleAlquiler> DetalleAlquiler) {
        this.detalleAlquiler = DetalleAlquiler;
    }
    //AGREGAR
    public boolean addNew(DetalleAlquiler DetalleAlquiler) {
        try {
            detalleAlquiler.add(DetalleAlquiler);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }
    //Listar
    public ArrayList<DetalleAlquiler> getDetalleAlquileres() {
        return detalleAlquiler;
    }
    public void deleteAll(){
        detalleAlquiler.clear();
    }
    //Actualizar
    public boolean update(int position, DetalleAlquiler newDetalleAlquiler) {
        try {
            detalleAlquiler.remove(position);
            detalleAlquiler.add(position, newDetalleAlquiler);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    //Eliminar
    public boolean delete(int position) {
        try {
            detalleAlquiler.remove(position);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

}
