package com.novacapital.controller;

import com.novacapital.dto.PuntuacionRequest;
import com.novacapital.dto.PuntuacionResponse;
import com.novacapital.service.PuntuacionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/puntuaciones")
public class PuntuacionController {

    private final PuntuacionService puntuacionService;

    public PuntuacionController(PuntuacionService puntuacionService) {
        this.puntuacionService = puntuacionService;
    }

    @PostMapping
    public ResponseEntity<PuntuacionResponse> valorar(
            @Valid @RequestBody PuntuacionRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        PuntuacionResponse p = puntuacionService.valorar(request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(p);
    }

    @GetMapping("/proyecto/{id}")
    public ResponseEntity<List<PuntuacionResponse>> valoracionesPorProyecto(@PathVariable Integer id) {
        return ResponseEntity.ok(puntuacionService.obtenerPorProyecto(id));
    }

    @GetMapping("/media/{id}")
    public ResponseEntity<Map<String, Double>> media(@PathVariable Integer id) {
        Double media = puntuacionService.obtenerMedia(id);
        return ResponseEntity.ok(Map.of("media", media));
    }
}