/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.reto1.models;

/**
 *
 * @author 20ala
 */
public class Shipment {
    private String shipmentID;
    private String trackingID;
    private String shipmentCompany;
    private ShipmentStatus status;

    public Shipment(String shipmentID, String trackingID,
                    String shipmentCompany, ShipmentStatus status) {
        this.shipmentID = shipmentID;
        this.trackingID = trackingID;
        this.shipmentCompany = shipmentCompany;
        this.status = status;
    }

    public String getShipmentID() {
        return shipmentID;
    }

    public void setShipmentID(String shipmentID) {
        this.shipmentID = shipmentID;
    }

    public String getTrackingID() {
        return trackingID;
    }

    public void setTrackingID(String trackingID) {
        this.trackingID = trackingID;
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
        return "Shipment{" + "shipmentID=" + shipmentID + ", trackingID=" + trackingID + ", shipmentCompany=" + shipmentCompany + ", status=" + status + '}';
    }

    

}
