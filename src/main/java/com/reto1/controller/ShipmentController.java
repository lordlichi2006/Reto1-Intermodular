package com.reto1.controller;

import java.util.List;

import org.bson.types.ObjectId;

import com.reto1.dao.ShipmentDao;
import com.reto1.dao.ShipmentImplementacionBD;
import com.reto1.model.Shipment;
import com.reto1.model.ShipmentStatus;

/**
 * Business logic of the shipments (orders).
 * <p>
 * The windows (package view) call this class; this class calls the DAO
 * through the interface {@link ShipmentDao}.
 */
public class ShipmentController {

    private final ShipmentDao dao;

    /**
     * Creates the controller with the MongoDB implementation of the DAO.
     */
    public ShipmentController() {
        this.dao = new ShipmentImplementacionBD();
    }

    /**
     * @param shipment new shipment
     */
    public void insert(Shipment shipment) {
        dao.insert(shipment);
    }

    /**
     * @param id _id of the shipment
     * @return the shipment, or {@code null} if it does not exist
     */
    public Shipment findById(ObjectId id) {
        return dao.findById(id);
    }

    /**
     * @return every shipment, newest first
     */
    public List<Shipment> findAll() {
        return dao.findAll();
    }

    /**
     * @param customerId _id of the customer
     * @return the shipments of that customer, newest first
     */
    public List<Shipment> findByCustomer(ObjectId customerId) {
        return dao.findByCustomer(customerId);
    }

    /**
     * @param id     _id of the shipment
     * @param status new status
     * @return {@code true} if the shipment was found and updated
     */
    public boolean updateStatus(ObjectId id, ShipmentStatus status) {
        return dao.updateStatus(id, status);
    }
}
