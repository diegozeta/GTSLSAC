package com.example.witch.gtslsac_app_1.mDatos;

/**
 * Created by witch on 23/06/2017.
 */

public class Equipo {
    private int idEquipo;
    private String nombreEquipo;
    private String marcaEquipo;
    private String modeloEquipo;
    private String capacidadEquipo;
    private String anioEquipo;
    private String placaEquipo;
    private String colorEquipo;
    private String codigoEquipo;
    private String fotoEquipo;
    private boolean estadoEquipo;

    public Equipo(int idEquipo, String nombreEquipo, String marcaEquipo, String modeloEquipo, String capacidadEquipo, String anioEquipo, String placaEquipo, String colorEquipo, String codigoEquipo, String fotoEquipo, boolean estadoEquipo) {
        this.idEquipo = idEquipo;
        this.nombreEquipo = nombreEquipo;
        this.marcaEquipo = marcaEquipo;
        this.modeloEquipo = modeloEquipo;
        this.capacidadEquipo = capacidadEquipo;
        this.anioEquipo = anioEquipo;
        this.placaEquipo = placaEquipo;
        this.colorEquipo = colorEquipo;
        this.codigoEquipo = codigoEquipo;
        this.fotoEquipo = fotoEquipo;
        this.estadoEquipo = estadoEquipo;
    }

    public int getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public String getMarcaEquipo() {
        return marcaEquipo;
    }

    public void setMarcaEquipo(String marcaEquipo) {
        this.marcaEquipo = marcaEquipo;
    }

    public String getModeloEquipo() {
        return modeloEquipo;
    }

    public void setModeloEquipo(String modeloEquipo) {
        this.modeloEquipo = modeloEquipo;
    }

    public String getCapacidadEquipo() {
        return capacidadEquipo;
    }

    public void setCapacidadEquipo(String capacidadEquipo) {
        this.capacidadEquipo = capacidadEquipo;
    }

    public String getAnioEquipo() {
        return anioEquipo;
    }

    public void setAnioEquipo(String anioEquipo) {
        this.anioEquipo = anioEquipo;
    }

    public String getPlacaEquipo() {
        return placaEquipo;
    }

    public void setPlacaEquipo(String placaEquipo) {
        this.placaEquipo = placaEquipo;
    }

    public String getColorEquipo() {
        return colorEquipo;
    }

    public void setColorEquipo(String colorEquipo) {
        this.colorEquipo = colorEquipo;
    }

    public String getCodigoEquipo() {
        return codigoEquipo;
    }

    public void setCodigoEquipo(String codigoEquipo) {
        this.codigoEquipo = codigoEquipo;
    }

    public String getFotoEquipo() {
        return fotoEquipo;
    }

    public void setFotoEquipo(String fotoEquipo) {
        this.fotoEquipo = fotoEquipo;
    }

    public boolean isEstadoEquipo() {
        return estadoEquipo;
    }

    public void setEstadoEquipo(boolean estadoEquipo) {
        this.estadoEquipo = estadoEquipo;
    }

    public String toString() {
        return nombreEquipo + " " + marcaEquipo + " " + modeloEquipo + " " + capacidadEquipo + " " + anioEquipo + " " + placaEquipo + " " + colorEquipo + " " + codigoEquipo;
    }

    public Equipo() {

    }

}
