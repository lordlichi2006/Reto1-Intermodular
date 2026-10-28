/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.reto1.models;

/**
 *
 * @author 20ala
 */
public class CartItem {
    private String name;
    private String brand;
    private double price;
    private int stock;
    private String productID;
    private int unitsSelected;

    public CartItem() {
    }

    public CartItem(String name, String brand, double price, int stock,
                    String productID, int unitsSelected) {
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.stock = stock;
        this.productID = productID;
        this.unitsSelected = unitsSelected;
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

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public int getUnitsSelected() {
        return unitsSelected;
    }

    public void setUnitsSelected(int unitsSelected) {
        this.unitsSelected = unitsSelected;
    }

    @Override
    public String toString() {
        return "CartItem{" + "name=" + name + ", brand=" + brand + ", price=" + price + ", stock=" + stock + ", productID=" + productID + ", unitsSelected=" + unitsSelected + '}';
    }
    
    

}