package com.novacapital.models;

public class RegistroRequest {
    private String nombre;
    private String apellidos;
    private String email;
    private String contrasena;
    private String telefono;

    public RegistroRequest(String nombre, String apellidos,
                           String email, String contrasena, String telefono) {
        this.nombre     = nombre;
        this.apellidos  = apellidos;
        this.email      = email;
        this.contrasena = contrasena;
        this.telefono   = telefono;
    }
}