package com.reto1.dao;

import java.util.ArrayList;

import org.bson.types.ObjectId;

import com.reto1.exceptions.DatabaseException;
import com.reto1.model.Product;

/**
 * Data access operations for the "products" collection.
 * <p>
 * If MongoDB fails, every method throws a {@link DatabaseException} that the
 * window catches to show the message.
 */
public interface ProductDao {

    /**
     * Returns every product, sorted by name.
     *
     * @return list of products (empty if there are none)
     * @throws DatabaseException if MongoDB fails
     */
    ArrayList<Product> findAll() throws DatabaseException;

    /**
     * Finds a product by its _id.
     *
     * @param id _id of the product
     * @return the product, or {@code null} if it does not exist
     * @throws DatabaseException if MongoDB fails
     */
    Product findById(ObjectId id) throws DatabaseException;

    /**
     * Finds the products whose name contains the given text, ignoring
     * upper/lower case. For example "osi" finds "Ositos de oro".
     *
     * @param text part of the name to search
     * @return list of matching products, sorted by name (empty if none)
     * @throws DatabaseException if MongoDB fails
     */
    ArrayList<Product> findByName(String text) throws DatabaseException;

    /**
     * Saves a new product. MongoDB generates the _id and it is set in the
     * object.
     *
     * @param product product to save
     * @throws DatabaseException if MongoDB fails or the data is not valid
     */
    void insert(Product product) throws DatabaseException;

    /**
     * Replaces the stored product with the given one (same _id).
     *
     * @param product product with the new data
     * @return {@code true} if a product with that _id was found
     * @throws DatabaseException if MongoDB fails or the new data is not valid
     */
    boolean update(Product product) throws DatabaseException;

    /**
     * Changes only the stock of a product.
     *
     * @param id    _id of the product
     * @param stock new stock (0 or more)
     * @return {@code true} if a product with that _id was found
     * @throws DatabaseException if MongoDB fails or the stock is negative
     */
    boolean updateStock(ObjectId id, int stock) throws DatabaseException;

    /**
     * Subtracts units from the stock, but only if there is enough stock.
     * Used when a customer purchases the cart.
     *
     * @param id    _id of the product
     * @param units units bought (1 or more)
     * @return {@code true} if the stock was decreased, {@code false} if there
     *         was not enough stock or the product does not exist
     * @throws DatabaseException if MongoDB fails
     */
    boolean decreaseStock(ObjectId id, int units) throws DatabaseException;

    /**
     * Deletes a product.
     *
     * @param id _id of the product
     * @return {@code true} if a product was deleted
     * @throws DatabaseException if MongoDB fails
     */
    boolean delete(ObjectId id) throws DatabaseException;
}
