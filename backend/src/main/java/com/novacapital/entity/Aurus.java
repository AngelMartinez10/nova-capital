package com.novacapital.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Aurus")
public class Aurus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aurus")
    private Integer idAurus;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false, unique = true)
    private Cliente cliente;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal saldo = new BigDecimal("1000.00");

    public Aurus() {}

    public Aurus(Cliente cliente) {
        this.cliente = cliente;
        this.saldo = new BigDecimal("1000.00");
    }

    public Integer getIdAurus() { return idAurus; }
    public void setIdAurus(Integer idAurus) { this.idAurus = idAurus; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }
}