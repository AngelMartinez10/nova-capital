package com.novacapital.dto;

import com.novacapital.entity.Inversion;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de respuesta de una inversión realizada.
 */
public class InversionResponse {

    private Integer idInversion;
    private Integer idProyecto;
    private String nombreProyecto;
    private BigDecimal cantidad;
    private LocalDateTime fechaInversion;
    private BigDecimal nuevoSaldoAurus;

    public static InversionResponse from(Inversion inv) {
        InversionResponse dto = new InversionResponse();
        dto.idInversion    = inv.getIdInversion();
        dto.idProyecto     = inv.getProyecto().getIdProyecto();
        dto.nombreProyecto = inv.getProyecto().getNombre();
        dto.cantidad       = inv.getCantidad();
        dto.fechaInversion = inv.getFechaInversion();
        return dto;
    }

    public Integer getIdInversion() { return idInversion; }
    public Integer getIdProyecto() { return idProyecto; }
    public String getNombreProyecto() { return nombreProyecto; }
    public BigDecimal getCantidad() { return cantidad; }
    public LocalDateTime getFechaInversion() { return fechaInversion; }
    public BigDecimal getNuevoSaldoAurus() { return nuevoSaldoAurus; }
    public void setNuevoSaldoAurus(BigDecimal saldo) { this.nuevoSaldoAurus = saldo; }
}