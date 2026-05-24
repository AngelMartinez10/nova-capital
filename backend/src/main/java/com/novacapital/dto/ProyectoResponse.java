package com.novacapital.dto;

import com.novacapital.entity.Proyecto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de respuesta con datos completos de un proyecto.
 */
public class ProyectoResponse {

    private Integer idProyecto;
    private String nombre;
    private String descripcion;
    private String categoria;
    private BigDecimal objetivoInversion;
    private BigDecimal cantidadActual;
    private BigDecimal porcentaje;
    private String estado;
    private String creadorNombre;
    private Integer idCreador;
    private LocalDateTime fechaCreacion;
    private Double mediaValoraciones;

    public static ProyectoResponse from(Proyecto p) {
        ProyectoResponse dto = new ProyectoResponse();
        dto.idProyecto       = p.getIdProyecto();
        dto.nombre           = p.getNombre();
        dto.descripcion      = p.getDescripcion();
        dto.categoria        = p.getCategoria();
        dto.objetivoInversion = p.getObjetivoInversion();
        dto.cantidadActual   = p.getCantidadActual();
        dto.porcentaje       = p.getPorcentaje();
        dto.estado           = p.getEstado().name();
        dto.fechaCreacion    = p.getFechaCreacion();
        if (p.getCliente() != null) {
            dto.creadorNombre = p.getCliente().getNombre() + " " + p.getCliente().getApellidos();
            dto.idCreador     = p.getCliente().getIdCliente();
        }
        return dto;
    }

    // Getters
    public Integer getIdProyecto() { return idProyecto; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getCategoria() { return categoria; }
    public BigDecimal getObjetivoInversion() { return objetivoInversion; }
    public BigDecimal getCantidadActual() { return cantidadActual; }
    public BigDecimal getPorcentaje() { return porcentaje; }
    public String getEstado() { return estado; }
    public String getCreadorNombre() { return creadorNombre; }
    public Integer getIdCreador() { return idCreador; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public Double getMediaValoraciones() { return mediaValoraciones; }
    public void setMediaValoraciones(Double media) { this.mediaValoraciones = media; }
}