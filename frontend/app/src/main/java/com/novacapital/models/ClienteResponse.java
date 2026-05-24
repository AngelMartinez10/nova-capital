package com.novacapital.models;

import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;

public class ClienteResponse {
    @SerializedName("idCliente")    private int idCliente;
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;
    @SerializedName("saldoAurus")   private BigDecimal saldoAurus;
    @SerializedName("fechaRegistro") private String fechaRegistro;

    public int getIdCliente()        { return idCliente; }
    public String getNombre()        { return nombre; }
    public String getApellidos()     { return apellidos; }
    public String getEmail()         { return email; }
    public String getTelefono()      { return telefono; }
    public BigDecimal getSaldoAurus(){ return saldoAurus; }
    public String getFechaRegistro() { return fechaRegistro; }
}