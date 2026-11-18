package com.reto1.controller;

import java.util.Date;

import com.reto1.dao.ShipmentDao;
import com.reto1.dao.ShipmentImplementacionBD;
import com.reto1.exceptions.DatabaseException;
import com.reto1.model.Customer;
import com.reto1.model.Shipment;
import com.reto1.model.ShipmentLine;
import com.reto1.model.ShipmentStatus;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

/**
 * Controller of the window Shipments View (shopDeliveries.fxml).
 * Shipments of the logged customer and their status.
 * The title bar and the menu buttons are handled by {@link BaseController}.
 */
public class ShopDeliveriesController extends BaseController {

    /** Table with the shipments of the customer. */
    @FXML
    private TableView<Shipment> tblShipments;

    /** Column with the date of the shipment. */
    @FXML
    private TableColumn<Shipment, Date> colDate;

    /** Column with the total of the shipment. */
    @FXML
    private TableColumn<Shipment, Double> colTotal;

    /** Column with the shipping company. */
    @FXML
    private TableColumn<Shipment, String> colCompany;

    /** Column with the delivery address. */
    @FXML
    private TableColumn<Shipment, String> colAddress;

    /** Column with the status of the shipment. */
    @FXML
    private TableColumn<Shipment, ShipmentStatus> colStatus;

    /** Label that shows the errors of the window. */
    @FXML
    private Label lblError;

    /** Logged-in customer whose shipments are shown. */
    private Customer customer;

    /**
     * Runs when the window opens.
     */
    @FXML
    private void initialize() {
        colDate.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colCompany.setCellValueFactory(new PropertyValueFactory<>("shipmentCompany"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        lblError.setText("");

        if (!(loggedUser instanceof Customer)) {
            lblError.setText("A customer account is required to view shipments.");
            return;
        }

        customer = (Customer) loggedUser;
        loadShipments();
    }

    /**
     * Fills tblShipments with the shipments of loggedUser (date, total,
     * company, address, status).
     */
    private void loadShipments() {
        try {
            ShipmentDao shipmentDao = new ShipmentImplementacionBD();
            tblShipments.getItems().setAll(shipmentDao.findByCustomer(customer.getId()));
        } catch (DatabaseException e) {
            logger.severe("Unable to load customer shipments: " + e.getMessage());
            lblError.setText("Could not load shipments: " + e.getMessage());
        }
    }

    /**
     * Double click on a shipment (onMouseClicked): shows its lines.
     *
     * @param event mouse event of the click
     */
    @FXML
    private void showShipmentLines(MouseEvent event) {
        if (event.getClickCount() != 2) {
            return;
        }

        Shipment shipment = tblShipments.getSelectionModel().getSelectedItem();
        if (shipment == null) {
            return;
        }

        if (shipment.getLines() == null || shipment.getLines().isEmpty()) {
            showInfo("This shipment has no products.", "Shipment details");
            return;
        }

        StringBuilder message = new StringBuilder();
        for (ShipmentLine line : shipment.getLines()) {
            message.append(line.getName())
                    .append(" - ")
                    .append(line.getUnits())
                    .append(" x ")
                    .append(String.format("%.2f€", line.getPrice()))
                    .append("\n");
        }
        showInfo(message.toString().trim(), "Shipment details");
    }
}
