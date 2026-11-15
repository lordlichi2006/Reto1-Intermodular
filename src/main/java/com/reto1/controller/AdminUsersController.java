package com.reto1.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controller of the window Admin Users (adminUsers.fxml).
 * Only for admins: edit and remove users.
 * The title bar and the menu buttons are handled by {@link BaseController}.
 */
public class AdminUsersController extends BaseController {

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
    }

    /**
     * Fills tblUsers with every user (name, email, customer/worker, role/type).
     */
    private void loadUsers() {
    }

    /**
     * Edit user button: loads the selected user in the form.
     */
    @FXML
    private void editUser() {
    }

    /**
     * Save changes button: saves the user of the form.
     */
    @FXML
    private void saveUser() {
    }

    /**
     * Delete user button: deletes the selected user.
     */
    @FXML
    private void deleteUser() {
    }

    /**
     * Context menu of lstAddresses: removes the selected address.
     */
    @FXML
    private void removeAddress() {
    }

    /**
     * Empties every field of the form.
     */
    private void clearForm() {
    }

    /**
     * Checks the form: name, email and idDocument filled and a valid email.
     *
     * @return {@code true} if the form is valid
     */
    private boolean validateForm() {
        boolean valid;

        valid = false;
        return valid;
    }
}

