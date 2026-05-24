package com.novacapital.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "Reto")
public class Reto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reto")
    private Integer idReto;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal recompensa;

    @Column(nullable = false)
    private Boolean activo = true;

    @OneToMany(mappedBy = "reto", fetch = FetchType.LAZY)
    private List<ClienteReto> clienteRetos;

    public Reto() {}

    public Integer getIdReto() { return idReto; }
    public void setIdReto(Integer idReto) { this.idReto = idReto; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public BigDecimal getRecompensa() { return recompensa; }
    public void setRecompensa(BigDecimal recompensa) { this.recompensa = recompensa; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public List<ClienteReto> getClienteRetos() { return clienteRetos; }
    public void setClienteRetos(List<ClienteReto> clienteRetos) { this.clienteRetos = clienteRetos; }
}