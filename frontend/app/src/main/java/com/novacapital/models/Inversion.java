package com.novacapital.models;

public class Inversion {
    private int id;
    private int idProyecto;
    private double cantidad;
    private String fecha;

    public Inversion(int idProyecto, double cantidad, String fecha) {
        this.idProyecto = idProyecto;
        this.cantidad = cantidad;
        this.fecha = fecha;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdProyecto() { return idProyecto; }
    public void setIdProyecto(int idProyecto) { this.idProyecto = idProyecto; }
    public double getCantidad() { return cantidad; }
    public void setCantidad(double cantidad) { this.cantidad = cantidad; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
}