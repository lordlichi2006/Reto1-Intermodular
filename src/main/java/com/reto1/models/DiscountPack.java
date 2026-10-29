package com.reto1.models;
import java.util.ArrayList;
import org.bson.types.ObjectId;
/**
 *
 * @author 20ala
 */
public class DiscountPack {
    private ObjectId id;
    private String name;
    private double discountPercentage;
    private ArrayList<String> products;   // productID
    private int remainingUnits;

    public DiscountPack() {
    }

    public DiscountPack(String name, double discountPercentage,
                        ArrayList<String> products, int remainingUnits) {
        this.name = name;
        this.discountPercentage = discountPercentage;
        this.products = products;
        this.remainingUnits = remainingUnits;
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

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public ArrayList<String> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<String> products) {
        this.products = products;
    }

    public int getRemainingUnits() {
        return remainingUnits;
    }

    public void setRemainingUnits(int remainingUnits) {
        this.remainingUnits = remainingUnits;
    }

    @Override
    public String toString() {
        return "DiscountPack{" + "name=" + name + ", discountPercentage=" + discountPercentage + ", products=" + products + ", remainingUnits=" + remainingUnits + '}';
    }
    
    
    
    
}
