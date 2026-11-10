package com.reto1.dao;

import java.util.ArrayList;

import org.bson.types.ObjectId;

import com.reto1.exceptions.DatabaseException;
import com.reto1.model.Shipment;
import com.reto1.model.ShipmentStatus;

/**
 * Data access operations for the "shipments" collection.
 * <p>
 * If MongoDB fails, every method throws a {@link DatabaseException} that the
 * window catches to show the message.
 */
public interface ShipmentDao {

    /**
     * Saves a new shipment. MongoDB generates the _id and it is set in the
     * object.
     *
     * @param shipment shipment to save
     * @throws DatabaseException if MongoDB fails or the data is not valid
     */
    void insert(Shipment shipment) throws DatabaseException;

    /**
     * Finds a shipment by its _id.
     *
     * @param id _id of the shipment
     * @return the shipment, or {@code null} if it does not exist
     * @throws DatabaseException if MongoDB fails
     */
    Shipment findById(ObjectId id) throws DatabaseException;

    /**
     * Returns every shipment, newest first.
     *
     * @return list of shipments (empty if there are none)
     * @throws DatabaseException if MongoDB fails
     */
    ArrayList<Shipment> findAll() throws DatabaseException;

    /**
     * Returns the shipments of one customer, newest first ("my shipments").
     *
     * @param customerId _id of the customer in the "users" collection
     * @return list of shipments of the customer (empty if none)
     * @throws DatabaseException if MongoDB fails
     */
    ArrayList<Shipment> findByCustomer(ObjectId customerId) throws DatabaseException;

    /**
     * Changes only the status of a shipment.
     *
     * @param id     _id of the shipment
     * @param status new status
     * @return {@code true} if a shipment with that _id was found
     * @throws DatabaseException if MongoDB fails
     */
    boolean updateStatus(ObjectId id, ShipmentStatus status) throws DatabaseException;

    /**
     * Checks if a customer has shipments that are not finished yet
     * (IN_PROGRESS or IN_SHIPMENT). Used before deleting a user.
     *
     * @param customerId _id of the customer
     * @return {@code true} if the customer has unfinished shipments
     * @throws DatabaseException if MongoDB fails
     */
    boolean existsInProgressByCustomer(ObjectId customerId) throws DatabaseException;
}
