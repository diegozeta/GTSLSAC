package com.example.witch.gtslsac_app_1.mDatos;

import java.util.ArrayList;
//CLASE MOCK PARA RETENER LOS DATOS DE FORMA LOCAL ANTES DE REALIZAR UNA OPERACION EN LA DB

/**
 * Created by witch on 14/06/2017.
 */

public class ReporteNotificacionCRUD {
    ArrayList<ReporteNotificacion> reporteNotificaciones;

    public ReporteNotificacionCRUD(ArrayList<ReporteNotificacion> ReporteNotificacion) {
        this.reporteNotificaciones = ReporteNotificacion;
    }
    //AGREGAR
    public boolean addNew(ReporteNotificacion ReporteNotificacion) {
        try {
            reporteNotificaciones.add(ReporteNotificacion);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }
    //Listar
    public ArrayList<ReporteNotificacion> getDetalleAlquileres() {
        return reporteNotificaciones;
    }
    public void deleteAll(){
        reporteNotificaciones.clear();
    }
    //Actualizar
    public boolean update(int position, ReporteNotificacion newReporteNotificacion) {
        try {
            reporteNotificaciones.remove(position);
            reporteNotificaciones.add(position, newReporteNotificacion);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    //Eliminar
    public boolean delete(int position) {
        try {
            reporteNotificaciones.remove(position);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

}
