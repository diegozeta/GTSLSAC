package com.example.witch.gtslsac_app_1.mDatos;

import java.util.ArrayList;

/**
 * Created by witch on 14/06/2017.
 */

public class ReporteNotificacionCollection {
    static ArrayList<ReporteNotificacion> reporteNotificaciones = new ArrayList<>();

    public static ArrayList<ReporteNotificacion> getReporteNotificaciones(){
        return reporteNotificaciones;
    }
    public static void deleteAll(){
        reporteNotificaciones.clear();
    }
}
