package com.example.witch.gtslsac_app_1.mDatos;

/**
 * Created by witch on 27/06/2017.
 */

public class Alquiler {
    private int idAlquiler;
    private int idUsuario;
    private int idCliente;
    private String lugarAlquiler;
    private String observacionAlquiler;

    public Alquiler(int idAlquiler, int idUsuario, int idCliente, String lugarAlquiler, String observacionAlquiler) {
        this.idAlquiler = idAlquiler;
        this.idUsuario = idUsuario;
        this.idCliente = idCliente;
        this.lugarAlquiler = lugarAlquiler;
        this.observacionAlquiler = observacionAlquiler;
    }

    public int getIdAlquiler() {
        return idAlquiler;
    }

    public void setIdAlquiler(int idAlquiler) {
        this.idAlquiler = idAlquiler;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getLugarAlquiler() {
        return lugarAlquiler;
    }

    public void setLugarAlquiler(String lugarAlquiler) {
        this.lugarAlquiler = lugarAlquiler;
    }

    public String getObservacionAlquiler() {
        return observacionAlquiler;
    }

    public void setObservacionAlquiler(String observacionAlquiler) {
        this.observacionAlquiler = observacionAlquiler;
    }
    public Alquiler() {
    }
}
