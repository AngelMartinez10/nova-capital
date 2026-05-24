package com.novacapital.dto;

import jakarta.validation.constraints.*;

/**
 * DTO para enviar una valoración de un proyecto.
 */
public class PuntuacionRequest {

    @NotNull
    private Integer idProyecto;

    @NotNull(message = "El valor es obligatorio")
    @Min(value = 1, message = "La valoración mínima es 1")
    @Max(value = 5, message = "La valoración máxima es 5")
    private Integer valor;

    private String comentario;

    public Integer getIdProyecto() { return idProyecto; }
    public void setIdProyecto(Integer idProyecto) { this.idProyecto = idProyecto; }

    public Integer getValor() { return valor; }
    public void setValor(Integer valor) { this.valor = valor; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
}