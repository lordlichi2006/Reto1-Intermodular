package com.reto1.exceptions;

/**
 * The DNI / NIE / CIF typed in a form is not valid (wrong format or letter).
 * Thrown by the validations of the windows (profile, users).
 */
public class DniException extends Exception {

    /** Version of the class for serialization. */
    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception with the message to show.
     *
     * @param message text that the window shows to the user
     */
    public DniException(String message) {
        super(message);
    }
}
