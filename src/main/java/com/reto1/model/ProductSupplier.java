
package com.reto1.model;

import org.bson.types.ObjectId;

/**
 * Summary of a supplier embedded inside a Product.
 * Keeps the reference (supplierId) plus the name and CIF, so the product
 * table can show who supplies it without querying "suppliers".
 *
 * @author Ivan
 */
public class ProductSupplier {

    /** _id of the supplier in the "suppliers" collection. */
    private ObjectId supplierId;
    private String name;
    private String cif;

    /**
     * Creates an empty product supplier.
     */
    public ProductSupplier() {
    }

    /**
     * Creates a product supplier with the specified supplier identifier,
     * name and tax identification number.
     *
     * @param supplierId the identifier of the supplier in the "suppliers" collection
     * @param name       the supplier's name
     * @param cif        the supplier's tax identification number
     */
    public ProductSupplier(ObjectId supplierId, String name, String cif) {
        this.supplierId = supplierId;
        this.name = name;
        this.cif = cif;
    }

    /**
     * Returns the identifier of the supplier associated with this product.
     *
     * @return the supplier's ObjectId
     */
    public ObjectId getSupplierId() {
        return supplierId;
    }

    /**
     * Sets the identifier of the supplier associated with this product.
     *
     * @param supplierId the supplier's ObjectId
     */
    public void setSupplierId(ObjectId supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * Returns the supplier's name.
     *
     * @return the supplier's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the supplier's name.
     *
     * @param name the name to assign to the supplier
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the supplier's tax identification number.
     *
     * @return the supplier's CIF
     */
    public String getCif() {
        return cif;
    }

    /**
     * Sets the supplier's tax identification number.
     *
     * @param cif the CIF to assign to the supplier
     */
    public void setCif(String cif) {
        this.cif = cif;
    }

    /**
     * Returns a string representation of the product supplier, including
     * the supplier identifier, name and CIF.
     *
     * @return a string containing the supplier information
     */
    @Override
    public String toString() {
        return "ProductSupplier{" + "supplierId=" + supplierId + ", name=" + name + ", cif=" + cif + '}';
    }
}
