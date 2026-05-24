package com.novacapital.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * DTO para crear o actualizar un proyecto.
 */
public class ProyectoRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 150)
    private String nombre;

    private String descripcion;

    @Size(max = 100)
    private String categoria;

    @NotNull(message = "El objetivo de inversión es obligatorio")
    @DecimalMin(value = "0.01", message = "El objetivo debe ser mayor que 0")
    private BigDecimal objetivoInversion;

    @NotNull(message = "El rendimiento mensual es obligatorio")
    @DecimalMin(value = "0.5", message = "El rendimiento mínimo es 0.5%")
    @DecimalMax(value = "2.0", message = "El rendimiento máximo es 2%")
    private BigDecimal rendimientoMensual;

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public BigDecimal getObjetivoInversion() { return objetivoInversion; }
    public void setObjetivoInversion(BigDecimal objetivoInversion) { this.objetivoInversion = objetivoInversion; }

    public BigDecimal getRendimientoMensual() { return rendimientoMensual; }
    public void setRendimientoMensual(BigDecimal rendimientoMensual) { this.rendimientoMensual = rendimientoMensual; }
}