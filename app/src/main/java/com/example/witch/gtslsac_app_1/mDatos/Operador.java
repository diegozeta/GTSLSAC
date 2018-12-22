package com.example.witch.gtslsac_app_1.mDatos;

/**
 * Created by witch on 16/06/2017.
 */

public class Operador {
    private int idOperador;
    private String nombresOperador;
    private String apellidosOperador;
    private String fotoOperador;
    private boolean estadoOperador;

    public Operador() {
    }

    public Operador(int idOperador, String nombresOperador, String apellidosOperador, String fotoOperador, boolean estadoOperador) {
        this.idOperador = idOperador;
        this.nombresOperador = nombresOperador;
        this.apellidosOperador = apellidosOperador;
        this.fotoOperador = fotoOperador;
        this.estadoOperador = estadoOperador;
    }

    public int getIdOperador() {
        return idOperador;
    }

    public void setIdOperador(int idOperador) {
        this.idOperador = idOperador;
    }

    public String getNombresOperador() {
        return nombresOperador;
    }

    public void setNombresOperador(String nombresOperador) {
        this.nombresOperador = nombresOperador;
    }

    public String getApellidosOperador() {
        return apellidosOperador;
    }

    public void setApellidosOperador(String apellidosOperador) {
        this.apellidosOperador = apellidosOperador;
    }

    public String getFotoOperador() {
        return fotoOperador;
    }

    public void setFotoOperador(String fotoOperador) {
        this.fotoOperador = fotoOperador;
    }

    public boolean isEstadoOperador() {
        return estadoOperador;
    }

    public void setEstadoOperador(boolean estadoOperador) {
        this.estadoOperador = estadoOperador;
    }

    public String toString() {
        return nombresOperador + " " + apellidosOperador;
    }
}
