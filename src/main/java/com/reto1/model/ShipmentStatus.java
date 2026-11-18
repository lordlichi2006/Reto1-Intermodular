package com.reto1.model;

/**
 * Enum for the status of shipments
 * @author Ekaitz
 */
public enum ShipmentStatus {
    /** The order has been placed and is being prepared. */
    IN_PROGRESS,
    /** The order has left the warehouse with the shipping company. */
    IN_SHIPMENT,
    /** The order has reached the customer. */
    ARRIVED,
    /** The order was cancelled and will not be delivered. */
    CANCELLED
}
