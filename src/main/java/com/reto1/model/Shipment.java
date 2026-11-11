
package com.reto1.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

/**
 * Order sent to a customer (collection "shipments").
 * <ul>
 * <li>Referenced: customerId (the customer in "users").</li>
 * <li>Embedded: the purchased lines and the delivery address.</li>
 * </ul>
 *
 * @author Ivan
 */
public class Shipment {

    private ObjectId id;
    private ObjectId customerId;
    private Date createdAt;
    private String address;
    private List<ShipmentLine> lines = new ArrayList<>();
    private double total;
    private String trackingId;
    private String shipmentCompany;
    private ShipmentStatus status;

    /**
     * Creates an empty shipment.
     */
    public Shipment() {
    }

    /**
     * Creates a shipment with the specified customer, delivery address,
     * purchased products, total amount and shipping company.
     * The creation date is automatically initialized to the current date,
     * and the shipment status is set to {@code IN_PROGRESS}.
     *
     * @param customerId     the identifier of the customer receiving the shipment
     * @param address        the delivery address
     * @param lines          the list of purchased shipment lines
     * @param total          the total shipment amount
     * @param shipmentCompany the company responsible for delivering the shipment
     */
    public Shipment(ObjectId customerId, String address, List<ShipmentLine> lines,
                    double total, String shipmentCompany) {
        this.customerId = customerId;
        this.address = address;
        this.lines = lines;
        this.total = total;
        this.shipmentCompany = shipmentCompany;
        this.createdAt = new Date();
        this.status = ShipmentStatus.IN_PROGRESS;
    }

    /**
     * Returns the MongoDB identifier of the shipment.
     *
     * @return the shipment's ObjectId, or {@code null} if it has not been assigned
     */
    public ObjectId getId() {
        return id;
    }

    /**
     * Sets the MongoDB identifier of the shipment.
     *
     * @param id the ObjectId to assign to the shipment
     */
    public void setId(ObjectId id) {
        this.id = id;
    }

    /**
     * Returns the identifier of the customer associated with the shipment.
     *
     * @return the customer's ObjectId
     */
    public ObjectId getCustomerId() {
        return customerId;
    }

    /**
     * Sets the identifier of the customer associated with the shipment.
     *
     * @param customerId the customer's ObjectId
     */
    public void setCustomerId(ObjectId customerId) {
        this.customerId = customerId;
    }

    /**
     * Returns the date and time when the shipment was created.
     *
     * @return the shipment creation date
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation date of the shipment.
     *
     * @param createdAt the date to assign to the shipment
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Returns the delivery address associated with the shipment.
     *
     * @return the delivery address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the delivery address associated with the shipment.
     *
     * @param address the delivery address to assign
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Returns the list of purchased products included in the shipment.
     *
     * @return the list of shipment lines
     */
    public List<ShipmentLine> getLines() {
        return lines;
    }

    /**
     * Sets the list of purchased products included in the shipment.
     *
     * @param lines the list of shipment lines to assign
     */
    public void setLines(List<ShipmentLine> lines) {
        this.lines = lines;
    }

    /**
     * Returns the total amount of the shipment.
     *
     * @return the total shipment amount
     */
    public double getTotal() {
        return total;
    }

    /**
     * Sets the total amount of the shipment.
     *
     * @param total the total amount to assign
     */
    public void setTotal(double total) {
        this.total = total;
    }

    /**
     * Returns the shipment's tracking identifier.
     *
     * @return the tracking identifier, or {@code null} if it has not been assigned
     */
    public String getTrackingId() {
        return trackingId;
    }

    /**
     * Sets the shipment's tracking identifier.
     *
     * @param trackingId the tracking identifier to assign
     */
    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    /**
     * Returns the name of the company responsible for delivering the shipment.
     *
     * @return the shipping company
     */
    public String getShipmentCompany() {
        return shipmentCompany;
    }

    /**
     * Sets the company responsible for delivering the shipment.
     *
     * @param shipmentCompany the shipping company to assign
     */
    public void setShipmentCompany(String shipmentCompany) {
        this.shipmentCompany = shipmentCompany;
    }

    /**
     * Returns the current status of the shipment.
     *
     * @return the shipment's current status
     */
    public ShipmentStatus getStatus() {
        return status;
    }

    /**
     * Sets the current status of the shipment.
     *
     * @param status the shipment status to assign
     */
    public void setStatus(ShipmentStatus status) {
        this.status = status;
    }

    /**
     * Returns a string representation of the shipment, including its
     * identifier, customer, creation date, delivery address, purchased
     * products, total amount, tracking identifier, shipping company and status.
     *
     * @return a string containing the shipment information
     */
    @Override
    public String toString() {
        return "Shipment{" + "id=" + id + ", customerId=" + customerId + ", createdAt=" + createdAt
                + ", address=" + address + ", lines=" + lines + ", total=" + total + ", trackingId=" + trackingId
                + ", shipmentCompany=" + shipmentCompany + ", status=" + status + '}';
    }
}
