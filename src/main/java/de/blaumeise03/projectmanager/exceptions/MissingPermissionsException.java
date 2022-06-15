package de.blaumeise03.projectmanager.exceptions;

public class MissingPermissionsException extends Exception {

    public MissingPermissionsException() {
        super("Missing permissions!");
    }

    public MissingPermissionsException(String message) {
        super(message);
    }
}
