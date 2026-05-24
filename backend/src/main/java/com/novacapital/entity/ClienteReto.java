package com.novacapital.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "Cliente_Reto")
public class ClienteReto {

    @EmbeddedId
    private ClienteRetoId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idCliente")
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idReto")
    @JoinColumn(name = "id_reto")
    private Reto reto;

    @Column(nullable = false)
    private Boolean completado = false;

    @Column(name = "fecha_completado")
    private LocalDateTime fechaCompletado;

    public ClienteReto() {}

    public ClienteRetoId getId() { return id; }
    public void setId(ClienteRetoId id) { this.id = id; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Reto getReto() { return reto; }
    public void setReto(Reto reto) { this.reto = reto; }
    public Boolean getCompletado() { return completado; }
    public void setCompletado(Boolean completado) { this.completado = completado; }
    public LocalDateTime getFechaCompletado() { return fechaCompletado; }
    public void setFechaCompletado(LocalDateTime fechaCompletado) { this.fechaCompletado = fechaCompletado; }

    @Embeddable
    public static class ClienteRetoId implements Serializable {

        @Column(name = "id_cliente")
        private Integer idCliente;

        @Column(name = "id_reto")
        private Integer idReto;

        public ClienteRetoId() {}

        public ClienteRetoId(Integer idCliente, Integer idReto) {
            this.idCliente = idCliente;
            this.idReto = idReto;
        }

        public Integer getIdCliente() { return idCliente; }
        public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }
        public Integer getIdReto() { return idReto; }
        public void setIdReto(Integer idReto) { this.idReto = idReto; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ClienteRetoId)) return false;
            ClienteRetoId that = (ClienteRetoId) o;
            return Objects.equals(idCliente, that.idCliente) && Objects.equals(idReto, that.idReto);
        }

        @Override
        public int hashCode() {
            return Objects.hash(idCliente, idReto);
        }
    }
}