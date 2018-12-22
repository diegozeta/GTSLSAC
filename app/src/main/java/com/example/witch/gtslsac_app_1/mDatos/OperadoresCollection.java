package com.example.witch.gtslsac_app_1.mDatos;

import java.util.ArrayList;

/**
 * Created by witch on 14/06/2017.
 */

public class OperadoresCollection {
    static ArrayList<Operador> operadores = new ArrayList<>();

    public static ArrayList<Operador> getOperadores(){
        return operadores;
    }
    public static void deleteAll(){
        operadores.clear();
    }
}
