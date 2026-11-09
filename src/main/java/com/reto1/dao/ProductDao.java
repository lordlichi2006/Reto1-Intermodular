package com.reto1.dao;

import java.util.List;

import org.bson.types.ObjectId;

import com.reto1.model.Product;

/**
 * Data access operations for the "products" collection.
 * <p>
 * Driver errors ({@code MongoException}, {@code MongoWriteException}) are not
 * caught here: they are propagated so the controller can show them.
 */
public interface ProductDao {

    /**
     * Returns every product, sorted by name.
     *
     * @return list of products (empty if there are none)
     */
    List<Product> findAll();

    /**
     * Finds a product by its _id.
     *
     * @param id _id of the product
     * @return the product, or {@code null} if it does not exist
     */
    Product findById(ObjectId id);

    /**
     * Finds the products whose name contains the given text, ignoring
     * upper/lower case. For example "osi" finds "Ositos de oro".
     *
     * @param text part of the name to search
     * @return list of matching products, sorted by name (empty if none)
     */
    List<Product> findByName(String text);

    /**
     * Saves a new product. The driver generates the _id and sets it in the
     * object.
     *
     * @param product product to save
     * @throws com.mongodb.MongoWriteException if the schema validation fails
     */
    void insert(Product product);

    /**
     * Replaces the stored product with the given one (same _id).
     *
     * @param product product with the new data
     * @return {@code true} if a product with that _id was found
     * @throws com.mongodb.MongoWriteException if the new data is not valid
     */
    boolean update(Product product);

    /**
     * Changes only the stock of a product.
     *
     * @param id    _id of the product
     * @param stock new stock (0 or more)
     * @return {@code true} if a product with that _id was found
     * @throws com.mongodb.MongoWriteException if the stock is negative
     */
    boolean updateStock(ObjectId id, int stock);

    /**
     * Subtracts units from the stock, but only if there is enough stock.
     * Used when a customer purchases the cart.
     *
     * @param id    _id of the product
     * @param units units bought (1 or more)
     * @return {@code true} if the stock was decreased, {@code false} if there
     *         was not enough stock or the product does not exist
     */
    boolean decreaseStock(ObjectId id, int units);

    /**
     * Deletes a product.
     *
     * @param id _id of the product
     * @return {@code true} if a product was deleted
     */
    boolean delete(ObjectId id);
}
