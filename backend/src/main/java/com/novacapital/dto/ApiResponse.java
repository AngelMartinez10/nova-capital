package com.novacapital.dto;

/**
 * Respuesta genérica para operaciones que no devuelven datos concretos.
 * Ejemplo: { "mensaje": "Operación realizada con éxito", "exito": true }
 */
public class ApiResponse {

    private boolean exito;
    private String mensaje;
    private Object datos;

    public ApiResponse(boolean exito, String mensaje) {
        this.exito   = exito;
        this.mensaje = mensaje;
    }

    public ApiResponse(boolean exito, String mensaje, Object datos) {
        this.exito   = exito;
        this.mensaje = mensaje;
        this.datos   = datos;
    }

    public boolean isExito() { return exito; }
    public String getMensaje() { return mensaje; }
    public Object getDatos() { return datos; }
}