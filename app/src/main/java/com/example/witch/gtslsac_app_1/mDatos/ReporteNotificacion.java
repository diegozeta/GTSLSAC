package com.example.witch.gtslsac_app_1.mDatos;

/**
 * Created by witch on 27/06/2017.
 */

public class ReporteNotificacion {
    private int idDetalleAlquiler;
    private int idAlquiler;
    private String nombresUsuario;
    private String apellidosUsuario;
    private String nombreEmpresa;
    private String nombreEquipo1;
    private String marcaEquipo1;
    private String modeloEquipo1;
    private String codigoEquipo1;
    private String nombreEquipo2;
    private String marcaEquipo2;
    private String modeloEquipo2;
    private String codigoEquipo2;
    private String nombresOperador1;
    private String apellidosOperador1;
    private String nombresOperador2;
    private String apellidosOperador2;
    private String fechaInicio;
    private String fechaFin;

    public ReporteNotificacion(int idDetalleAlquiler, int idAlquiler, String nombresUsuario, String apellidosUsuario, String nombreEmpresa, String nombreEquipo1, String marcaEquipo1, String modeloEquipo1, String codigoEquipo1, String nombreEquipo2, String marcaEquipo2, String modeloEquipo2, String codigoEquipo2, String nombresOperador1, String apellidosOperador1, String nombresOperador2, String apellidosOperador2, String fechaInicio, String fechaFin) {
        this.idDetalleAlquiler = idDetalleAlquiler;
        this.idAlquiler = idAlquiler;
        this.nombresUsuario = nombresUsuario;
        this.apellidosUsuario = apellidosUsuario;
        this.nombreEmpresa = nombreEmpresa;
        this.nombreEquipo1 = nombreEquipo1;
        this.marcaEquipo1 = marcaEquipo1;
        this.modeloEquipo1 = modeloEquipo1;
        this.codigoEquipo1 = codigoEquipo1;
        this.nombreEquipo2 = nombreEquipo2;
        this.marcaEquipo2 = marcaEquipo2;
        this.modeloEquipo2 = modeloEquipo2;
        this.codigoEquipo2 = codigoEquipo2;
        this.nombresOperador1 = nombresOperador1;
        this.apellidosOperador1 = apellidosOperador1;
        this.nombresOperador2 = nombresOperador2;
        this.apellidosOperador2 = apellidosOperador2;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
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

    public String getNombresUsuario() {
        return nombresUsuario;
    }

    public void setNombresUsuario(String nombresUsuario) {
        this.nombresUsuario = nombresUsuario;
    }

    public String getApellidosUsuario() {
        return apellidosUsuario;
    }

    public void setApellidosCliente(String apellidosUsuario) {
        this.apellidosUsuario = apellidosUsuario;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getNombreEquipo1() {
        return nombreEquipo1;
    }

    public void setNombreEquipo1(String nombreEquipo1) {
        this.nombreEquipo1 = nombreEquipo1;
    }

    public String getMarcaEquipo1() {
        return marcaEquipo1;
    }

    public void setMarcaEquipo1(String marcaEquipo1) {
        this.marcaEquipo1 = marcaEquipo1;
    }

    public String getModeloEquipo1() {
        return modeloEquipo1;
    }

    public void setModeloEquipo1(String modeloEquipo1) {
        this.modeloEquipo1 = modeloEquipo1;
    }

    public String getCodigoEquipo1() {
        return codigoEquipo1;
    }

    public void setCodigoEquipo1(String codigoEquipo1) {
        this.codigoEquipo1 = codigoEquipo1;
    }

    public String getNombreEquipo2() {
        return nombreEquipo2;
    }

    public void setNombreEquipo2(String nombreEquipo2) {
        this.nombreEquipo2 = nombreEquipo2;
    }

    public String getMarcaEquipo2() {
        return marcaEquipo2;
    }

    public void setMarcaEquipo2(String marcaEquipo2) {
        this.marcaEquipo2 = marcaEquipo2;
    }

    public String getModeloEquipo2() {
        return modeloEquipo2;
    }

    public void setModeloEquipo2(String modeloEquipo2) {
        this.modeloEquipo2 = modeloEquipo2;
    }

    public String getCodigoEquipo2() {
        return codigoEquipo2;
    }

    public void setCodigoEquipo2(String codigoEquipo2) {
        this.codigoEquipo2 = codigoEquipo2;
    }

    public String getNombresOperador1() {
        return nombresOperador1;
    }

    public void setNombresOperador1(String nombresOperador1) {
        this.nombresOperador1 = nombresOperador1;
    }

    public String getApellidosOperador1() {
        return apellidosOperador1;
    }

    public void setApellidosOperador1(String apellidosOperador1) {
        this.apellidosOperador1 = apellidosOperador1;
    }

    public String getNombresOperador2() {
        return nombresOperador2;
    }

    public void setNombresOperador2(String nombresOperador2) {
        this.nombresOperador2 = nombresOperador2;
    }

    public String getApellidosOperador2() {
        return apellidosOperador2;
    }

    public void setApellidosOperador2(String apellidosOperador2) {
        this.apellidosOperador2 = apellidosOperador2;
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
}
