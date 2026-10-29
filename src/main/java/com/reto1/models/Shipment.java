package com.reto1.models;

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
 * @author 20ala
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

    public Shipment() {
    }

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

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getCustomerId() {
        return customerId;
    }

    public void setCustomerId(ObjectId customerId) {
        this.customerId = customerId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<ShipmentLine> getLines() {
        return lines;
    }

    public void setLines(List<ShipmentLine> lines) {
        this.lines = lines;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public String getShipmentCompany() {
        return shipmentCompany;
    }

    public void setShipmentCompany(String shipmentCompany) {
        this.shipmentCompany = shipmentCompany;
    }

    public ShipmentStatus getStatus() {
        return status;
    }

    public void setStatus(ShipmentStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Shipment{" + "id=" + id + ", customerId=" + customerId + ", createdAt=" + createdAt
                + ", address=" + address + ", lines=" + lines + ", total=" + total + ", trackingId=" + trackingId
                + ", shipmentCompany=" + shipmentCompany + ", status=" + status + '}';
    }
}
