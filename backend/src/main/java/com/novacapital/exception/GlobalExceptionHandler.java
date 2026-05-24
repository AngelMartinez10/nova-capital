package com.novacapital.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones.
 *
 * Captura las excepciones lanzadas en cualquier Controller y las convierte
 * en respuestas JSON coherentes con el código HTTP correcto.
 *
 * Sin esto, Spring devolvería páginas HTML de error o respuestas inconsistentes.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // --- 400 Bad Request: datos inválidos ---
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(400, ex.getMessage()));
    }

    // --- 400: credenciales incorrectas en el login ---
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(400, "Email o contraseña incorrectos"));
    }

    // --- 400: fallos de validación en @Valid (campos del DTO) ---
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String campo = ((FieldError) error).getField();
            String mensaje = error.getDefaultMessage();
            errores.put(campo, mensaje);
        });

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("codigo", 400);
        respuesta.put("mensaje", "Error de validación");
        respuesta.put("errores", errores);
        respuesta.put("timestamp", LocalDateTime.now());

        return ResponseEntity.badRequest().body(respuesta);
    }

    // --- 404 Not Found ---
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(404, ex.getMessage()));
    }

    // --- 500 Internal Server Error: cualquier otra excepción no controlada ---
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(500, "Error interno del servidor: " + ex.getMessage()));
    }

    // DTO interno para la respuesta de error
    public static class ErrorResponse {
        private int codigo;
        private String mensaje;
        private LocalDateTime timestamp;

        public ErrorResponse(int codigo, String mensaje) {
            this.codigo    = codigo;
            this.mensaje   = mensaje;
            this.timestamp = LocalDateTime.now();
        }

        public int getCodigo() { return codigo; }
        public String getMensaje() { return mensaje; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }
}