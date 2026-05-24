package com.novacapital.controller;

import com.novacapital.dto.ApiResponse;
import com.novacapital.dto.NotificacionResponse;
import com.novacapital.service.ClienteService;
import com.novacapital.service.NotificacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;
    private final ClienteService clienteService;

    public NotificacionController(NotificacionService notificacionService,
                                  ClienteService clienteService) {
        this.notificacionService = notificacionService;
        this.clienteService      = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<NotificacionResponse>> misNotificaciones(
            @AuthenticationPrincipal UserDetails userDetails) {
        Integer idCliente = clienteService
                .obtenerPorEmail(userDetails.getUsername()).getIdCliente();
        return ResponseEntity.ok(notificacionService.obtenerPorCliente(idCliente));
    }

    @GetMapping("/no-leidas")
    public ResponseEntity<List<NotificacionResponse>> noLeidas(
            @AuthenticationPrincipal UserDetails userDetails) {
        Integer idCliente = clienteService
                .obtenerPorEmail(userDetails.getUsername()).getIdCliente();
        return ResponseEntity.ok(notificacionService.obtenerNoLeidas(idCliente));
    }

    @GetMapping("/contador")
    public ResponseEntity<Map<String, Long>> contador(
            @AuthenticationPrincipal UserDetails userDetails) {
        Integer idCliente = clienteService
                .obtenerPorEmail(userDetails.getUsername()).getIdCliente();
        long count = notificacionService.contarNoLeidas(idCliente);
        return ResponseEntity.ok(Map.of("noLeidas", count));
    }

    @PatchMapping("/leer-todas")
    public ResponseEntity<ApiResponse> leerTodas(
            @AuthenticationPrincipal UserDetails userDetails) {
        Integer idCliente = clienteService
                .obtenerPorEmail(userDetails.getUsername()).getIdCliente();
        notificacionService.marcarTodasComoLeidas(idCliente);
        return ResponseEntity.ok(new ApiResponse(true, "Todas las notificaciones marcadas como leidas"));
    }

    @PatchMapping("/{id}/leer")
    public ResponseEntity<ApiResponse> leerUna(@PathVariable Integer id) {
        notificacionService.marcarComoLeida(id);
        return ResponseEntity.ok(new ApiResponse(true, "Notificacion marcada como leida"));
    }
}