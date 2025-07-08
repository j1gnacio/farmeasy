package org.example.exception;

/**
 * Excepción personalizada para manejar errores durante el proceso de registro de usuarios.
 */
public class RegistroException extends RuntimeException {
    /**
     * Constructor que acepta un mensaje de error.
     *
     * @param message El mensaje que describe el error.
     */
    public RegistroException(String message) {
        super(message);
    }
}