package com.reto1.model;

import org.bson.types.ObjectId;

/**
 * Line of a customer's cart (embedded inside Customer.cart). Name, brand and
 * price are copied so the cart can be shown without querying "products"; the
 * stock is NOT copied because it changes all the time.
 *
 * @author 20ala
 */
public class CartItem {

    /**
     * _id of the product in the "products" collection.
     */
    private ObjectId productId;
    private String name;
    private String brand;
    private double price;
    private int units;

    /**
     * Empty Cart Item constructor
     */
    public CartItem() {
    }

    /**
     * @author Alain Cart Item constructor
     * @param productId
     * @param name
     * @param brand
     * @param price
     * @param units
     */
    public CartItem(ObjectId productId, String name, String brand, double price, int units) {
        this.productId = productId;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.units = units;
    }

    /**
     * @return mongoDb objectId of the product associated with this CartProduct
     */
    public ObjectId getProductId() {
        return productId;
    }

    /**
     * sets the mongoDB objectid of the product associated with this CartProduct
     *
     * @param productId
     */
    public void setProductId(ObjectId productId) {
        this.productId = productId;
    }

    /**
     * @return The name of the CartProduct
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the CartProduct
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return the brand of the CartProduct
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Set the brand of the CartProduct
     *
     * @param brand
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     *
     * @return the price of the CartProduct
     */
    public double getPrice() {
        return price;
    }

    /**
     * Gets the price of the CartProduct
     *
     * @param price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     *
     * @return how many units of the product the customer chose
     */
    public int getUnits() {
        return units;
    }

    /**
     * Sets how many units of the product the customer wants
     *
     * @param units
     */
    public void setUnits(int units) {
        this.units = units;
    }

    /**
     * Default toString for the CartProduct
     * @return
     */
    @Override
    public String toString() {
        return "CartItem{" + "productId=" + productId + ", name=" + name + ", brand=" + brand
                + ", price=" + price + ", units=" + units + '}';
    }
}
