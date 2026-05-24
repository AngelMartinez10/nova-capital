package com.novacapital.models;

import java.math.BigDecimal;

public class InversionRequest {
    private int idProyecto;
    private BigDecimal cantidad;

    public InversionRequest(int idProyecto, BigDecimal cantidad) {
        this.idProyecto = idProyecto;
        this.cantidad   = cantidad;
    }
}