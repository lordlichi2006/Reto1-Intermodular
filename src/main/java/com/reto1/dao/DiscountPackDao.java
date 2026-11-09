package com.reto1.dao;

import java.util.List;

import org.bson.types.ObjectId;

import com.reto1.model.DiscountPack;

/**
 * Data access operations for the "discountPacks" collection (offers).
 * <p>
 * Driver errors ({@code MongoException}, {@code MongoWriteException}) are not
 * caught here: they are propagated so the controller can show them.
 */
public interface DiscountPackDao {

    /**
     * Returns every discount pack, sorted by name.
     *
     * @return list of packs (empty if there are none)
     */
    List<DiscountPack> findAll();

    /**
     * Finds a discount pack by its _id.
     *
     * @param id _id of the pack
     * @return the pack, or {@code null} if it does not exist
     */
    DiscountPack findById(ObjectId id);

    /**
     * Saves a new discount pack. The driver generates the _id and sets it in
     * the object.
     *
     * @param pack pack to save
     * @throws com.mongodb.MongoWriteException if the schema validation fails
     */
    void insert(DiscountPack pack);

    /**
     * Replaces the stored pack with the given one (same _id).
     *
     * @param pack pack with the new data
     * @return {@code true} if a pack with that _id was found
     * @throws com.mongodb.MongoWriteException if the new data is not valid
     */
    boolean update(DiscountPack pack);

    /**
     * Deletes a discount pack.
     *
     * @param id _id of the pack
     * @return {@code true} if a pack was deleted
     */
    boolean delete(ObjectId id);

    /**
     * Checks if a product is included in any pack. A product that is in a
     * pack cannot be deleted.
     *
     * @param productId _id of the product
     * @return {@code true} if at least one pack contains the product
     */
    boolean existsByProduct(ObjectId productId);
}
