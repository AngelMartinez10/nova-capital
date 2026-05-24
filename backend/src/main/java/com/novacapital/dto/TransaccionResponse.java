package com.novacapital.dto;

import com.novacapital.entity.Transaccion;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransaccionResponse {

    private Integer idTransaccion;
    private String tipo;
    private BigDecimal cantidad;
    private BigDecimal saldoAnterior;
    private BigDecimal saldoPosterior;
    private String descripcion;
    private LocalDateTime fecha;

    public static TransaccionResponse from(Transaccion t) {
        TransaccionResponse dto = new TransaccionResponse();
        dto.idTransaccion  = t.getIdTransaccion();
        dto.tipo           = t.getTipo().name();
        dto.cantidad       = t.getCantidad();
        dto.saldoAnterior  = t.getSaldoAnterior();
        dto.saldoPosterior = t.getSaldoPosterior();
        dto.descripcion    = t.getDescripcion();
        dto.fecha          = t.getFecha();
        return dto;
    }

    public Integer getIdTransaccion() { return idTransaccion; }
    public String getTipo() { return tipo; }
    public BigDecimal getCantidad() { return cantidad; }
    public BigDecimal getSaldoAnterior() { return saldoAnterior; }
    public BigDecimal getSaldoPosterior() { return saldoPosterior; }
    public String getDescripcion() { return descripcion; }
    public LocalDateTime getFecha() { return fecha; }
}