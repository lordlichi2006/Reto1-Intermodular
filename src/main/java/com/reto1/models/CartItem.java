package com.reto1.models;

import org.bson.types.ObjectId;

/**
 * Line of a customer's cart (embedded inside Customer.cart).
 * Name, brand and price are copied so the cart can be shown without
 * querying "products"; the stock is NOT copied because it changes all the time.
 *
 * @author 20ala
 */
public class CartItem {

    /** _id of the product in the "products" collection. */
    private ObjectId productId;
    private String name;
    private String brand;
    private double price;
    private int units;

    public CartItem() {
    }

    public CartItem(ObjectId productId, String name, String brand, double price, int units) {
        this.productId = productId;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.units = units;
    }

    public ObjectId getProductId() {
        return productId;
    }

    public void setProductId(ObjectId productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    @Override
    public String toString() {
        return "CartItem{" + "productId=" + productId + ", name=" + name + ", brand=" + brand
                + ", price=" + price + ", units=" + units + '}';
    }
}
