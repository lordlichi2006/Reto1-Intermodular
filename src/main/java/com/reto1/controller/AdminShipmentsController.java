package com.reto1.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controller of the window Admin Shipments (adminShipments.fxml).
 * Workers see the shipments in progress, change their status or cancel them.
 * The title bar and the menu buttons are handled by {@link BaseController}.
 */
public class AdminShipmentsController extends BaseController {

    @FXML
    private Button btnUsers;
    @FXML
    private Button btnLog;

    /**
     * Runs when the window opens.
     */
    @FXML
    private void initialize() {
        applyAdminPermissions(btnUsers, btnLog);
        // TODO: applyAdminPermissions(btnUsers, btnLog) and loadShipments()
    }

    /**
     * Fills the table with the shipments that are not finished.
     */
    private void loadShipments() {
        // TODO: shipmentDao.findAll() (only IN_PROGRESS and IN_SHIPMENT)
    }

    /**
     * Click on a shipment of the table: loads its status in the combo.
     */
    @FXML
    private void selectShipment() {
        // TODO
    }

    /**
     * Confirm changes button: saves the status chosen in the combo.
     */
    @FXML
    private void confirmChanges() {
        // TODO: shipment selected? status changed?
        //       shipmentDao.updateStatus(id, status) and loadShipments()
    }

    /**
     * Cancel shipment button: marks the selected shipment as CANCELLED.
     */
    @FXML
    private void cancelShipment() {
        // TODO: shipment selected? can it be cancelled (not ARRIVED)? confirm(...)?
        //       shipmentDao.updateStatus(id, ShipmentStatus.CANCELLED)
    }
}
