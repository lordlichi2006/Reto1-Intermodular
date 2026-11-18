package com.reto1.controller;

import java.util.ArrayList;
import java.util.Objects;

import com.reto1.dao.UserDao;
import com.reto1.dao.UserImplementacionBD;
import com.reto1.exceptions.DatabaseException;
import com.reto1.exceptions.DniException;
import com.reto1.exceptions.EmailException;
import com.reto1.model.Customer;
import com.reto1.model.User;
import com.reto1.util.UserValidation;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * Controls the customer profile view defined in {@code shopProfile.fxml}.
 * Loads and updates customer details and manages the customer's saved addresses.
 * The title bar and menu buttons are handled by {@link BaseController}.
 */
public class ShopProfileController extends BaseController {

    /** Field for the name of the customer. */
    @FXML
    private TextField txtName;
    /** Field for the email of the customer. */
    @FXML
    private TextField txtEmail;
    /** Label with the customer type, which the customer cannot change. */
    @FXML
    private Label lblType;
    /** List with the saved addresses of the customer. */
    @FXML
    private ListView<String> lstAddresses;
    /** Field for a new address, added to the list from its context menu. */
    @FXML
    private TextField txtNewAddress;
    /** Label that shows the errors of the form. */
    @FXML
    private Label lblError;
    /** Button Save Changes, disabled if the logged-in user is not a customer. */
    @FXML
    private Button btnSave;

    /** Data access object of the "users" collection. */
    private UserDao userDao;

    /**
     * Initializes the customer type choices and loads the logged-in customer's profile.
     * If no DAO has been injected, a database-backed implementation is created.
     */
    @FXML
    private void initialize() {
        if (userDao == null) {
            userDao = new UserImplementacionBD();
        }
        loadProfile();
    }

    /**
     * Sets the DAO used to look up and update customer accounts.
     *
     * @param userDao the DAO to use
     */
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Populates the profile controls from the logged-in customer.
     * If the logged-in user is not a customer, displays an error and disables saving.
     */
    private void loadProfile() {
        if (!(loggedUser instanceof Customer)) {
            lblError.setText("A customer profile is not available.");
            btnSave.setDisable(true);
            return;
        }
        Customer customer = (Customer) loggedUser;
        txtName.setText(customer.getName());
        txtEmail.setText(customer.getEmail());
        lblType.setText(customer.getType() == null ? "" : customer.getType().toString());
        lstAddresses.getItems().setAll(customer.getAddresses() == null
                ? new ArrayList<>() : customer.getAddresses());
        lblError.setText("");
    }

    /**
     * Adds the trimmed address from the input field unless it is empty or already present.
     * Displays a validation message when the address cannot be added.
     */
    @FXML
    private void addAddress() {
        String address = txtNewAddress.getText() == null ? "" : txtNewAddress.getText().trim();
        if (address.isEmpty()) {
            lblError.setText("Enter an address to add.");
            return;
        }
        if (lstAddresses.getItems().contains(address)) {
            lblError.setText("That address is already saved.");
            return;
        }
        lstAddresses.getItems().add(address);
        txtNewAddress.clear();
        lblError.setText("");
    }

    /**
     * Removes the currently selected address from the address list, if one is selected.
     */
    @FXML
    private void removeAddress() {
        int selectedIndex = lstAddresses.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            lstAddresses.getItems().remove(selectedIndex);
            lblError.setText("");
        }
    }

    /**
     * Validates the profile form and persists the updated customer details.
     * The current customer's account ID and other account state are retained.
     */
    @FXML
    private void saveChanges() {
        if (!(loggedUser instanceof Customer)) {
            lblError.setText("A customer profile is not available.");
            return;
        }
        String name = txtName.getText() == null ? "" : txtName.getText().trim();
        String email = txtEmail.getText() == null ? "" : txtEmail.getText().trim();
        Customer customer = (Customer) loggedUser;
        if (name.isEmpty()) {
            lblError.setText("Enter a name.");
            return;
        }
        try {
            UserValidation.validateEmail(email);
            UserValidation.validateIdDocument(customer.getIdDocument());
        } catch (EmailException | DniException e) {
            lblError.setText(e.getMessage());
            return;
        }

        try {
            User existing = userDao.findByEmail(email);
            if (existing != null && !Objects.equals(existing.getId(), customer.getId())) {
                lblError.setText("That email address is already in use.");
                return;
            }
            Customer updatedCustomer = new Customer(name, email, customer.getPassword(), customer.getIdDocument(),
                    new ArrayList<>(lstAddresses.getItems()), customer.getType());
            updatedCustomer.setId(customer.getId());
            updatedCustomer.setTotalSpent(customer.getTotalSpent());
            updatedCustomer.setCart(new ArrayList<>(customer.getCart()));
            updatedCustomer.setShipmentIds(new ArrayList<>(customer.getShipmentIds()));
            if (!userDao.update(updatedCustomer)) {
                lblError.setText("The profile could not be found. Please reload the application.");
                logger.warning("Profile update did not match customer id " + updatedCustomer.getId());
                return;
            }
            loggedUser = updatedCustomer;
            lblError.setText("");
            showInfo("Profile saved successfully.", "My profile");
        } catch (DatabaseException e) {
            logger.severe("Unable to save profile: " + e.getMessage());
            lblError.setText(e.getMessage());
        }
    }
}
