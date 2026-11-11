
package com.reto1.model;

import org.bson.types.ObjectId;

/**
 * Represents a supplier of the shop, storing the supplier's identification
 * and contact information.
 *
 * @author Ekaitz.Rivero
 */
public class Supplier {
    private ObjectId id;
    private String name;
    private String cif;
    private String email;
    private String telephone;

    /**
     * Creates an empty supplier.
     */
    public Supplier() {
    }

    /**
     * Creates a supplier with the specified identification and contact information.
     *
     * @param name      the supplier's name
     * @param cif       the supplier's tax identification number
     * @param email     the supplier's email address
     * @param telephone the supplier's telephone number
     */
    public Supplier(String name, String cif, String email, String telephone) {
        this.name = name;
        this.cif = cif;
        this.email = email;
        this.telephone = telephone;
    }

    /**
     * Returns the supplier's MongoDB identifier.
     *
     * @return the supplier's ObjectId, or {@code null} if it has not been assigned
     */
    public ObjectId getId() {
        return id;
    }

    /**
     * Sets the supplier's MongoDB identifier.
     *
     * @param id the ObjectId to assign to the supplier
     */
    public void setId(ObjectId id) {
        this.id = id;
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
     * Returns the supplier's email address.
     *
     * @return the supplier's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the supplier's email address.
     *
     * @param email the email address to assign to the supplier
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the supplier's telephone number.
     *
     * @return the supplier's telephone number
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * Sets the supplier's telephone number.
     *
     * @param telephone the telephone number to assign to the supplier
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * Returns a string representation of the supplier, including its
     * name, CIF, email address and telephone number.
     *
     * @return a string containing the supplier's information
     */
    @Override
    public String toString() {
        return "Supplier{" + "name=" + name + ", cif=" + cif + ", email=" + email + ", telephone=" + telephone + '}';
    }
    
    
}
