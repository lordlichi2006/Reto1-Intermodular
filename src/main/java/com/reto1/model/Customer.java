package com.reto1.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

/**
 * Shop customer (stored in "users" with userType = "customer").
 * <ul>
 * <li>Embedded: addresses and cart (always read together with the
 * customer).</li>
 * <li>Referenced: shipmentIds, an array of _id of the "shipments"
 * collection.</li>
 * </ul>
 *
 * @author Ekaitz.Rivero
 */
public class Customer extends User {

    private List<String> addresses = new ArrayList<>();
    private double totalSpent;
    private CustomerType type;
    private List<CartItem> cart = new ArrayList<>();
    private List<ObjectId> shipmentIds = new ArrayList<>();

    /**
     * Default customer constructor
     */
    public Customer() {
        super();
    }

    /**
     * Constructor of the customer object
     *
     * @param name
     * @param email
     * @param password
     * @param idDocument
     * @param addresses
     * @param type
     */
    public Customer(String name, String email, String password, String idDocument,
            List<String> addresses, CustomerType type) {
        super(name, email, password, idDocument);
        this.addresses = addresses;
        this.type = type;
    }

    /**
     *
     * @return List of the addresses the customer has
     */
    public List<String> getAddresses() {
        return addresses;
    }

    /**
     * Sets the list of the addresses the customer has
     *
     * @param addresses
     */
    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }

    /**
     *
     * @return total amount of money the customer has spent
     */
    public double getTotalSpent() {
        return totalSpent;
    }

    /**
     * Sets the total amount of money the customer has spent
     *
     * @param totalSpent
     */
    public void setTotalSpent(double totalSpent) {
        this.totalSpent = totalSpent;
    }

    /**
     *
     * @return gets the {@link com.reto1.model.CustomerType} of this customer
     */
    public CustomerType getType() {
        return type;
    }

    /**
     * sets the {@link com.reto1.model.CustomerType} of this customer
     *
     * @param type
     */
    public void setType(CustomerType type) {
        this.type = type;
    }

    /**
     *
     * @return list of all the cartProducts of this customer
     */
    public List<CartItem> getCart() {
        return cart;
    }

    /**
     * sets the list of all the cartProducts of this customer
     *
     * @param cart
     */
    public void setCart(List<CartItem> cart) {
        this.cart = cart;
    }

    /**
     *
     * @return the list of shipmentIds of this customer
     */
    public List<ObjectId> getShipmentIds() {
        return shipmentIds;
    }

    /**
     * sets the list of shipmentIds of this customer
     *
     * @param shipmentIds
     */
    public void setShipmentIds(List<ObjectId> shipmentIds) {
        this.shipmentIds = shipmentIds;
    }

    /**
     * Default tostring of the customer object
     *
     * @return
     */
    @Override
    public String toString() {
        return "Customer{" + super.toString() + ", type=" + type + ", totalSpent=" + totalSpent
                + ", addresses=" + addresses + ", cart=" + cart + ", shipmentIds=" + shipmentIds + '}';
    }
}
