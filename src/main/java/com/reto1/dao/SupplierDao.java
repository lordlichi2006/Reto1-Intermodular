package com.reto1.dao;

import java.util.ArrayList;

import org.bson.types.ObjectId;

import com.reto1.exceptions.DatabaseException;
import com.reto1.model.Supplier;

/**
 * Data access operations for the "suppliers" collection.
 * <p>
 * If MongoDB fails, every method throws a {@link DatabaseException} that the
 * window catches to show the message.
 */
public interface SupplierDao {

    /**
     * Returns every supplier, sorted by name.
     *
     * @return list of suppliers (empty if there are none)
     * @throws DatabaseException if MongoDB fails
     */
    ArrayList<Supplier> findAll() throws DatabaseException;

    /**
     * Finds a supplier by its _id.
     *
     * @param id _id of the supplier
     * @return the supplier, or {@code null} if it does not exist
     * @throws DatabaseException if MongoDB fails
     */
    Supplier findById(ObjectId id) throws DatabaseException;

    /**
     * Finds a supplier by its CIF (the CIF is unique).
     *
     * @param cif CIF of the supplier, for example "B12345678"
     * @return the supplier, or {@code null} if it does not exist
     * @throws DatabaseException if MongoDB fails
     */
    Supplier findByCif(String cif) throws DatabaseException;

    /**
     * Saves a new supplier. MongoDB generates the _id and it is set in the
     * object.
     *
     * @param supplier supplier to save
     * @throws DatabaseException if MongoDB fails, the data is not valid or the
     *                           CIF is already used
     */
    void insert(Supplier supplier) throws DatabaseException;

    /**
     * Replaces the stored supplier with the given one (same _id).
     *
     * @param supplier supplier with the new data
     * @return {@code true} if a supplier with that _id was found
     * @throws DatabaseException if MongoDB fails or the new data is not valid
     */
    boolean update(Supplier supplier) throws DatabaseException;

    /**
     * Deletes a supplier.
     *
     * @param id _id of the supplier
     * @return {@code true} if a supplier was deleted
     * @throws DatabaseException if MongoDB fails
     */
    boolean delete(ObjectId id) throws DatabaseException;
}
