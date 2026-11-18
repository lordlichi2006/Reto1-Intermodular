package com.reto1.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.reto1.dao.ShipmentDao;
import com.reto1.dao.ShipmentImplementacionBD;
import com.reto1.exceptions.DatabaseException;
import com.reto1.model.Shipment;
import com.reto1.model.ShipmentStatus;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

/**
 * Controller of the window Admin Shipments (adminShipments.fxml).
 * Workers see the shipments in progress, change their status or cancel them.
 * The title bar and the menu buttons are handled by {@link BaseController}.
 */
public class AdminShipmentsController extends BaseController {

    /** Menu button USERS, hidden for workers that are not admins. */
    @FXML
    private Button btnUsers;
    /** Menu button LOG, hidden for workers that are not admins. */
    @FXML
    private Button btnLog;
    /** Table with the shipments in progress or in shipment. */
    @FXML
    private TableView<Shipment> shipmentTable;
    /** Column with the date of the shipment (dd/MM/yyyy). */
    @FXML
    private TableColumn<Shipment, String> colDate;
    /** Column with the delivery address. */
    @FXML
    private TableColumn<Shipment, String> colAddress;
    /** Column with the total of the shipment, with two decimals. */
    @FXML
    private TableColumn<Shipment, String> colTotal;
    /** Column with the shipping company. */
    @FXML
    private TableColumn<Shipment, String> colCompany;
    /** Column with the status of the shipment. */
    @FXML
    private TableColumn<Shipment, String> colStatus;
    /** Combo with the new status for the selected shipment. */
    @FXML
    private ComboBox<ShipmentStatus> cmbStatus;
    /** Button Confirm changes, disabled while no shipment is selected. */
    @FXML
    private Button btnConfirm;
    /** Button Cancel shipment, disabled while no shipment is selected. */
    @FXML
    private Button btnCancel;

    /** Data access object of the "shipments" collection. */
    private ShipmentDao shipmentDao;

    /**
     * Runs when the window opens.
     */
    @FXML
    private void initialize() {
        applyAdminPermissions(btnUsers, btnLog);
        if (shipmentDao == null) {
            shipmentDao = new ShipmentImplementacionBD();
        }
        configureTable();
        cmbStatus.setItems(FXCollections.observableArrayList(
                ShipmentStatus.IN_PROGRESS, ShipmentStatus.IN_SHIPMENT, ShipmentStatus.ARRIVED));
        shipmentTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Shipment>() {
            @Override
            public void changed(ObservableValue<? extends Shipment> observable, Shipment oldShipment,
                    Shipment shipment) {
                selectShipment(shipment);
            }
        });
        selectShipment(null);
        loadShipments();
    }

    /**
     * Sets the DAO used to load shipments and save status changes.
     *
     * @param shipmentDao the DAO to use
     */
    public void setShipmentDao(ShipmentDao shipmentDao) {
        this.shipmentDao = shipmentDao;
    }

    /** Sets how each column of shipmentTable gets its text from the shipment. */
    private void configureTable() {
        colDate.setCellValueFactory(new Callback<CellDataFeatures<Shipment, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Shipment, String> cell) {
                String date;

                date = "";
                if (cell.getValue().getCreatedAt() != null) {
                    date = new SimpleDateFormat("dd/MM/yyyy").format(cell.getValue().getCreatedAt());
                }
                return new ReadOnlyStringWrapper(date);
            }
        });
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colTotal.setCellValueFactory(new Callback<CellDataFeatures<Shipment, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Shipment, String> cell) {
                return new ReadOnlyStringWrapper(String.format("%.2f", cell.getValue().getTotal()));
            }
        });
        colCompany.setCellValueFactory(new Callback<CellDataFeatures<Shipment, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Shipment, String> cell) {
                return new ReadOnlyStringWrapper(cell.getValue().getShipmentCompany() == null
                        ? "" : cell.getValue().getShipmentCompany());
            }
        });
        colStatus.setCellValueFactory(new Callback<CellDataFeatures<Shipment, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Shipment, String> cell) {
                return new ReadOnlyStringWrapper(cell.getValue().getStatus() == null
                        ? "" : cell.getValue().getStatus().name());
            }
        });
    }

    /**
     * Fills the table with shipments that are still in progress or in shipment.
     *
     * @return true if the shipments were loaded, false if the database failed
     */
    private boolean loadShipments() {
        try {
            ArrayList<Shipment> activeShipments = new ArrayList<>();
            for (Shipment shipment : shipmentDao.findAll()) {
                if (shipment.getStatus() == ShipmentStatus.IN_PROGRESS
                        || shipment.getStatus() == ShipmentStatus.IN_SHIPMENT) {
                    activeShipments.add(shipment);
                }
            }
            shipmentTable.setItems(FXCollections.observableArrayList(activeShipments));
            return true;
        } catch (DatabaseException e) {
            logger.severe("Unable to load shipments: " + e.getMessage());
            showError(e.getMessage(), "Shipments");
            return false;
        }
    }

    /**
     * Click on a shipment of the table: loads its status in the combo.
     */
    @FXML
    private void selectShipment() {
        selectShipment(shipmentTable.getSelectionModel().getSelectedItem());
    }

    /**
     * Enables the status combo and the buttons when a shipment is selected and
     * shows its status in the combo; disables them when nothing is selected.
     *
     * @param shipment selected shipment, or null if there is none
     */
    private void selectShipment(Shipment shipment) {
        boolean hasSelection = shipment != null;
        btnConfirm.setDisable(!hasSelection);
        btnCancel.setDisable(!hasSelection);
        cmbStatus.setDisable(!hasSelection);
        if (hasSelection) {
            cmbStatus.getSelectionModel().select(shipment.getStatus());
        } else {
            cmbStatus.getSelectionModel().clearSelection();
        }
    }

    /**
     * Confirm changes button: saves the status chosen in the combo.
     */
    @FXML
    private void confirmChanges() {
        Shipment selected = shipmentTable.getSelectionModel().getSelectedItem();
        ShipmentStatus status = cmbStatus.getValue();
        if (selected == null || status == null) {
            showError("Select a shipment and a status.", "Shipments");
            return;
        }
        saveStatus(selected, status);
    }

    /**
     * Cancel shipment button: marks the selected shipment as CANCELLED.
     */
    @FXML
    private void cancelShipment() {
        Shipment selected = shipmentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Select a shipment to cancel.", "Shipments");
            return;
        }
        if (!confirm("Are you sure you want to cancel this shipment?", "Cancel shipment")) {
            return;
        }
        saveStatus(selected, ShipmentStatus.CANCELLED);
    }

    /**
     * Saves the new status with {@link ShipmentDao#updateStatus} and reloads
     * the table. Shows an error if the shipment no longer exists or the
     * database fails.
     *
     * @param shipment shipment to change
     * @param status   new status of the shipment
     */
    private void saveStatus(Shipment shipment, ShipmentStatus status) {
        try {
            if (!shipmentDao.updateStatus(shipment.getId(), status)) {
                showError("The shipment no longer exists.", "Shipments");
                loadShipments();
                return;
            }
            if (loadShipments()) {
                showInfo("Shipment status updated successfully.", "Shipments");
            }
        } catch (DatabaseException e) {
            logger.severe("Unable to update shipment status: " + e.getMessage());
            showError(e.getMessage(), "Shipments");
        }
    }
}
