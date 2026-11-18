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
    /** Name of the product when it was added to the cart. */
    private String name;
    /** Brand of the product. */
    private String brand;
    /** Price of one unit when it was added to the cart. */
    private double price;
    /** Units of the product in the cart. */
    private int units;

    /**
     * Empty Cart Item constructor
     */
    public CartItem() {
    }

    /**
     * Creates a cart line with all its data.
     *
     * @param productId _id of the product
     * @param name name of the product
     * @param brand brand of the product
     * @param price price of one unit
     * @param units units of the product
     */
    public CartItem(ObjectId productId, String name, String brand, double price, int units) {
        this.productId = productId;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.units = units;
    }
    /**
     * Creates a cart line from a product of the shop.
     *
     * @param product product added to the cart
     * @param units   units of the product
     */
    public CartItem(Product product,int units) {
        this.productId = product.getId();
        this.name = product.getName();
        this.brand = product.getBrand();
        this.price = product.getPrice();
        this.units = units;
    }

    /**
     * Returns mongoDb objectId of the product associated with this CartProduct.
     *
     * @return mongoDb objectId of the product associated with this CartProduct
     */
    public ObjectId getProductId() {
        return productId;
    }

    /**
     * sets the mongoDB objectid of the product associated with this CartProduct
     *
     * @param productId _id of the product
     */
    public void setProductId(ObjectId productId) {
        this.productId = productId;
    }

    /**
     * Returns the name of the CartProduct.
     *
     * @return The name of the CartProduct
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the CartProduct
     *
     * @param name name of the product
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the brand of the CartProduct.
     *
     * @return the brand of the CartProduct
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Set the brand of the CartProduct
     *
     * @param brand brand of the product
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * Returns the price of the CartProduct.
     *
     * @return the price of the CartProduct
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the CartProduct
     *
     * @param price price of one unit
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Returns how many units of the product the customer chose.
     *
     * @return how many units of the product the customer chose
     */
    public int getUnits() {
        return units;
    }

    /**
     * Sets how many units of the product the customer wants
     *
     * @param units units of the product
     */
    public void setUnits(int units) {
        this.units = units;
    }

    /**
     * Default toString for the CartProduct
     *
     * @return the text with the fields of the cart line
     */
    @Override
    public String toString() {
        return "CartItem{" + "productId=" + productId + ", name=" + name + ", brand=" + brand
                + ", price=" + price + ", units=" + units + '}';
    }
}
