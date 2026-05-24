package com.novacapital.controller;

import com.novacapital.dto.ProyectoRequest;
import com.novacapital.dto.ProyectoResponse;
import com.novacapital.entity.Cliente;
import com.novacapital.service.ClienteService;
import com.novacapital.service.ProyectoService;
import com.novacapital.service.RetoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proyectos")
public class ProyectoController {

    private final ProyectoService proyectoService;
    private final ClienteService clienteService;
    private final RetoService retoService;

    public ProyectoController(ProyectoService proyectoService,
                              ClienteService clienteService,
                              RetoService retoService) {
        this.proyectoService = proyectoService;
        this.clienteService  = clienteService;
        this.retoService     = retoService;
    }

    @GetMapping
    public ResponseEntity<List<ProyectoResponse>> listarTodos() {
        return ResponseEntity.ok(proyectoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProyectoResponse> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(proyectoService.obtenerPorId(id));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<ProyectoResponse>> listarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(proyectoService.listarPorEstado(estado));
    }

    @GetMapping("/mios")
    public ResponseEntity<List<ProyectoResponse>> misProyectos(
            @AuthenticationPrincipal UserDetails userDetails) {
        Integer idCliente = clienteService
                .obtenerPorEmail(userDetails.getUsername()).getIdCliente();
        return ResponseEntity.ok(proyectoService.listarPorCliente(idCliente));
    }

    @PostMapping
    public ResponseEntity<ProyectoResponse> crearProyecto(
            @Valid @RequestBody ProyectoRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        // Una sola llamada a obtenerPorEmail
        Cliente cliente = clienteService.obtenerPorEmail(userDetails.getUsername());
        ProyectoResponse response = proyectoService.crearProyecto(request, userDetails.getUsername());

        long totalProyectos = proyectoService.contarProyectosPorCliente(cliente.getIdCliente());
        retoService.comprobarRetoCreadorProyectos(cliente, totalProyectos);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<ProyectoResponse> cancelarProyecto(
            @PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails) {
        ProyectoResponse response = proyectoService.cancelarProyecto(id, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }
}