package com.reto1.dao;

import java.util.List;

import org.bson.types.ObjectId;

import com.reto1.model.Shipment;
import com.reto1.model.ShipmentStatus;

/**
 * Data access operations for the "shipments" collection.
 * <p>
 * Driver errors ({@code MongoException}, {@code MongoWriteException}) are not
 * caught here: they are propagated so the controller can show them.
 */
public interface ShipmentDao {

    /**
     * Saves a new shipment. The driver generates the _id and sets it in the
     * object.
     *
     * @param shipment shipment to save
     * @throws com.mongodb.MongoWriteException if the schema validation fails
     */
    void insert(Shipment shipment);

    /**
     * Finds a shipment by its _id.
     *
     * @param id _id of the shipment
     * @return the shipment, or {@code null} if it does not exist
     */
    Shipment findById(ObjectId id);

    /**
     * Returns every shipment, newest first.
     *
     * @return list of shipments (empty if there are none)
     */
    List<Shipment> findAll();

    /**
     * Returns the shipments of one customer, newest first ("my shipments").
     *
     * @param customerId _id of the customer in the "users" collection
     * @return list of shipments of the customer (empty if none)
     */
    List<Shipment> findByCustomer(ObjectId customerId);

    /**
     * Changes only the status of a shipment.
     *
     * @param id     _id of the shipment
     * @param status new status
     * @return {@code true} if a shipment with that _id was found
     */
    boolean updateStatus(ObjectId id, ShipmentStatus status);
}
