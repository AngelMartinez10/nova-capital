package com.novacapital.dto;

import java.math.BigDecimal;

/**
 * DTO de respuesta al hacer login o registro exitoso.
 * Devuelve el token JWT y datos básicos del cliente.
 */
public class AuthResponse {

    private String token;
    private Integer idCliente;
    private String nombre;
    private String email;
    private BigDecimal saldoAurus;

    public AuthResponse(String token, Integer idCliente, String nombre, String email, BigDecimal saldoAurus) {
        this.token = token;
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.email = email;
        this.saldoAurus = saldoAurus;
    }

    public String getToken() { return token; }
    public Integer getIdCliente() { return idCliente; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public BigDecimal getSaldoAurus() { return saldoAurus; }
}