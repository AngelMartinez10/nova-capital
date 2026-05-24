package com.novacapital.models;

import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;

public class InversionResponse {
    @SerializedName("idInversion")     private int idInversion;
    @SerializedName("idProyecto")      private int idProyecto;
    @SerializedName("nombreProyecto")  private String nombreProyecto;
    private BigDecimal cantidad;
    @SerializedName("fechaInversion")  private String fechaInversion;
    @SerializedName("nuevoSaldoAurus") private BigDecimal nuevoSaldoAurus;

    public int getIdInversion()          { return idInversion; }
    public int getIdProyecto()           { return idProyecto; }
    public String getNombreProyecto()    { return nombreProyecto; }
    public BigDecimal getCantidad()      { return cantidad; }
    public String getFechaInversion()    { return fechaInversion; }
    public BigDecimal getNuevoSaldoAurus(){ return nuevoSaldoAurus; }
}