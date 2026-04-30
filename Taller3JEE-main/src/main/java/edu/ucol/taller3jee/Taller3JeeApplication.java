package edu.ucol.taller3jee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Aplicación principal de Taller3JEE
 * Sistema de gestión de curso de Estructuras de Datos
 */
@SpringBootApplication
@EnableScheduling
public class Taller3JeeApplication {

    public static void main(String[] args) {
        SpringApplication.run(Taller3JeeApplication.class, args);
    }
}
