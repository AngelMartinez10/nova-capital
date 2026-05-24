package com.novacapital.models;

public class Reto {
    private int id;
    private String titulo;
    private double recompensa;
    private boolean completado;

    public Reto(String titulo, double recompensa) {
        this.titulo = titulo;
        this.recompensa = recompensa;
        this.completado = false;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public double getRecompensa() { return recompensa; }
    public void setRecompensa(double recompensa) { this.recompensa = recompensa; }
    public boolean isCompletado() { return completado; }
    public void setCompletado(boolean completado) { this.completado = completado; }
}