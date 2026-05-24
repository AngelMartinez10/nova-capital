package com.novacapital.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Inversion")
public class Inversion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inversion")
    private Integer idInversion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proyecto", nullable = false)
    private Proyecto proyecto;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cantidad;

    @Column(name = "fecha_inversion", updatable = false)
    private LocalDateTime fechaInversion;

    @PrePersist
    protected void onCreate() {
        fechaInversion = LocalDateTime.now();
    }

    public Inversion() {}

    public Integer getIdInversion() { return idInversion; }
    public void setIdInversion(Integer idInversion) { this.idInversion = idInversion; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Proyecto getProyecto() { return proyecto; }
    public void setProyecto(Proyecto proyecto) { this.proyecto = proyecto; }
    public BigDecimal getCantidad() { return cantidad; }
    public void setCantidad(BigDecimal cantidad) { this.cantidad = cantidad; }
    public LocalDateTime getFechaInversion() { return fechaInversion; }
    public void setFechaInversion(LocalDateTime fechaInversion) { this.fechaInversion = fechaInversion; }
}