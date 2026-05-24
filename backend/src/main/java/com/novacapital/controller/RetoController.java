package com.novacapital.controller;

import com.novacapital.dto.ClienteRetoResponse;
import com.novacapital.entity.Reto;
import com.novacapital.service.ClienteService;
import com.novacapital.service.RetoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/retos")
public class RetoController {

    private final RetoService retoService;
    private final ClienteService clienteService;

    public RetoController(RetoService retoService, ClienteService clienteService) {
        this.retoService    = retoService;
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<Reto>> listarRetos() {
        return ResponseEntity.ok(retoService.obtenerRetosDisponibles());
    }

    @GetMapping("/mios")
    public ResponseEntity<List<ClienteRetoResponse>> misRetos(
            @AuthenticationPrincipal UserDetails userDetails) {
        Integer idCliente = clienteService
                .obtenerPorEmail(userDetails.getUsername()).getIdCliente();
        return ResponseEntity.ok(retoService.obtenerRetosDeCliente(idCliente));
    }
}