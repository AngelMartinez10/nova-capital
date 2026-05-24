package com.novacapital.controller;

import com.novacapital.dto.ClienteResponse;
import com.novacapital.dto.TransaccionResponse;
import com.novacapital.repository.TransaccionRepository;
import com.novacapital.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final TransaccionRepository transaccionRepo;

    public ClienteController(ClienteService clienteService,
                             TransaccionRepository transaccionRepo) {
        this.clienteService  = clienteService;
        this.transaccionRepo = transaccionRepo;
    }

    @GetMapping("/perfil")
    public ResponseEntity<ClienteResponse> miPerfil(
            @AuthenticationPrincipal UserDetails userDetails) {
        ClienteResponse response = clienteService.obtenerPerfil(
                clienteService.obtenerPorEmail(userDetails.getUsername()).getIdCliente());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/perfil/{id}")
    public ResponseEntity<ClienteResponse> perfilPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(clienteService.obtenerPerfil(id));
    }

    @GetMapping("/transacciones")
    public ResponseEntity<List<TransaccionResponse>> misTransacciones(
            @AuthenticationPrincipal UserDetails userDetails) {
        Integer idCliente = clienteService
                .obtenerPorEmail(userDetails.getUsername()).getIdCliente();
        List<TransaccionResponse> resultado = transaccionRepo
                .findByClienteIdClienteOrderByFechaDesc(idCliente)
                .stream()
                .map(TransaccionResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resultado);
    }
}