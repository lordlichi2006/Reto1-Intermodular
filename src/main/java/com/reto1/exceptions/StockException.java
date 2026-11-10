package com.reto1.exceptions;

/**
 * There is not enough stock of a product (for example, when the customer
 * adds more units to the cart than there are, or when purchasing).
 */
public class StockException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * @param message text that the window shows to the user
     */
    public StockException(String message) {
        super(message);
    }
}
