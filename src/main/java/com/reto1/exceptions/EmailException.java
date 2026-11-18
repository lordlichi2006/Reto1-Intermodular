package com.reto1.exceptions;

/**
 * The email typed in a form does not have a valid format (name@domain.com).
 * Thrown by the validations of the windows (profile, users).
 */
public class EmailException extends Exception {

    /** Version of the class for serialization. */
    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception with the message to show.
     *
     * @param message text that the window shows to the user
     */
    public EmailException(String message) {
        super(message);
    }
}
