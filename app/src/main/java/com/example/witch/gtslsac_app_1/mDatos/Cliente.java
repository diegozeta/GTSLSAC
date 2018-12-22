package com.example.witch.gtslsac_app_1.mDatos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by witch on 05/06/2017.
 */
public class Cliente {
    private int idEmpresa;
    private String nombreEmpresa;
    private String logoEmpresa;

    public Cliente(int idEmpresa, String nombreEmpresa, String logoEmpresa) {
        this.idEmpresa = idEmpresa;
        this.nombreEmpresa = nombreEmpresa;
        this.logoEmpresa = logoEmpresa;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getLogoEmpresa() {
        return logoEmpresa;
    }

    public void setLogoEmpresa(String logoEmpresa) {
        this.logoEmpresa = logoEmpresa;
    }

    public String toString(){
        return nombreEmpresa;
    }
    public Cliente() {
    }
}
