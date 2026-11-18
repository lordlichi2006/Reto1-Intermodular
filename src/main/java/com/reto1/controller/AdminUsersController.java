package com.reto1.controller;

import java.util.ArrayList;
import java.util.Objects;

import com.reto1.dao.ShipmentDao;
import com.reto1.dao.ShipmentImplementacionBD;
import com.reto1.dao.UserDao;
import com.reto1.dao.UserImplementacionBD;
import com.reto1.exceptions.DatabaseException;
import com.reto1.exceptions.DniException;
import com.reto1.exceptions.EmailException;
import com.reto1.model.Customer;
import com.reto1.model.CustomerType;
import com.reto1.model.Role;
import com.reto1.model.User;
import com.reto1.model.Worker;
import com.reto1.util.UserValidation;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

/**
 * Controller of the window Admin Users (adminUsers.fxml).
 * Only for admins: edit and remove users.
 * The title bar and the menu buttons are handled by {@link BaseController}.
 */
public class AdminUsersController extends BaseController {

    /** Menu button USERS, hidden for workers that are not admins. */
    @FXML
    private Button btnUsers;
    /** Menu button LOG, hidden for workers that are not admins. */
    @FXML
    private Button btnLog;
    /** Button Delete user, disabled when no user or the logged-in user is selected. */
    @FXML
    private Button btnDelete;
    /** Table with every user, customers and workers. */
    @FXML
    private TableView<User> userTable;
    /** Field for the name of the user. */
    @FXML
    private TextField tfName;
    /** Field for the email of the user. */
    @FXML
    private TextField tfEmail;
    /** Field for the DNI, NIE or CIF of the user. */
    @FXML
    private TextField tfIdDocument;
    /** Field for a new password; if it is left empty, the password does not change. */
    @FXML
    private PasswordField pfPasswd;
    /** Combo with the customer type, enabled only for customers. */
    @FXML
    private ComboBox<CustomerType> cmbType;
    /** Combo with the worker role, enabled only for workers. */
    @FXML
    private ComboBox<Role> cmbRole;
    /** List with the addresses of the customer being edited. */
    @FXML
    private ListView<String> lstAddresses;
    /** Field for a new address of the customer being edited. */
    @FXML
    private TextField tfNewAddress;
    /** Button Add address, enabled only while a customer is being edited. */
    @FXML
    private Button btnAddAddress;

    /** Data access object of the "users" collection. */
    private UserDao userDao;
    /**
     * Data access object of the "shipments" collection, used to check the
     * shipments in progress before deleting a customer.
     */
    private ShipmentDao shipmentDao;
    /** User loaded in the form with Edit user, or null if there is none. */
    private User editingUser;

