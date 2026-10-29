package com.reto1.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.types.ObjectId;

/**
 * Shop customer (stored in "users" with _t = "customer").
 * <ul>
 * <li>Embedded: addresses and cart (always read together with the customer).</li>
 * <li>Referenced: shipmentIds, an array of _id of the "shipments" collection.</li>
 * </ul>
 *
 * @author Ekaitz.Rivero
 */
@BsonDiscriminator(key = "_t", value = "customer")
public class Customer extends User {

    private List<String> addresses = new ArrayList<>();
    private double totalSpent;
    private CustomerType type;
    private List<CartItem> cart = new ArrayList<>();
    private List<ObjectId> shipmentIds = new ArrayList<>();

    public Customer() {
        super();
    }

    public Customer(String name, String email, String password, String idDocument,
                    List<String> addresses, CustomerType type) {
        super(name, email, password, idDocument);
        this.addresses = addresses;
        this.type = type;
    }

    public List<String> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }

    public double getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(double totalSpent) {
        this.totalSpent = totalSpent;
    }

    public CustomerType getType() {
        return type;
    }

    public void setType(CustomerType type) {
        this.type = type;
    }

    public List<CartItem> getCart() {
        return cart;
    }

    public void setCart(List<CartItem> cart) {
        this.cart = cart;
    }

    public List<ObjectId> getShipmentIds() {
        return shipmentIds;
    }

    public void setShipmentIds(List<ObjectId> shipmentIds) {
        this.shipmentIds = shipmentIds;
    }

    @Override
    public String toString() {
        return "Customer{" + super.toString() + ", type=" + type + ", totalSpent=" + totalSpent
                + ", addresses=" + addresses + ", cart=" + cart + ", shipmentIds=" + shipmentIds + '}';
    }
}
