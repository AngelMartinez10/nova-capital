package com.novacapital.dto;

import com.novacapital.entity.Puntuacion;
import java.time.LocalDateTime;

public class PuntuacionResponse {

    private Integer idPuntuacion;
    private Integer idProyecto;
    private String nombreProyecto;
    private Integer valor;
    private String comentario;
    private LocalDateTime fecha;

    public static PuntuacionResponse from(Puntuacion p) {
        PuntuacionResponse dto = new PuntuacionResponse();
        dto.idPuntuacion   = p.getIdPuntuacion();
        dto.idProyecto     = p.getProyecto().getIdProyecto();
        dto.nombreProyecto = p.getProyecto().getNombre();
        dto.valor          = p.getValor();
        dto.comentario     = p.getComentario();
        dto.fecha          = p.getFecha();
        return dto;
    }

    public Integer getIdPuntuacion()   { return idPuntuacion; }
    public Integer getIdProyecto()     { return idProyecto; }
    public String getNombreProyecto()  { return nombreProyecto; }
    public Integer getValor()          { return valor; }
    public String getComentario()      { return comentario; }
    public LocalDateTime getFecha()    { return fecha; }
}