    /**
     * Runs when the window opens.
     */
    @FXML
    private void initialize() {
        applyAdminPermissions(btnUsers, btnLog);
        cmbType.getItems().setAll(CustomerType.values());
        cmbRole.getItems().setAll(Role.values());
        configureTable();
        btnDelete.setDisable(true);
        tfNewAddress.setDisable(true);
        btnAddAddress.setDisable(true);
        userTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue<? extends User> observable, User oldUser, User user) {
                btnDelete.setDisable(user == null || isLoggedInUser(user));
                if (user == null) {
                    clearForm();
                }
            }
        });
        if (userDao == null) {
            userDao = new UserImplementacionBD();
        }
        if (shipmentDao == null) {
            shipmentDao = new ShipmentImplementacionBD();
        }
        loadUsers();
    }

    /**
     * Sets the DAO of the users. The tests use it to pass a fake DAO.
     *
     * @param userDao the DAO to use
     */
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Sets the DAO of the shipments. The tests use it to pass a fake DAO.
     *
     * @param shipmentDao the DAO to use
     */
    public void setShipmentDao(ShipmentDao shipmentDao) {
        this.shipmentDao = shipmentDao;
    }

    /**
     * Creates the columns of userTable: name, email, user type and customer
     * type or role.
     */
    private void configureTable() {
        TableColumn<User, String> name = new TableColumn<>("Name");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<User, String> email = new TableColumn<>("Email");
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        TableColumn<User, String> userType = new TableColumn<>("User type");
        userType.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> cell) {
                return new ReadOnlyStringWrapper(
                        cell.getValue() instanceof Customer ? "Customer" : "Worker");
            }
        });
        TableColumn<User, String> customerType = new TableColumn<>("Type / Role");
        customerType.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> cell) {
                        User user = cell.getValue();
                        if (user instanceof Customer) {
                            CustomerType type = ((Customer) user).getType();
                            return new ReadOnlyStringWrapper(type == null ? "" : type.name());
                        }
                        Role role = ((Worker) user).getRole();
                        return new ReadOnlyStringWrapper(role == null ? "" : role.name());
                    }
                });
        userTable.getColumns().setAll(name, email, userType, customerType);
    }

    /**
     * Fills tblUsers with every user (name, email, customer/worker, role/type).
     */
    private void loadUsers() {
        try {
            userTable.setItems(FXCollections.observableArrayList(userDao.findAll()));
        } catch (DatabaseException e) {
            logger.severe("Unable to load users: " + e.getMessage());
            showError(e.getMessage(), "Users");
        }
    }

    /**
     * Edit user button: loads the selected user in the form.
     */
    @FXML
    private void editUser() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Select a user to edit.", "Users");
            return;
        }
        editingUser = selected;
        tfName.setText(selected.getName());
        tfEmail.setText(selected.getEmail());
        tfIdDocument.setText(selected.getIdDocument());
        pfPasswd.clear();
        tfNewAddress.clear();
        cmbRole.getItems().setAll(Role.values());

        boolean isCustomer = selected instanceof Customer;
        cmbType.setDisable(!isCustomer);
        cmbRole.setDisable(isCustomer);
        lstAddresses.setDisable(!isCustomer);
        tfNewAddress.setDisable(!isCustomer);
        btnAddAddress.setDisable(!isCustomer);
        if (isCustomer) {
            Customer customer = (Customer) selected;
            cmbType.getSelectionModel().select(customer.getType());
            lstAddresses.getItems().setAll(customer.getAddresses() == null
                    ? new ArrayList<>()
                    : customer.getAddresses());
            cmbRole.getSelectionModel().clearSelection();
        } else {
            Worker worker = (Worker) selected;
            cmbRole.getSelectionModel().select(worker.getRole());
            cmbType.getSelectionModel().clearSelection();
            lstAddresses.getItems().clear();
        }
    }

    /**
     * Save changes button: saves the user of the form.
     */
    @FXML
    private void saveUser() {
        if (editingUser == null || editingUser != userTable.getSelectionModel().getSelectedItem()) {
            showError("Select and edit a user before saving.", "Users");
            return;
        }
        if (!validateForm()) {
            return;
        }
        try {
            User existing = userDao.findByEmail(tfEmail.getText().trim());
            if (existing != null && !Objects.equals(existing.getId(), editingUser.getId())) {
                showError("That email address is already in use.", "Invalid user");
                return;
            }
            editingUser.setName(tfName.getText().trim());
            editingUser.setEmail(tfEmail.getText().trim());
            editingUser.setIdDocument(tfIdDocument.getText().trim());
            if (!pfPasswd.getText().isEmpty()) {
                editingUser.setPassword(pfPasswd.getText());
            }
            if (editingUser instanceof Customer) {
                Customer customer = (Customer) editingUser;
                customer.setType(cmbType.getValue());
                customer.setAddresses(new ArrayList<>(lstAddresses.getItems()));
            } else {
                ((Worker) editingUser).setRole(cmbRole.getValue());
            }
            if (!userDao.update(editingUser)) {
                showError("The user no longer exists.", "Users");
                loadUsers();
                return;
            }
            loadUsers();
            clearForm();
            showInfo("User saved successfully.", "Users");
        } catch (DatabaseException e) {
            logger.severe("Unable to save user: " + e.getMessage());
            showError(e.getMessage(), "Users");
        }
    }

    /**
     * Delete user button: deletes the selected user.
     */
    @FXML
    private void deleteUser() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Select a user to delete.", "Users");
            return;
        }
        if (isLoggedInUser(selected)) {
            showError("You cannot delete the currently logged-in user.", "Users");
            return;
        }
        try {
            if (selected instanceof Customer
                    && shipmentDao.existsInProgressByCustomer(selected.getId())) {
                showError("This customer cannot be deleted while they have shipments in progress.", "Users");
                return;
            }
            if (!confirm("Are you sure you want to delete this user?", "Delete user")) {
                return;
            }
            if (!userDao.delete(selected.getId())) {
                showError("The user no longer exists.", "Users");
                loadUsers();
                return;
            }
            loadUsers();
            clearForm();
        } catch (DatabaseException e) {
            logger.severe("Unable to delete user: " + e.getMessage());
            showError(e.getMessage(), "Users");
        }
    }

    /**
     * Checks if a user is the logged-in user, comparing the object and its _id.
     *
     * @param user user to check
     * @return true if it is the logged-in user
     */
    private boolean isLoggedInUser(User user) {
        return loggedUser != null && (loggedUser == user
                || (loggedUser.getId() != null && loggedUser.getId().equals(user.getId())));
    }

    /**
     * Context menu of lstAddresses: removes the selected address.
     */
    @FXML
    private void removeAddress() {
        int selectedIndex = lstAddresses.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            lstAddresses.getItems().remove(selectedIndex);
        }
    }

    /**
     * Adds the address entered in the form to the selected customer's address list.
     */
    @FXML
    private void addAddress() {
        if (!(editingUser instanceof Customer)) {
            showError("Edit a customer before adding an address.", "Addresses");
            return;
        }
        String address = tfNewAddress.getText() == null ? "" : tfNewAddress.getText().trim();
        if (address.isEmpty()) {
            showError("Enter an address to add.", "Addresses");
            return;
        }
        if (lstAddresses.getItems().contains(address)) {
            showError("That address is already in the list.", "Addresses");
            return;
        }
        lstAddresses.getItems().add(address);
        tfNewAddress.clear();
    }

    /**
     * Empties every field of the form.
     */
    private void clearForm() {
        editingUser = null;
        tfName.clear();
        tfEmail.clear();
        tfIdDocument.clear();
        pfPasswd.clear();
        cmbType.getSelectionModel().clearSelection();
        cmbRole.getSelectionModel().clearSelection();
        cmbType.setDisable(false);
        cmbRole.setDisable(false);
        lstAddresses.setDisable(true);
        lstAddresses.getItems().clear();
        tfNewAddress.clear();
        tfNewAddress.setDisable(true);
        btnAddAddress.setDisable(true);
    }
    /**
     * Checks the form: name, email and idDocument filled and a valid email.
     *
     * @return {@code true} if the form is valid
     */
    private boolean validateForm() {
        if (tfName.getText() == null || tfName.getText().trim().isEmpty()) {
            showError("Enter a name.", "Invalid user");
            return false;
        }
        try {
            UserValidation.validateEmail(tfEmail.getText());
            UserValidation.validateIdDocument(tfIdDocument.getText());
        } catch (EmailException | DniException e) {
            showError(e.getMessage(), "Invalid user");
            return false;
        }
        if (editingUser instanceof Customer && cmbType.getValue() == null) {
            showError("Select a customer type.", "Invalid user");
            return false;
        }
        if (editingUser instanceof Worker && cmbRole.getValue() == null) {
            showError("Select a worker role.", "Invalid user");
            return false;
        }
        return true;
    }
}
