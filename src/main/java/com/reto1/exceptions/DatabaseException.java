package com.reto1.exceptions;

/**
 * Error while working with MongoDB: the server is not running, the data does
 * not pass the $jsonSchema validation, the email is already used...
 * <p>
 * The DAO (ImplementacionBD) throws it and the window catches it and shows
 * the message to the user.
 */
public class DatabaseException extends Exception {

    /** Version of the class for serialization. */
    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception with the message to show.
     *
     * @param message text that the window shows to the user
     */
    public DatabaseException(String message) {
        super(message);
    }
}
