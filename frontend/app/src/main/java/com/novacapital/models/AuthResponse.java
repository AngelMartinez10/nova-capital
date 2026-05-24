package com.novacapital.models;

import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;

public class AuthResponse {
    private String token;
    @SerializedName("idCliente")  private int idCliente;
    private String nombre;
    private String email;
    @SerializedName("saldoAurus") private BigDecimal saldoAurus;

    public String getToken()         { return token; }
    public int getIdCliente()        { return idCliente; }
    public String getNombre()        { return nombre; }
    public String getEmail()         { return email; }
    public BigDecimal getSaldoAurus(){ return saldoAurus; }
}