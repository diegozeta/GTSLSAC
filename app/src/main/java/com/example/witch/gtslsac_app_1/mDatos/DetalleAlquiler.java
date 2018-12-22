package com.example.witch.gtslsac_app_1.mDatos;

import java.util.Date;

/**
 * Created by witch on 27/06/2017.
 */

public class DetalleAlquiler {
    private int idDetalleAlquiler;
    private int idAlquiler;
    private int idUsuario;
    private int idCliente;
    private int idEquipo;
    private int idEquipo2;
    private int idOperador;
    private int idOperador2;
    private String fechaInicio;
    private String fechaFin;
    private String observacionDetalle;

    public DetalleAlquiler(int idDetalleAlquiler, int idAlquiler, int idUsuario, int idCliente, int idEquipo, int idEquipo2, int idOperador, int idOperador2, String fechaInicio, String fechaFin, String observacionDetalle) {
        this.idDetalleAlquiler = idDetalleAlquiler;
        this.idAlquiler = idAlquiler;
        this.idUsuario = idUsuario;
        this.idCliente = idCliente;
        this.idEquipo = idEquipo;
        this.idEquipo2 = idEquipo2;
        this.idOperador = idOperador;
        this.idOperador2 = idOperador2;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.observacionDetalle = observacionDetalle;
    }

    public int getIdDetalleAlquiler() {
        return idDetalleAlquiler;
    }

    public void setIdDetalleAlquiler(int idDetalleAlquiler) {
        this.idDetalleAlquiler = idDetalleAlquiler;
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

    public int getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    public int getIdEquipo2() {
        return idEquipo2;
    }

    public void setIdEquipo2(int idEquipo2) {
        this.idEquipo2 = idEquipo2;
    }

    public int getIdOperador() {
        return idOperador;
    }

    public void setIdOperador(int idOperador) {
        this.idOperador = idOperador;
    }

    public int getIdOperador2() {
        return idOperador2;
    }

    public void setIdOperador2(int idOperador2) {
        this.idOperador2 = idOperador2;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getObservacionDetalle() {
        return observacionDetalle;
    }

    public void setObservacionDetalle(String observacionDetalle) {
        this.observacionDetalle = observacionDetalle;
    }
    public DetalleAlquiler(){

    }
}
