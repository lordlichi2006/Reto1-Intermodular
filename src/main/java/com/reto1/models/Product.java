/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.reto1.models;
import java.util.ArrayList;
import org.bson.types.ObjectId;
/**
 *
 * @author 20ala
 */
public class Product {
    private ObjectId id;
    private String name;
    private String brand;
    private double price;
    private int stock;
    private String description;
    private ArrayList<Supplier> supplier;

    public Product() {
    }

    public Product(String name, String brand, double price, int stock,
                   String description, ArrayList<Supplier> supplier) {
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.supplier = supplier;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Supplier> getSupplier() {
        return supplier;
    }

    public void setSupplier(ArrayList<Supplier> supplier) {
        this.supplier = supplier;
    }

    @Override
    public String toString() {
        return "Product{" + "name=" + name + ", brand=" + brand + ", price=" + price + ", stock=" + stock + ", description=" + description + ", supplier=" + supplier + '}';
    }
    
}
