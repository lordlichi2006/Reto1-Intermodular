package com.reto1.dao;

import java.util.ArrayList;

import org.bson.types.ObjectId;

import com.reto1.exceptions.DatabaseException;
import com.reto1.model.DiscountPack;

/**
 * Data access operations for the "discountPacks" collection (offers).
 * <p>
 * If MongoDB fails, every method throws a {@link DatabaseException} that the
 * window catches to show the message.
 */
public interface DiscountPackDao {

    /**
     * Returns every discount pack, sorted by name.
     *
     * @return list of packs (empty if there are none)
     * @throws DatabaseException if MongoDB fails
     */
    ArrayList<DiscountPack> findAll() throws DatabaseException;

    /**
     * Finds a discount pack by its _id.
     *
     * @param id _id of the pack
     * @return the pack, or {@code null} if it does not exist
     * @throws DatabaseException if MongoDB fails
     */
    DiscountPack findById(ObjectId id) throws DatabaseException;

    /**
     * Saves a new discount pack. MongoDB generates the _id and it is set in
     * the object.
     *
     * @param pack pack to save
     * @throws DatabaseException if MongoDB fails or the data is not valid
     */
    void insert(DiscountPack pack) throws DatabaseException;

    /**
     * Replaces the stored pack with the given one (same _id).
     *
     * @param pack pack with the new data
     * @return {@code true} if a pack with that _id was found
     * @throws DatabaseException if MongoDB fails or the new data is not valid
     */
    boolean update(DiscountPack pack) throws DatabaseException;

    /**
     * Deletes a discount pack.
     *
     * @param id _id of the pack
     * @return {@code true} if a pack was deleted
     * @throws DatabaseException if MongoDB fails
     */
    boolean delete(ObjectId id) throws DatabaseException;

    /**
     * Checks if a product is included in any pack. A product that is in a
     * pack cannot be deleted.
     *
     * @param productId _id of the product
     * @return {@code true} if at least one pack contains the product
     * @throws DatabaseException if MongoDB fails
     */
    boolean existsByProduct(ObjectId productId) throws DatabaseException;
}
