package com.novacapital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Punto de entrada de la aplicación Nova Capital Backend.
 * Ejecuta esta clase desde IntelliJ para arrancar el servidor en puerto 8080.
 */
@EnableScheduling
@SpringBootApplication
public class NovaCapitalApplication {

    public static void main(String[] args) {
        SpringApplication.run(NovaCapitalApplication.class, args);
    }
}