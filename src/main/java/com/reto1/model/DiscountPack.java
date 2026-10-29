package com.reto1.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

/**
 * Offer pack (collection "discountPacks").
 * References its products with an array of _id (productIds).
 *
 * @author 20ala
 */
public class DiscountPack {

    private ObjectId id;
    private String name;
    /** Discount between 0 and 100. */
    private double discountPercentage;
    /** _id of the products of the "products" collection included in the pack. */
    private List<ObjectId> productIds = new ArrayList<>();
    private int remainingUnits;

    public DiscountPack() {
    }

    public DiscountPack(String name, double discountPercentage,
                        List<ObjectId> productIds, int remainingUnits) {
        this.name = name;
        this.discountPercentage = discountPercentage;
        this.productIds = productIds;
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

    public List<ObjectId> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<ObjectId> productIds) {
        this.productIds = productIds;
    }

    public int getRemainingUnits() {
        return remainingUnits;
    }

    public void setRemainingUnits(int remainingUnits) {
        this.remainingUnits = remainingUnits;
    }

    @Override
    public String toString() {
        return "DiscountPack{" + "id=" + id + ", name=" + name + ", discountPercentage=" + discountPercentage
                + ", productIds=" + productIds + ", remainingUnits=" + remainingUnits + '}';
    }
}
