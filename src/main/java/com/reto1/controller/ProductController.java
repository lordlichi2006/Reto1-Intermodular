package com.reto1.controller;

import java.util.List;

import org.bson.types.ObjectId;

import com.reto1.dao.ProductDao;
import com.reto1.dao.ProductImplementacionBD;
import com.reto1.model.Product;

/**
 * Business logic of the products.
 * <p>
 * The windows (package view) call this class; this class calls the DAO
 * through the interface {@link ProductDao}.
 */
public class ProductController {

    private final ProductDao dao;

    /**
     * Creates the controller with the MongoDB implementation of the DAO.
     */
    public ProductController() {
        this.dao = new ProductImplementacionBD();
    }

    /**
     * @return every product, sorted by name
     */
    public List<Product> findAll() {
        return dao.findAll();
    }

    /**
     * @param id _id of the product
     * @return the product, or {@code null} if it does not exist
     */
    public Product findById(ObjectId id) {
        return dao.findById(id);
    }

    /**
     * Search of the shop: products whose name contains the text.
     *
     * @param text part of the name, without caring about upper/lower case
     * @return matching products
     */
    public List<Product> findByName(String text) {
        return dao.findByName(text);
    }

    /**
     * @param product new product
     */
    public void insert(Product product) {
        dao.insert(product);
    }

    /**
     * @param product product with the new data (same _id)
     * @return {@code true} if the product was found and updated
     */
    public boolean update(Product product) {
        return dao.update(product);
    }

    /**
     * @param id    _id of the product
     * @param stock new stock
     * @return {@code true} if the product was found and updated
     */
    public boolean updateStock(ObjectId id, int stock) {
        return dao.updateStock(id, stock);
    }

    /**
     * @param id _id of the product
     * @return {@code true} if the product was deleted
     */
    public boolean delete(ObjectId id) {
        return dao.delete(id);
    }
}
