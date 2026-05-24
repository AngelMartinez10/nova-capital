package com.novacapital.models;

public class Proyecto {
    private int id;
    private String nombre;
    private String descripcion;
    private double inversionObjetivo;
    private double inversionActual;
    private String estado;

    public Proyecto(String nombre, String descripcion, double inversionObjetivo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.inversionObjetivo = inversionObjetivo;
        this.inversionActual = 0;
        this.estado = "ACTIVO";
    }

    public int getPorcentajeFinanciacion() {
        if (inversionObjetivo == 0) return 0;
        return Math.min((int)((inversionActual / inversionObjetivo) * 100), 100);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public double getInversionObjetivo() { return inversionObjetivo; }
    public void setInversionObjetivo(double v) { this.inversionObjetivo = v; }
    public double getInversionActual() { return inversionActual; }
    public void setInversionActual(double v) { this.inversionActual = v; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}