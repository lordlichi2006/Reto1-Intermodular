package com.reto1.controller;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Pattern;

import com.reto1.dao.ShipmentDao;
import com.reto1.dao.ShipmentImplementacionBD;
import com.reto1.dao.UserDao;
import com.reto1.dao.UserImplementacionBD;
import com.reto1.exceptions.DatabaseException;
import com.reto1.model.Customer;
import com.reto1.model.CustomerType;
import com.reto1.model.Role;
import com.reto1.model.User;
import com.reto1.model.Worker;

import javafx.beans.property.ReadOnlyStringWrapper;
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

/**
 * Controller of the window Admin Users (adminUsers.fxml).
 * Only for admins: edit and remove users.
 * The title bar and the menu buttons are handled by {@link BaseController}.
 */
public class AdminUsersController extends BaseController {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    @FXML
    private Button btnUsers;
    @FXML
    private Button btnLog;
    @FXML
    private Button btnDelete;
    @FXML
    private TableView<User> userTable;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfIdDocument;
    @FXML
    private PasswordField pfPasswd;
    @FXML
    private ComboBox<CustomerType> cmbType;
    @FXML
    private ComboBox<Role> cmbRole;
    @FXML
    private ListView<String> lstAddresses;

    private UserDao userDao;
    private ShipmentDao shipmentDao;
    private User editingUser;

    @FXML
    private void initialize() {
        applyAdminPermissions(btnUsers, btnLog);
        cmbType.getItems().setAll(CustomerType.values());
        cmbRole.getItems().setAll(Role.values());
        configureTable();
        btnDelete.setDisable(true);
        userTable.getSelectionModel().selectedItemProperty().addListener((observable, oldUser, user) -> {
            btnDelete.setDisable(user == null || isLoggedInUser(user));
            if (user == null) {
                clearForm();
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

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setShipmentDao(ShipmentDao shipmentDao) {
        this.shipmentDao = shipmentDao;
    }

    private void configureTable() {
        TableColumn<User, String> name = new TableColumn<>("Name");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<User, String> email = new TableColumn<>("Email");
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        TableColumn<User, String> userType = new TableColumn<>("User type");
        userType.setCellValueFactory(cell -> new ReadOnlyStringWrapper(
                cell.getValue() instanceof Customer ? "Customer" : "Worker"));
        TableColumn<User, String> customerType = new TableColumn<>("Type / Role");
        customerType.setCellValueFactory(cell -> {
            User user = cell.getValue();
            if (user instanceof Customer) {
                CustomerType type = ((Customer) user).getType();
                return new ReadOnlyStringWrapper(type == null ? "" : type.name());
            }
            Role role = ((Worker) user).getRole();
            return new ReadOnlyStringWrapper(role == null ? "" : role.name());
        });
        userTable.getColumns().setAll(name, email, userType, customerType);
    }

    private void loadUsers() {
        try {
            userTable.setItems(FXCollections.observableArrayList(userDao.findAll()));
        } catch (DatabaseException e) {
            logger.severe("Unable to load users: " + e.getMessage());
            showError(e.getMessage(), "Users");
        }
    }

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

        boolean isCustomer = selected instanceof Customer;
        cmbType.setDisable(!isCustomer);
        cmbRole.setDisable(isCustomer);
        lstAddresses.setDisable(!isCustomer);
        if (isCustomer) {
            Customer customer = (Customer) selected;
            cmbType.getSelectionModel().select(customer.getType());
            lstAddresses.getItems().setAll(customer.getAddresses() == null
                    ? new ArrayList<>() : customer.getAddresses());
            cmbRole.getSelectionModel().clearSelection();
        } else {
            Worker worker = (Worker) selected;
            cmbRole.getSelectionModel().select(worker.getRole());
            cmbType.getSelectionModel().clearSelection();
            lstAddresses.getItems().clear();
        }
    }

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

    private boolean isLoggedInUser(User user) {
        return loggedUser != null && (loggedUser == user
                || (loggedUser.getId() != null && loggedUser.getId().equals(user.getId())));
    }

    @FXML
    private void removeAddress() {
        int selectedIndex = lstAddresses.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            lstAddresses.getItems().remove(selectedIndex);
        }
    }

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
        lstAddresses.setDisable(false);
        lstAddresses.getItems().clear();
    }

    private boolean validateForm() {
        if (tfName.getText() == null || tfName.getText().trim().isEmpty()
                || tfEmail.getText() == null || !EMAIL_PATTERN.matcher(tfEmail.getText().trim()).matches()
                || tfIdDocument.getText() == null || tfIdDocument.getText().trim().isEmpty()) {
            showError("Enter a name, a valid email address and an ID document.", "Invalid user");
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
