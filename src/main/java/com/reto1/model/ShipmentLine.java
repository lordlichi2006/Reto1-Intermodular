
package com.reto1.model;

import org.bson.types.ObjectId;

/**
 * Line of a shipment (embedded inside Shipment.lines).
 * The price is copied at purchase time, so later price changes
 * do not modify old orders.
 *
 * @author Alain
 */
public class ShipmentLine {

    private ObjectId productId;
    private String name;
    private double price;
    private int units;

    /**
     * Creates an empty shipment line.
     */
    public ShipmentLine() {
    }

    /**
     * Creates a shipment line with the specified product information,
     * price and quantity.
     *
     * @param productId the identifier of the product
     * @param name      the name of the product
     * @param price     the product price at the time of purchase
     * @param units     the number of units purchased
     */
    public ShipmentLine(ObjectId productId, String name, double price, int units) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.units = units;
    }

    /**
     * Returns the identifier of the product associated with this shipment line.
     *
     * @return the product's ObjectId
     */
    public ObjectId getProductId() {
        return productId;
    }

    /**
     * Sets the identifier of the product associated with this shipment line.
     *
     * @param productId the product's ObjectId
     */
    public void setProductId(ObjectId productId) {
        this.productId = productId;
    }

    /**
     * Returns the name of the product.
     *
     * @return the product's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the product.
     *
     * @param name the product's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the product price recorded at the time of purchase.
     *
     * @return the product price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the product price for this shipment line.
     *
     * @param price the product price to assign
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Returns the number of units included in this shipment line.
     *
     * @return the quantity of units
     */
    public int getUnits() {
        return units;
    }

    /**
     * Sets the number of units included in this shipment line.
     *
     * @param units the quantity of units to assign
     */
    public void setUnits(int units) {
        this.units = units;
    }

    /**
     * Returns a string representation of the shipment line, including
     * the product identifier, name, price and quantity.
     *
     * @return a string containing the shipment line information
     */
    @Override
    public String toString() {
        return "ShipmentLine{" + "productId=" + productId + ", name=" + name + ", price=" + price + ", units=" + units + '}';
    }
}
