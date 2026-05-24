package com.novacapital.dto;

import com.novacapital.entity.Notificacion;
import java.time.LocalDateTime;

public class NotificacionResponse {

    private Integer idNotificacion;
    private String mensaje;
    private Boolean leido;
    private LocalDateTime fecha;

    public static NotificacionResponse from(Notificacion n) {
        NotificacionResponse dto = new NotificacionResponse();
        dto.idNotificacion = n.getIdNotificacion();
        dto.mensaje        = n.getMensaje();
        dto.leido          = n.getLeido();
        dto.fecha          = n.getFecha();
        return dto;
    }

    public Integer getIdNotificacion() { return idNotificacion; }
    public String getMensaje()         { return mensaje; }
    public Boolean getLeido()          { return leido; }
    public LocalDateTime getFecha()    { return fecha; }
}