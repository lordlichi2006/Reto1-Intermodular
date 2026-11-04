package com.reto1.controller;

import java.util.List;

import org.bson.types.ObjectId;

import com.reto1.dao.SupplierDao;
import com.reto1.dao.SupplierImplementacionBD;
import com.reto1.model.Supplier;

/**
 * Business logic of the suppliers.
 * <p>
 * The windows (package view) call this class; this class calls the DAO
 * through the interface {@link SupplierDao}.
 */
public class SupplierController {

    private final SupplierDao dao;

    /**
     * Creates the controller with the MongoDB implementation of the DAO.
     */
    public SupplierController() {
        this.dao = new SupplierImplementacionBD();
    }

    /**
     * @return every supplier, sorted by name
     */
    public List<Supplier> findAll() {
        return dao.findAll();
    }

    /**
     * @param id _id of the supplier
     * @return the supplier, or {@code null} if it does not exist
     */
    public Supplier findById(ObjectId id) {
        return dao.findById(id);
    }

    /**
     * @param cif CIF of the supplier
     * @return the supplier, or {@code null} if it does not exist
     */
    public Supplier findByCif(String cif) {
        return dao.findByCif(cif);
    }

    /**
     * @param supplier new supplier
     */
    public void insert(Supplier supplier) {
        dao.insert(supplier);
    }

    /**
     * @param supplier supplier with the new data (same _id)
     * @return {@code true} if the supplier was found and updated
     */
    public boolean update(Supplier supplier) {
        return dao.update(supplier);
    }

    /**
     * @param id _id of the supplier
     * @return {@code true} if the supplier was deleted
     */
    public boolean delete(ObjectId id) {
        return dao.delete(id);
    }
}
