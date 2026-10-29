package com.reto1.model;

import org.bson.types.ObjectId;

/**
 * Summary of a supplier embedded inside a Product.
 * Keeps the reference (supplierId) plus the name and CIF, so the product
 * table can show who supplies it without querying "suppliers".
 *
 * @author 20ala
 */
public class ProductSupplier {

    /** _id of the supplier in the "suppliers" collection. */
    private ObjectId supplierId;
    private String name;
    private String cif;

    public ProductSupplier() {
    }

    public ProductSupplier(ObjectId supplierId, String name, String cif) {
        this.supplierId = supplierId;
        this.name = name;
        this.cif = cif;
    }

    public ObjectId getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(ObjectId supplierId) {
        this.supplierId = supplierId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    @Override
    public String toString() {
        return "ProductSupplier{" + "supplierId=" + supplierId + ", name=" + name + ", cif=" + cif + '}';
    }
}
