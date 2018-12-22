package com.example.witch.gtslsac_app_1.mDatos;

import java.util.ArrayList;
//CLASE MOCK PARA RETENER LOS DATOS DE FORMA LOCAL ANTES DE REALIZAR UNA OPERACION EN LA DB
/**
 * Created by witch on 14/06/2017.
 */

public class ClientesCRUD {
    ArrayList<Cliente> clientes;

    public ClientesCRUD(ArrayList<Cliente> clientes) {
        this.clientes = clientes;
    }
    public ClientesCRUD(){

    }
    //AGREGAR
    public boolean addNew(Cliente cliente) {
        try {
            clientes.add(cliente);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }
    //Listar
    public ArrayList<Cliente> getClientes() {
        return clientes;
    }
    //Borrar todos
    public void deleteAll(){
        clientes.clear();
    }
    //Actualizar
    public boolean update(int position, Cliente newCliente) {
        try {
            clientes.remove(position);
            clientes.add(position, newCliente);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    //Eliminar
    public boolean delete(int position) {
        try {
            clientes.remove(position);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }


}
