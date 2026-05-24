package com.novacapital.models;

import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;

public class ProyectoResponse {
    @SerializedName("idProyecto")        private int idProyecto;
    private String nombre;
    private String descripcion;
    private String categoria;
    @SerializedName("objetivoInversion") private BigDecimal objetivoInversion;
    @SerializedName("cantidadActual")    private BigDecimal cantidadActual;
    private BigDecimal porcentaje;
    private String estado;
    @SerializedName("creadorNombre")     private String creadorNombre;

    public int getIdProyecto()              { return idProyecto; }
    public String getNombre()               { return nombre; }
    public String getDescripcion()          { return descripcion; }
    public String getCategoria()            { return categoria; }
    public BigDecimal getObjetivoInversion(){ return objetivoInversion; }
    public BigDecimal getCantidadActual()   { return cantidadActual; }
    public BigDecimal getPorcentaje()       { return porcentaje; }
    public String getEstado()               { return estado; }
    public String getCreadorNombre()        { return creadorNombre; }

    public int getPorcentajeFinanciacion() {
        if (objetivoInversion == null || objetivoInversion.doubleValue() == 0) return 0;
        int p = (int)((cantidadActual.doubleValue() / objetivoInversion.doubleValue()) * 100);
        return Math.min(p, 100);
    }
}