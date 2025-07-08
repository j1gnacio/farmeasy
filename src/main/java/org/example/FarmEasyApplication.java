package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal que inicia la aplicación FarmEasy.
 * <p>
 * Esta clase utiliza la anotación {@code @SpringBootApplication} que engloba
 * {@code @Configuration}, {@code @EnableAutoConfiguration}, y {@code @ComponentScan}.
 * Sirve como el punto de entrada para la ejecución de la aplicación Spring Boot.
 * </p>
 */
@SpringBootApplication
public class FarmEasyApplication {

    /**
     * El método principal que sirve como punto de entrada para la aplicación.
     *
     * @param args los argumentos de línea de comandos pasados al iniciar la aplicación.
     */
    public static void main(String[] args) {
        SpringApplication.run(FarmEasyApplication.class, args);
    }
}