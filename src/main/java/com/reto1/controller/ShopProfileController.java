package com.reto1.controller;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Pattern;

import com.reto1.dao.UserDao;
import com.reto1.dao.UserImplementacionBD;
import com.reto1.exceptions.DatabaseException;
import com.reto1.model.Customer;
import com.reto1.model.CustomerType;
import com.reto1.model.User;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * Controls the customer profile view defined in {@code shopProfile.fxml}.
 * Loads and updates customer details and manages the customer's saved addresses.
 * The title bar and menu buttons are handled by {@link BaseController}.
 */
public class ShopProfileController extends BaseController {

    /** Regular expression used to validate the email address entered in the profile. */
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    @FXML
    private TextField txtName;
    @FXML
    private TextField txtEmail;
    @FXML
    private ComboBox<CustomerType> cmbType;
    @FXML
    private ListView<String> lstAddresses;
    @FXML
    private TextField txtNewAddress;
    @FXML
    private Label lblError;
    @FXML
    private Button btnSave;

    private UserDao userDao;

    /**
     * Initializes the customer type choices and loads the logged-in customer's profile.
     * If no DAO has been injected, a database-backed implementation is created.
     */
    @FXML
    private void initialize() {
        cmbType.getItems().setAll(CustomerType.values());
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
        cmbType.getSelectionModel().select(customer.getType());
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
        if (name.isEmpty() || !EMAIL_PATTERN.matcher(email).matches() || cmbType.getValue() == null) {
            lblError.setText("Enter a name, a valid email address and a customer type.");
            return;
        }

        Customer customer = (Customer) loggedUser;
        try {
            User existing = userDao.findByEmail(email);
            if (existing != null && !Objects.equals(existing.getId(), customer.getId())) {
                lblError.setText("That email address is already in use.");
                return;
            }
            Customer updatedCustomer = new Customer(name, email, customer.getPassword(), customer.getIdDocument(),
                    new ArrayList<>(lstAddresses.getItems()), cmbType.getValue());
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
