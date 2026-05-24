package com.novacapital.models;

import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;

public class ClienteRetoResponse {

    @SerializedName("idReto")       private int idReto;
    @SerializedName("titulo")       private String titulo;
    @SerializedName("descripcion")  private String descripcion;
    @SerializedName("recompensa")   private BigDecimal recompensa;
    @SerializedName("completado")   private boolean completado;
    @SerializedName("fechaCompletado") private String fechaCompletado;

    public int getIdReto()              { return idReto; }
    public String getTitulo()           { return titulo; }
    public String getDescripcion()      { return descripcion; }
    public BigDecimal getRecompensa()   { return recompensa; }
    public boolean isCompletado()       { return completado; }
    public String getFechaCompletado()  { return fechaCompletado; }
}