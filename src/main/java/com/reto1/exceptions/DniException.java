package com.reto1.exceptions;

/**
 * The DNI / NIE / CIF typed in a form is not valid (wrong format or letter).
 * Thrown by the validations of the windows (register, profile, users).
 */
public class DniException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * @param message text that the window shows to the user
     */
    public DniException(String message) {
        super(message);
    }
}
