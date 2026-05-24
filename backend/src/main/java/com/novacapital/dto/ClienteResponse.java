package com.novacapital.dto;

import com.novacapital.entity.Cliente;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de respuesta con datos del cliente (nunca devuelve la contraseña).
 */
public class ClienteResponse {

    private Integer idCliente;
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;
    private BigDecimal saldoAurus;
    private LocalDateTime fechaRegistro;

    // Método estático para construir desde entidad
    public static ClienteResponse from(Cliente cliente, BigDecimal saldo) {
        ClienteResponse dto = new ClienteResponse();
        dto.idCliente    = cliente.getIdCliente();
        dto.nombre       = cliente.getNombre();
        dto.apellidos    = cliente.getApellidos();
        dto.email        = cliente.getEmail();
        dto.telefono     = cliente.getTelefono();
        dto.saldoAurus   = saldo;
        dto.fechaRegistro = cliente.getFechaRegistro();
        return dto;
    }

    public Integer getIdCliente() { return idCliente; }
    public String getNombre() { return nombre; }
    public String getApellidos() { return apellidos; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }
    public BigDecimal getSaldoAurus() { return saldoAurus; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
}