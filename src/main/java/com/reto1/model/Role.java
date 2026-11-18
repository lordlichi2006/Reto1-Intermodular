package com.reto1.model;

/**
 * What roles a worker is
 * @author Ekaitz.Rivero
 */
public enum Role {
    /** Worker: manages products, offers and shipments. */
    WORKER,
    /** Administrator: can also manage the users and see the log. */
    ADMIN;
}
