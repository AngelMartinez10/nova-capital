package com.novacapital.dto;

import com.novacapital.entity.ClienteReto;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ClienteRetoResponse {

    private Integer idReto;
    private String titulo;
    private String descripcion;
    private BigDecimal recompensa;
    private Boolean completado;
    private LocalDateTime fechaCompletado;

    public static ClienteRetoResponse from(ClienteReto cr) {
        ClienteRetoResponse dto = new ClienteRetoResponse();
        dto.idReto          = cr.getReto().getIdReto();
        dto.titulo          = cr.getReto().getTitulo();
        dto.descripcion     = cr.getReto().getDescripcion();
        dto.recompensa      = cr.getReto().getRecompensa();
        dto.completado      = cr.getCompletado();
        dto.fechaCompletado = cr.getFechaCompletado();
        return dto;
    }

    public Integer getIdReto()             { return idReto; }
    public String getTitulo()              { return titulo; }
    public String getDescripcion()         { return descripcion; }
    public BigDecimal getRecompensa()      { return recompensa; }
    public Boolean getCompletado()         { return completado; }
    public LocalDateTime getFechaCompletado() { return fechaCompletado; }
}