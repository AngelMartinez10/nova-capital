package com.novacapital.controller;

import com.novacapital.dto.AuthResponse;
import com.novacapital.dto.LoginRequest;
import com.novacapital.dto.RegistroRequest;
import com.novacapital.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller de autenticación.
 *
 * Endpoints PÚBLICOS (no requieren JWT):
 *   POST /api/auth/registro  → Crear cuenta nueva
 *   POST /api/auth/login     → Obtener token JWT
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * POST /api/auth/registro
     *
     * Body JSON:
     * {
     *   "nombre": "Carlos",
     *   "apellidos": "García López",
     *   "email": "carlos@email.com",
     *   "contrasena": "password123",
     *   "telefono": "612345678"
     * }
     *
     * Respuesta 201: { "token": "...", "idCliente": 1, "nombre": "Carlos", "saldoAurus": 1000 }
     */
    @PostMapping("/registro")
    public ResponseEntity<AuthResponse> registro(@Valid @RequestBody RegistroRequest request) {
        AuthResponse response = authService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * POST /api/auth/login
     *
     * Body JSON:
     * {
     *   "email": "carlos@email.com",
     *   "contrasena": "password123"
     * }
     *
     * Respuesta 200: { "token": "eyJhbGciOiJIUzI1NiJ9...", "idCliente": 1, ... }
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}