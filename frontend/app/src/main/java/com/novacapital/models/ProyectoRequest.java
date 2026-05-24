package com.novacapital.models;

import java.math.BigDecimal;

public class ProyectoRequest {
    private String nombre;
    private String descripcion;
    private String categoria;
    private BigDecimal objetivoInversion;
    private BigDecimal rendimientoMensual;

    public ProyectoRequest(String nombre, String descripcion,
                           String categoria, BigDecimal objetivoInversion,
                           BigDecimal rendimientoMensual) {
        this.nombre             = nombre;
        this.descripcion        = descripcion;
        this.categoria          = categoria;
        this.objetivoInversion  = objetivoInversion;
        this.rendimientoMensual = rendimientoMensual;
    }
}