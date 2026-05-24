package com.novacapital.models;

public class Usuario {
    private int id;
    private String nombre;
    private double saldoAurus;

    public Usuario(String nombre, double saldoAurus) {
        this.nombre = nombre;
        this.saldoAurus = saldoAurus;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public double getSaldoAurus() { return saldoAurus; }
    public void setSaldoAurus(double saldoAurus) { this.saldoAurus = saldoAurus; }
}