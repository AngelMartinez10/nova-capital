package com.novacapital.service;

import com.novacapital.dto.NotificacionResponse;
import com.novacapital.entity.Cliente;
import com.novacapital.entity.Notificacion;
import com.novacapital.exception.ResourceNotFoundException;
import com.novacapital.repository.NotificacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepo;

    public NotificacionService(NotificacionRepository notificacionRepo) {
        this.notificacionRepo = notificacionRepo;
    }

    // Convierte la lista de entidades Notificacion a lista de DTOs NotificacionResponse
    public List<NotificacionResponse> obtenerPorCliente(Integer idCliente) {
        List<Notificacion> notificaciones = notificacionRepo
                .findByClienteIdClienteOrderByFechaDesc(idCliente);

        List<NotificacionResponse> resultado = new ArrayList<>();
        for (Notificacion n : notificaciones) {
            resultado.add(NotificacionResponse.from(n));
        }
        return resultado;
    }

    // Convierte solo las notificaciones no leídas a lista de DTOs
    public List<NotificacionResponse> obtenerNoLeidas(Integer idCliente) {
        List<Notificacion> notificaciones = notificacionRepo
                .findByClienteIdClienteAndLeidoFalse(idCliente);

        List<NotificacionResponse> resultado = new ArrayList<>();
        for (Notificacion n : notificaciones) {
            resultado.add(NotificacionResponse.from(n));
        }
        return resultado;
    }

    public long contarNoLeidas(Integer idCliente) {
        return notificacionRepo.countByClienteIdClienteAndLeidoFalse(idCliente);
    }

    @Transactional
    public void marcarTodasComoLeidas(Integer idCliente) {
        notificacionRepo.marcarTodasComoLeidas(idCliente);
    }

    @Transactional
    public void marcarComoLeida(Integer idNotificacion) {
        Notificacion n = notificacionRepo.findById(idNotificacion)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Notificacion no encontrada: " + idNotificacion));
        n.setLeido(true);
        notificacionRepo.save(n);
    }

    @Transactional
    public void crearNotificacion(Cliente cliente, String mensaje) {
        notificacionRepo.save(new Notificacion(cliente, mensaje));
    }
}