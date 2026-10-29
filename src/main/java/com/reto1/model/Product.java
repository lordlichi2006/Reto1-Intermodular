package com.reto1.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

/**
 * Product of the shop (collection "products").
 * Embeds a summary of its suppliers ({@link ProductSupplier}).
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
    private List<ProductSupplier> suppliers = new ArrayList<>();

    public Product() {
    }

    public Product(String name, String brand, double price, int stock,
                   String description, List<ProductSupplier> suppliers) {
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.suppliers = suppliers;
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

    public List<ProductSupplier> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(List<ProductSupplier> suppliers) {
        this.suppliers = suppliers;
    }

    @Override
    public String toString() {
        return "Product{" + "id=" + id + ", name=" + name + ", brand=" + brand + ", price=" + price
                + ", stock=" + stock + ", description=" + description + ", suppliers=" + suppliers + '}';
    }
}
