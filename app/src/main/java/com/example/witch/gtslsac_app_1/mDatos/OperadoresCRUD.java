package com.example.witch.gtslsac_app_1.mDatos;

import java.util.ArrayList;
//CLASE MOCK PARA RETENER LOS DATOS DE FORMA LOCAL ANTES DE REALIZAR UNA OPERACION EN LA DB

/**
 * Created by witch on 14/06/2017.
 */

public class OperadoresCRUD {
    ArrayList<Operador> operadores;

    public OperadoresCRUD(ArrayList<Operador> operadores) {
        this.operadores = operadores;
    }

    //AGREGAR
    public boolean addNew(Operador operador) {
        try {
            operadores.add(operador);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public void deleteAll(){
        operadores.clear();
    }

    //Listar
    public ArrayList<Operador> getOperadores() {
        return operadores;
    }

    //Actualizar
    public boolean update(int position, Operador newOperador) {
        try {
            operadores.remove(position);
            operadores.add(position, newOperador);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    //Eliminar
    public boolean delete(int position) {
        try {
            operadores.remove(position);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
