package com.novacapital.controller;

import com.novacapital.dto.InversionRequest;
import com.novacapital.dto.InversionResponse;
import com.novacapital.service.ClienteService;
import com.novacapital.service.InversionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller de inversiones.
 *
 * POST /api/inversiones              → Realizar una inversión
 * GET  /api/inversiones/mias         → Historial del cliente autenticado
 * GET  /api/inversiones/proyecto/{id}→ Inversiones de un proyecto
 */
@RestController
@RequestMapping("/api/inversiones")
public class InversionController {

    private final InversionService inversionService;
    private final ClienteService clienteService;

    public InversionController(InversionService inversionService,
                               ClienteService clienteService) {
        this.inversionService = inversionService;
        this.clienteService   = clienteService;
    }

    /**
     * POST /api/inversiones
     *
     * Body JSON:
     * {
     *   "idProyecto": 1,
     *   "cantidad": 100.00
     * }
     *
     * Respuesta: inversión creada + nuevo saldo Aurus
     */
    @PostMapping
    public ResponseEntity<InversionResponse> invertir(
            @Valid @RequestBody InversionRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        InversionResponse response = inversionService.invertir(request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /** GET /api/inversiones/mias — Historial de inversiones del cliente autenticado */
    @GetMapping("/mias")
    public ResponseEntity<List<InversionResponse>> misInversiones(
            @AuthenticationPrincipal UserDetails userDetails) {
        Integer idCliente = clienteService
                .obtenerPorEmail(userDetails.getUsername()).getIdCliente();
        return ResponseEntity.ok(inversionService.obtenerHistorial(idCliente));
    }

    /** GET /api/inversiones/proyecto/{id} — Inversiones recibidas por un proyecto */
    @GetMapping("/proyecto/{id}")
    public ResponseEntity<List<InversionResponse>> inversionesPorProyecto(@PathVariable Integer id) {
        return ResponseEntity.ok(inversionService.obtenerPorProyecto(id));
    }
}