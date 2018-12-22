package com.example.witch.gtslsac_app_1.mVolley;

/**
 * Created by Witchraper on 15/05/2017.
 */

public class Config {
    //URL to our login.php file
    //local
    //private static String URL = "http://10.0.2.2:8080/AppMovil/php/";
    //private static String URL= "http://gtslsacapp.000webhostapp.com/AppMovil/php/";
    //private static String URL = "http://appinmobile.tk/AppMovil/php/";
    private static String URL = "http://gtslsac.com/AppMovil/php/";
    public static final String ADAPTADOR_URL = URL + "adaptador.php";
    public static final String UPLOAD_URL_IMG_CLIENTES = URL + "adaptadorImgClientes.php";
    public static final String UPLOAD_URL_IMG_OPERADORES = URL + "adaptadorImgOperadores.php";
    public static final String UPLOAD_URL_IMG_EQUIPOS = URL + "adaptadorImgEquipos.php";

    //Keys for Sharedpreferences
    //Para mantener la sesion iniciada en el dispositivo
    public static final String SHARED_PREF_NAME = "myloginapp";
    //This would be used to store the email of current logged in user
    public static final String EMAIL_SHARED_PREF = "Correo";
    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";
    //PARA SABER SI SE ELIMINO O MODIFICO UN REGISTRO
    public static final String REGISTRO_CAMBIADO = "registroCambiado";
    //PARA SABER SI SE ACCEDIO A UN FRAGMENT EN EL PROCESO DE ALQUILER
    public static final String PROCESO_DE_ALQUILER = "procesoAlquiler";
    //PARA SABER SI SE ACCEDIO A UN FRAGMENT EN EL PROCESO DE MANTENIMIENTO
    public static final String PROCESO_DE_MANTENIMIENTO = "procesoMantenimiento";
    //PARA SABER SI ES EQUIPO, TRACTO, OPERADOR o AYUDANTE
    public static final String OPCION_DETALLE_ALQUILER = "opcionDetalleAlquiler";

    public static final String ID_USUARIO_ALQUILER = "id_usuario_alquiler";
    public static final String NOMBRE_USUARIO_ALQUILER = "nombre_usuario_alquiler";
    public static final String ID_CLIENTE_ALQUILER = "id_cliente_alquiler";
    public static final String NOMBRE_CLIENTE_ALQUILER = "nombre_cliente_alquiler";
    public static final String ID_EQUIPO_ALQUILER = "id_equipo_alquiler";
    public static final String NOMBRE_EQUIPO_ALQUILER = "nombre_equipo_alquiler";
    public static final String ID_TRACTO_ALQUILER = "id_tracto_alquiler";
    public static final String NOMBRE_TRACTO_ALQUILER = "nombre_tracto_alquiler";
    public static final String ID_OPERADOR_ALQUILER = "id_operador_alquiler";
    public static final String NOMBRE_OPERADOR_ALQUILER = "nombre_operador_alquiler";
    public static final String ID_AYUDANTE_ALQUILER = "id_ayudante_alquiler";
    public static final String NOMBRE_AYUDANTE_ALQUILER = "nombre_ayudante_alquiler";

    public static final String ID_EQUIPO_MANTENIMIENTO = "id_equipo_mantenimiento";
    public static final String NOMBRE_EQUIPO_MANTENIMIENTO = "nombre_equipo_mantenimiento";



}
