package com.novacapital.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Proyecto")
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proyecto")
    private Integer idProyecto;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(length = 100)
    private String categoria;

    @Column(name = "objetivo_inversion", nullable = false, precision = 12, scale = 2)
    private BigDecimal objetivoInversion;

    @Column(name = "cantidad_actual", nullable = false, precision = 12, scale = 2)
    private BigDecimal cantidadActual = BigDecimal.ZERO;

    @Column(name = "porcentaje", insertable = false, updatable = false, precision = 5, scale = 2)
    private BigDecimal porcentaje;

    @Column(name = "rendimiento_mensual", nullable = false, precision = 5, scale = 2)
    private BigDecimal rendimientoMensual = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoProyecto estado = EstadoProyecto.ACTIVO;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @OneToMany(mappedBy = "proyecto", fetch = FetchType.LAZY)
    private List<Inversion> inversiones;

    @OneToMany(mappedBy = "proyecto", fetch = FetchType.LAZY)
    private List<Puntuacion> puntuaciones;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    public enum EstadoProyecto {
        ACTIVO, EN_PROGRESO, FINANCIADO, CANCELADO
    }

    public Integer getIdProyecto() { return idProyecto; }
    public void setIdProyecto(Integer idProyecto) { this.idProyecto = idProyecto; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public BigDecimal getObjetivoInversion() { return objetivoInversion; }
    public void setObjetivoInversion(BigDecimal objetivoInversion) { this.objetivoInversion = objetivoInversion; }
    public BigDecimal getCantidadActual() { return cantidadActual; }
    public void setCantidadActual(BigDecimal cantidadActual) { this.cantidadActual = cantidadActual; }
    public BigDecimal getPorcentaje() { return porcentaje; }
    public BigDecimal getRendimientoMensual() { return rendimientoMensual; }
    public void setRendimientoMensual(BigDecimal rendimientoMensual) { this.rendimientoMensual = rendimientoMensual; }
    public EstadoProyecto getEstado() { return estado; }
    public void setEstado(EstadoProyecto estado) { this.estado = estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public List<Inversion> getInversiones() { return inversiones; }
    public void setInversiones(List<Inversion> inversiones) { this.inversiones = inversiones; }
    public List<Puntuacion> getPuntuaciones() { return puntuaciones; }
    public void setPuntuaciones(List<Puntuacion> puntuaciones) { this.puntuaciones = puntuaciones; }
}