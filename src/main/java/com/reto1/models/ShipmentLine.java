package com.reto1.models;

import org.bson.types.ObjectId;

/**
 * Line of a shipment (embedded inside Shipment.lines).
 * The price is copied at purchase time, so later price changes
 * do not modify old orders.
 *
 * @author 20ala
 */
public class ShipmentLine {

    private ObjectId productId;
    private String name;
    private double price;
    private int units;

    public ShipmentLine() {
    }

    public ShipmentLine(ObjectId productId, String name, double price, int units) {
        this.productId = productId;
        this.name = name;
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
        return "ShipmentLine{" + "productId=" + productId + ", name=" + name + ", price=" + price + ", units=" + units + '}';
    }
}
