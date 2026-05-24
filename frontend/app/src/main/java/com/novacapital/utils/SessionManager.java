package com.novacapital.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME  = "NovaCapitalSession";
    private static final String KEY_TOKEN  = "jwt_token";
    private static final String KEY_NOMBRE = "nombre";
    private static final String KEY_SALDO  = "saldo";
    private static final String KEY_ID     = "id_cliente";

    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void guardarSesion(String token, int idCliente, String nombre, String saldo) {
        prefs.edit()
                .putString(KEY_TOKEN, token)
                .putInt(KEY_ID, idCliente)
                .putString(KEY_NOMBRE, nombre)
                .putString(KEY_SALDO, saldo)
                .apply();
    }

    public String getToken()  { return prefs.getString(KEY_TOKEN, null); }
    public int getIdCliente() { return prefs.getInt(KEY_ID, -1); }
    public String getNombre() { return prefs.getString(KEY_NOMBRE, "Usuario"); }
    public String getSaldo()  { return prefs.getString(KEY_SALDO, "0"); }

    public boolean haySesion() { return getToken() != null; }

    public void cerrarSesion() {
        prefs.edit().clear().apply();
    }
}