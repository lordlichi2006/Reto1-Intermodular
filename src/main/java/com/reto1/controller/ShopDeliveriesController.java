package com.reto1.controller;

import javafx.fxml.FXML;

/**
 * Controller of the window Shipments View (shopDeliveries.fxml).
 * Shipments of the logged customer and their status.
 * The title bar and the menu buttons are handled by {@link BaseController}.
 */
public class ShopDeliveriesController extends BaseController {

    /**
     * Runs when the window opens.
     */
    @FXML
    private void initialize() {
        // TODO: loadShipments()
    }

    /**
     * Fills tblShipments with the shipments of loggedUser (date, total,
     * company, status).
     */
    private void loadShipments() {
        // TODO: shipmentDao.findByCustomer(loggedUser.getId())
    }

    /**
     * Double click on a shipment (onMouseClicked): shows its lines.
     */
    @FXML
    private void showShipmentLines() {
        // TODO: if it is a double click, show product, price and units of each line
    }
}
