package com.example.witch.gtslsac_app_1.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.witch.gtslsac_app_1.Fragments.Alquiler_fragment;
import com.example.witch.gtslsac_app_1.Fragments.Clientes_fragment;
import com.example.witch.gtslsac_app_1.Fragments.Equipos_fragment;
import com.example.witch.gtslsac_app_1.Fragments.Mantenimiento_fragment;
import com.example.witch.gtslsac_app_1.Fragments.Operadores_fragment;
import com.example.witch.gtslsac_app_1.Fragments.Reportes_Equipo_fragment;
import com.example.witch.gtslsac_app_1.Fragments.Reportes_Operador_fragment;
import com.example.witch.gtslsac_app_1.R;
import com.example.witch.gtslsac_app_1.mVolley.Config;
import com.example.witch.gtslsac_app_1.mVolley.OperacionesVolley;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    OperacionesVolley ov = new OperacionesVolley();
    int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString(getString(R.string.FCM_TOKEN), "default");
        final String cuentaConToken = sharedPreferences.getString(getString(R.string.CUENTA_CON_TOKEN), "NO");
        SharedPreferences sharedPreferences1 = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        idUsuario = sharedPreferences1.getInt(Config.ID_USUARIO_ALQUILER, 0);
        Log.e("ID USUARIO LOGUEADO", String.valueOf(idUsuario));
        if (cuentaConToken.equals("SI")) {
            Log.e("ESTADO", "YA CUENTA CON TOKEN");
        } else {
            ov.insertarToken(getApplicationContext(), idUsuario, token);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getString(R.string.CUENTA_CON_TOKEN), "SI");
            editor.commit();
        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private void changeSearchViewTextColor(View view) {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // update the observer here (aka nested fragments)
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menuLogout) {
            logout();
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(Config.PROCESO_DE_ALQUILER, 0);
        editor.putInt(Config.PROCESO_DE_MANTENIMIENTO, 0);
        editor.commit();
        int enAlquiler = sharedPref.getInt(Config.PROCESO_DE_ALQUILER, 0);
        Log.e("RESETEANDO ALQUILER", String.valueOf(enAlquiler));
        int enMantenimiento = sharedPref.getInt(Config.PROCESO_DE_MANTENIMIENTO, 0);
        Log.e("RESETEANDO MANTENMIENTO", String.valueOf(enMantenimiento));

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (id == R.id.nav_alquiler) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new Alquiler_fragment()).commit();
        } else if (id == R.id.nav_mantenimiento) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new Mantenimiento_fragment()).commit();
        } else if (id == R.id.nav_equipos) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new Equipos_fragment()).commit();
        } else if (id == R.id.nav_operadores) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new Operadores_fragment()).commit();
        } else if (id == R.id.nav_clientes) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new Clientes_fragment()).commit();
            /*Intent newAct = new Intent(getApplicationContext(), ClientesActivity.class);
            startActivity(newAct);*/
        } else if (id == R.id.nav_rep_operadores) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new Reportes_Operador_fragment()).commit();
        } else if (id == R.id.nav_rep_equipos) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new Reportes_Equipo_fragment()).commit();
        } else if (id == R.id.nav_logout) {
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    ////Logout function
    private void logout() {
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("DESEA CERRAR SESION?");
        alertDialogBuilder.setPositiveButton("SI",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt(Config.ID_USUARIO_ALQUILER, 0);
                        editor.putString(Config.NOMBRE_USUARIO_ALQUILER, "");
                        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);
                        editor.putString(Config.EMAIL_SHARED_PREF, "");
                        editor.commit();

                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
