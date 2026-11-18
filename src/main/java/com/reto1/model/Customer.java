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

    /** Delivery addresses saved by the customer (embedded). */
    private List<String> addresses = new ArrayList<>();
    /** Money spent by the customer in every purchase. */
    private double totalSpent;
    /** Type of customer: individual or business. */
    private CustomerType type;
    /** Lines of the cart (embedded). */
    private List<CartItem> cart = new ArrayList<>();
    /** _id of the shipments of the customer in the "shipments" collection. */
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
     * @param name name of the customer
     * @param email email of the customer
     * @param password password of the customer
     * @param idDocument DNI, NIE or CIF of the customer
     * @param addresses delivery addresses of the customer
     * @param type type of customer
     */
    public Customer(String name, String email, String password, String idDocument,
            List<String> addresses, CustomerType type) {
        super(name, email, password, idDocument);
        this.addresses = addresses;
        this.type = type;
    }

    /**
     * Returns list of the addresses the customer has.
     *
     * @return List of the addresses the customer has
     */
    public List<String> getAddresses() {
        return addresses;
    }

    /**
     * Sets the list of the addresses the customer has
     *
     * @param addresses delivery addresses of the customer
     */
    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }

    /**
     * Returns total amount of money the customer has spent.
     *
     * @return total amount of money the customer has spent
     */
    public double getTotalSpent() {
        return totalSpent;
    }

    /**
     * Sets the total amount of money the customer has spent
     *
     * @param totalSpent money spent by the customer
     */
    public void setTotalSpent(double totalSpent) {
        this.totalSpent = totalSpent;
    }

    /**
     * Returns gets the {@link com.reto1.model.CustomerType} of this customer.
     *
     * @return gets the {@link com.reto1.model.CustomerType} of this customer
     */
    public CustomerType getType() {
        return type;
    }

    /**
     * sets the {@link com.reto1.model.CustomerType} of this customer
     *
     * @param type type of customer
     */
    public void setType(CustomerType type) {
        this.type = type;
    }

    /**
     * Returns list of all the cartProducts of this customer.
     *
     * @return list of all the cartProducts of this customer
     */
    public List<CartItem> getCart() {
        return cart;
    }

    /**
     * sets the list of all the cartProducts of this customer
     *
     * @param cart lines of the cart
     */
    public void setCart(List<CartItem> cart) {
        this.cart = cart;
    }

    /**
     * Returns the list of shipmentIds of this customer.
     *
     * @return the list of shipmentIds of this customer
     */
    public List<ObjectId> getShipmentIds() {
        return shipmentIds;
    }

    /**
     * sets the list of shipmentIds of this customer
     *
     * @param shipmentIds _id of the shipments of the customer
     */
    public void setShipmentIds(List<ObjectId> shipmentIds) {
        this.shipmentIds = shipmentIds;
    }

    /**
     * Default tostring of the customer object
     *
     * @return the text with the fields of the customer
     */
    @Override
    public String toString() {
        return "Customer{" + super.toString() + ", type=" + type + ", totalSpent=" + totalSpent
                + ", addresses=" + addresses + ", cart=" + cart + ", shipmentIds=" + shipmentIds + '}';
    }
}
