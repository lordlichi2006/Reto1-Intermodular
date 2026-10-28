/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.reto1.models;

import java.util.ArrayList;

/**
 *
 * @author Ekaitz.Rivero
 */
public class Customer extends User{
    private ArrayList<String> addresses;
    private double totalSpent;
    private CustomerType type;
    private ArrayList<CartItem> cart;
    private ArrayList<Shipment> shipments;

    public Customer() {
        super();
    }

    public Customer(String name, String email, String password, String idDocument,
                    ArrayList<String> addresses, double totalSpent, CustomerType type,
                    ArrayList<CartItem> cart, ArrayList<Shipment> shipments) {
        super(name, email, password, idDocument);
        this.addresses = addresses;
        this.totalSpent = totalSpent;
        this.type = type;
        this.cart = cart;
        this.shipments = shipments;
        
    }

    public ArrayList<String> getAddresses() {
        return addresses;
    }

    public void setAddresses(ArrayList<String> addresses) {
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

    public ArrayList<CartItem> getCart() {
        return cart;
    }

    public void setCart(ArrayList<CartItem> cart) {
        this.cart = cart;
    }

    public ArrayList<Shipment> getShipments() {
        return shipments;
    }

    public void setShipments(ArrayList<Shipment> shipments) {
        this.shipments = shipments;
    }

    @Override
    public String toString() {
        return "Customer{" + "addresses=" + addresses + ", totalSpent=" + totalSpent + ", type=" + type + ", cart=" + cart + ", shipments=" + shipments + '}';
    }
    
    
}
