
package com.reto1.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

/**
 * Offer pack (collection "discountPacks").
 * References its products with an array of _id (productIds).
 *
 * @author Alain
 */
public class DiscountPack {

    private ObjectId id;
    private String name;
    /** Discount between 0 and 100. */
    private double discountPercentage;
    /** _id of the products of the "products" collection included in the pack. */
    private List<ObjectId> productIds = new ArrayList<>();
    private int remainingUnits;

    /**
     * Creates an empty discount pack.
     */
    public DiscountPack() {
    }

    /**
     * Creates a discount pack with the specified name, discount percentage,
     * product identifiers and remaining units.
     *
     * @param name               the name of the discount pack
     * @param discountPercentage the discount percentage, between 0 and 100
     * @param productIds         the list of product identifiers included in the pack
     * @param remainingUnits     the number of remaining available packs
     */
    public DiscountPack(String name, double discountPercentage,
                        List<ObjectId> productIds, int remainingUnits) {
        this.name = name;
        this.discountPercentage = discountPercentage;
        this.productIds = productIds;
        this.remainingUnits = remainingUnits;
    }

    /**
     * Returns the MongoDB identifier of the discount pack.
     *
     * @return the discount pack's ObjectId, or {@code null} if it has not been assigned
     */
    public ObjectId getId() {
        return id;
    }

    /**
     * Sets the MongoDB identifier of the discount pack.
     *
     * @param id the ObjectId to assign to the discount pack
     */
    public void setId(ObjectId id) {
        this.id = id;
    }

    /**
     * Returns the name of the discount pack.
     *
     * @return the discount pack's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the discount pack.
     *
     * @param name the name to assign to the discount pack
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the discount percentage applied to the pack.
     *
     * @return the discount percentage, between 0 and 100
     */
    public double getDiscountPercentage() {
        return discountPercentage;
    }

    /**
     * Sets the discount percentage applied to the pack.
     *
     * @param discountPercentage the discount percentage to assign, between 0 and 100
     */
    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    /**
     * Returns the identifiers of the products included in the discount pack.
     *
     * @return the list of product ObjectIds
     */
    public List<ObjectId> getProductIds() {
        return productIds;
    }

    /**
     * Sets the identifiers of the products included in the discount pack.
     *
     * @param productIds the list of product ObjectIds to assign
     */
    public void setProductIds(List<ObjectId> productIds) {
        this.productIds = productIds;
    }

    /**
     * Returns the number of discount packs still available.
     *
     * @return the remaining number of units
     */
    public int getRemainingUnits() {
        return remainingUnits;
    }

    /**
     * Sets the number of discount packs still available.
     *
     * @param remainingUnits the remaining number of units to assign
     */
    public void setRemainingUnits(int remainingUnits) {
        this.remainingUnits = remainingUnits;
    }

    /**
     * Returns a string representation of the discount pack, including
     * its identifier, name, discount percentage, product identifiers
     * and remaining units.
     *
     * @return a string containing the discount pack information
     */
    @Override
    public String toString() {
        return "DiscountPack{" + "id=" + id + ", name=" + name + ", discountPercentage=" + discountPercentage
                + ", productIds=" + productIds + ", remainingUnits=" + remainingUnits + '}';
    }
}
