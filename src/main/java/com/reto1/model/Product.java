
package com.reto1.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

/**
 * Product of the shop (collection "products").
 * Embeds a summary of its suppliers ({@link ProductSupplier}).
 *
 * @author Ekaitz
 */
public class Product {

    private ObjectId id;
    private String name;
    private String brand;
    private double price;
    private int stock;
    private String description;
    private List<ProductSupplier> suppliers = new ArrayList<>();

    /**
     * Creates an empty product.
     */
    public Product() {
    }

    /**
     * Creates a product with the specified name, brand, price, stock,
     * description and list of suppliers.
     *
     * @param name        the product's name
     * @param brand       the product's brand
     * @param price       the product's price
     * @param stock       the available stock quantity
     * @param description the product's description
     * @param suppliers   the list of suppliers associated with the product
     */
    public Product(String name, String brand, double price, int stock,
                   String description, List<ProductSupplier> suppliers) {
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.suppliers = suppliers;
    }

    /**
     * Returns the MongoDB identifier of the product.
     *
     * @return the product's ObjectId, or {@code null} if it has not been assigned
     */
    public ObjectId getId() {
        return id;
    }

    /**
     * Sets the MongoDB identifier of the product.
     *
     * @param id the ObjectId to assign to the product
     */
    public void setId(ObjectId id) {
        this.id = id;
    }

    /**
     * Returns the product's name.
     *
     * @return the product's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the product's name.
     *
     * @param name the name to assign to the product
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the product's brand.
     *
     * @return the product's brand
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Sets the product's brand.
     *
     * @param brand the brand to assign to the product
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * Returns the product's price.
     *
     * @return the product's price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the product's price.
     *
     * @param price the price to assign to the product
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Returns the current stock quantity of the product.
     *
     * @return the available number of units
     */
    public int getStock() {
        return stock;
    }

    /**
     * Sets the current stock quantity of the product.
     *
     * @param stock the number of units to assign
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Returns the product's description.
     *
     * @return the product's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the product's description.
     *
     * @param description the description to assign to the product
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the list of suppliers associated with the product.
     *
     * @return the list of {@link ProductSupplier} objects
     */
    public List<ProductSupplier> getSuppliers() {
        return suppliers;
    }

    /**
     * Sets the list of suppliers associated with the product.
     *
     * @param suppliers the list of suppliers to assign to the product
     */
    public void setSuppliers(List<ProductSupplier> suppliers) {
        this.suppliers = suppliers;
    }

    /**
     * Returns a string representation of the product, including its
     * identifier, name, brand, price, stock, description and suppliers.
     *
     * @return a string containing the product information
     */
    @Override
    public String toString() {
        return "Product{" + "id=" + id + ", name=" + name + ", brand=" + brand + ", price=" + price
                + ", stock=" + stock + ", description=" + description + ", suppliers=" + suppliers + '}';
    }
}
