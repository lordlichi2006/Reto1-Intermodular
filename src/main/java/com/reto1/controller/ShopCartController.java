package com.reto1.controller;

import java.util.ArrayList;
import java.util.List;

import com.reto1.dao.ProductDao;
import com.reto1.dao.ProductImplementacionBD;
import com.reto1.dao.ShipmentDao;
import com.reto1.dao.ShipmentImplementacionBD;
import com.reto1.dao.UserDao;
import com.reto1.dao.UserImplementacionBD;
import com.reto1.exceptions.DatabaseException;
import com.reto1.exceptions.StockException;
import com.reto1.model.CartItem;
import com.reto1.model.Customer;
import com.reto1.model.Shipment;
import com.reto1.model.ShipmentLine;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controller of the window Cart View (shopCart.fxml).
 * Customer cart: change units, remove items and purchase.
 * The title bar and the menu buttons are handled by {@link BaseController}.
 */
public class ShopCartController extends BaseController {

    @FXML
    private TableView<CartItem> tblCart;

    @FXML
    private Button btnPurchase;

    @FXML
    private TableColumn<CartItem, String> colName;

    @FXML
    private TableColumn<CartItem, String> colBrand;

    @FXML
    private TableColumn<CartItem, Double> colPrice;

    @FXML
    private TableColumn<CartItem, Integer> colUnits;

    @FXML
    private Spinner<Integer> spnAmount;

    @FXML
    private Label lblTotal;

    @FXML
    private ComboBox<String> cmbAddress;

    private CartItem selectedCartItem;

    private Double totalPrice;

    private Customer customer;

    /**
     * Runs when the window opens.
     */
    @FXML
    private void initialize() {
        btnPurchase.disableProperty().bind(Bindings.isEmpty(tblCart.getItems()));
        if (loggedUser == null || !(loggedUser instanceof Customer)) {
            return;
        }
        customer = (Customer) loggedUser;
        spnAmount.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 99, 1));

        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colUnits.setCellValueFactory(new PropertyValueFactory<>("units"));

        tblCart.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<CartItem>() {
                    @Override
                    public void changed(ObservableValue<? extends CartItem> observable, CartItem oldItem,
                            CartItem newItem) {
                        onCartSelectionChanged(newItem);
                    }
                });
        spnAmount.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue,
                    Integer newValue) {
                changeAmount(newValue);
            }
        });
        loadCart();
        loadAddresses();

    }

    /**
     * Runs every time the user selects an item from the table
     * 
     * @param newItem
     */
    private void onCartSelectionChanged(CartItem newItem) {
        selectedCartItem = newItem;

        if (selectedCartItem != null && spnAmount.getValueFactory() != null) {
            spnAmount.getValueFactory().setValue(selectedCartItem.getUnits());
        }
    }

    /**
     * Fills tblCart with the cart of loggedUser and shows the total.
     */
    private void loadCart() {

        tblCart.getItems().setAll(customer.getCart());

        totalPrice = 0.0;
        for (CartItem item : customer.getCart()) {
            totalPrice += item.getPrice() * item.getUnits();
        }

        lblTotal.setText(String.format("Total: %.2f€", totalPrice));
    }

    /**
     * Fills cmbAddress with the saved addresses of loggedUser.
     */
    private void loadAddresses() {
        ObservableList<String> addresses = FXCollections.observableArrayList(customer.getAddresses());
        cmbAddress.setItems(addresses);

    }

    /**
     * Edit amount: changes the units of the selected cart line.
     */
    private void changeAmount(Integer newUnits) {
        if (selectedCartItem == null || newUnits == null || newUnits < 1
                || selectedCartItem.getUnits() == newUnits) {
            return;
        }

        int previousUnits = selectedCartItem.getUnits();
        selectedCartItem.setUnits(newUnits);
        tblCart.refresh();
        loadCart();

        try {
            UserDao userDao = new UserImplementacionBD();
            if (!userDao.update(customer)) {
                selectedCartItem.setUnits(previousUnits);
                spnAmount.getValueFactory().setValue(previousUnits);
                tblCart.refresh();
                loadCart();
                showError("Could not save the cart quantity.", "Database Error");
            }
        } catch (DatabaseException e) {
            selectedCartItem.setUnits(previousUnits);
            spnAmount.getValueFactory().setValue(previousUnits);
            tblCart.refresh();
            loadCart();
            showError("Could not save the cart quantity: " + e.getMessage(), "Database Error");
        }
    }

    /**
     * Remove button: removes the selected line from the cart.
     */
    @FXML
    private void removeFromCart() {
        if (selectedCartItem == null) {
            return;
        }

        customer.getCart().remove(selectedCartItem);
        tblCart.getItems().remove(selectedCartItem);
        selectedCartItem = null;
        totalPrice = 0.0;
        spnAmount.getValueFactory().setValue(1);

        try {
            UserDao userDao = new UserImplementacionBD();
            if (!userDao.update(customer)) {
                loadCart();
                showError("Could not remove item from cart.", "Database Error");
            }
        } catch (DatabaseException e) {
            loadCart();
            showError("Could not remove item from cart: " + e.getMessage(), "Database Error");
        }
    }

    /**
     * Purchase button: creates the shipment and empties the cart.
     */
    @FXML
    private void purchase() {
        String address = cmbAddress.getSelectionModel().getSelectedItem();
        if (address == null || address.trim().isEmpty()) {
            showError("Select a delivery address before purchasing.", "Missing Address");
            return;
        }
        if (customer.getCart().isEmpty()) {
            showError("Your cart is empty.", "Empty Cart");
            return;
        }

        // create shipment and shipmentline
        List<ShipmentLine> lines = new ArrayList<>();
        for (CartItem item : customer.getCart()) {
            lines.add(new ShipmentLine(
                    item.getProductId(),
                    item.getName(),
                    item.getPrice(),
                    item.getUnits()));
        }

        Shipment shipment = new Shipment(
                customer.getId(),
                address,
                lines,
                totalPrice,
                "Mikelon Envios SA");

        try {
            ProductDao productDao = new ProductImplementacionBD();
            for (CartItem item : customer.getCart()) {
                if (!productDao.decreaseStock(item.getProductId(), item.getUnits())) {
                    throw new StockException("Not enough stock for " + item.getName() + ".");
                }
            }

            ShipmentDao shipmentDao = new ShipmentImplementacionBD();
            UserDao userDao = new UserImplementacionBD();

            shipmentDao.insert(shipment);
            customer.getShipmentIds().add(shipment.getId());

            customer.getCart().clear();
            tblCart.getItems().clear();
            selectedCartItem = null;
            totalPrice = 0.0;
            spnAmount.getValueFactory().setValue(1);

            if (!userDao.update(customer)) {
                showError("Shipment was created, but the customer record could not be updated.",
                        "Database Error");
            }

        } catch (StockException e) {
            showError(e.getMessage(), "Insufficient Stock");
        } catch (DatabaseException e) {
            showError("Could not complete the purchase: " + e.getMessage(), "Database Error");
        }
    }
}
