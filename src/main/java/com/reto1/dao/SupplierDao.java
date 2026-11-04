package com.reto1.dao;

import java.util.List;

import org.bson.types.ObjectId;

import com.reto1.model.Supplier;

/**
 * Data access operations for the "suppliers" collection.
 * <p>
 * Driver errors ({@code MongoException}, {@code MongoWriteException}) are not
 * caught here: they are propagated so the controller can show them.
 */
public interface SupplierDao {

    /**
     * Returns every supplier, sorted by name.
     *
     * @return list of suppliers (empty if there are none)
     */
    List<Supplier> findAll();

    /**
     * Finds a supplier by its _id.
     *
     * @param id _id of the supplier
     * @return the supplier, or {@code null} if it does not exist
     */
    Supplier findById(ObjectId id);

    /**
     * Finds a supplier by its CIF (the CIF is unique).
     *
     * @param cif CIF of the supplier, for example "B12345678"
     * @return the supplier, or {@code null} if it does not exist
     */
    Supplier findByCif(String cif);

    /**
     * Saves a new supplier. The driver generates the _id and sets it in the
     * object.
     *
     * @param supplier supplier to save
     * @throws com.mongodb.MongoWriteException if the schema validation fails or
     *                                         the CIF is duplicated
     */
    void insert(Supplier supplier);

    /**
     * Replaces the stored supplier with the given one (same _id).
     *
     * @param supplier supplier with the new data
     * @return {@code true} if a supplier with that _id was found
     * @throws com.mongodb.MongoWriteException if the new data is not valid
     */
    boolean update(Supplier supplier);

    /**
     * Deletes a supplier.
     *
     * @param id _id of the supplier
     * @return {@code true} if a supplier was deleted
     */
    boolean delete(ObjectId id);
}
