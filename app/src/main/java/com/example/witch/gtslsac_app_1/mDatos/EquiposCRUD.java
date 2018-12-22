package com.example.witch.gtslsac_app_1.mDatos;

import java.util.ArrayList;
//CLASE MOCK PARA RETENER LOS DATOS DE FORMA LOCAL ANTES DE REALIZAR UNA OPERACION EN LA DB

/**
 * Created by witch on 14/06/2017.
 */

public class EquiposCRUD {
    ArrayList<Equipo> equipos;

    public EquiposCRUD(ArrayList<Equipo> equipos) {
        this.equipos = equipos;
    }
    public EquiposCRUD(){

    }
    //AGREGAR
    public boolean addNew(Equipo equipo) {
        try {
            equipos.add(equipo);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }
    //Listar
    public ArrayList<Equipo> getEquipos() {
        return equipos;
    }
    //Borrar todos
    public void deleteAll(){
        equipos.clear();
    }
    //Actualizar
    public boolean update(int position, Equipo newEquipo) {
        try {
            equipos.remove(position);
            equipos.add(position, newEquipo);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    //Eliminar
    public boolean delete(int position) {
        try {
            equipos.remove(position);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }


}
