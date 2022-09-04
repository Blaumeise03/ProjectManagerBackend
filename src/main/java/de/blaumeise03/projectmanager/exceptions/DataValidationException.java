package de.blaumeise03.projectmanager.exceptions;

public class DataValidationException extends IllegalArgumentException {
    public DataValidationException() {
        super("The data has an invalid format");
    }

    public DataValidationException(String message) {
        super(message);
    }

    public DataValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataValidationException(Throwable cause) {
        super("The data has an invalid format", cause);
    }
}
