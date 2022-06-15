package de.blaumeise03.projectmanager.exceptions;

public class POJOMappingException extends Exception{
    public POJOMappingException() {
        super("An unexpected error occurred during the POJO-mapping");
    }

    public POJOMappingException(String message) {
        super(message);
    }

    public POJOMappingException(String message, Throwable cause) {
        super(message, cause);
    }
}
