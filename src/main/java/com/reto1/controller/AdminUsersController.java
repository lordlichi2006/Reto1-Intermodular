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
        // TODO: applyAdminPermissions(btnUsers, btnLog) and loadUsers()
    }

    /**
     * Fills tblUsers with every user (name, email, customer/worker, role/type).
     */
    private void loadUsers() {
        // TODO: userDao.findAll()
    }

    /**
     * Edit user button: loads the selected user in the form.
     */
    @FXML
    private void editUser() {
        // TODO: user selected? enable cmbRole if it is a Worker, or cmbType and
        //       lstAddresses if it is a Customer
    }

    /**
     * Save changes button: saves the user of the form.
     */
    @FXML
    private void saveUser() {
        // TODO: if validateForm(): email not used by another user?
        //       userDao.update(user), loadUsers() and showInfo(...)
    }

    /**
     * Delete user button: deletes the selected user.
     */
    @FXML
    private void deleteUser() {
        // TODO: user selected? not loggedUser? if it is a customer,
        //       shipmentDao.existsInProgressByCustomer(id) is false?
        //       confirm(...)? userDao.delete(id) and loadUsers()
    }

    /**
     * Context menu of lstAddresses: removes the selected address.
     */
    @FXML
    private void removeAddress() {
        // TODO: address selected? remove it from the list (saved with saveUser)
    }

    /**
     * Empties every field of the form.
     */
    private void clearForm() {
        // TODO
    }

    /**
     * Checks the form: name, email and idDocument filled and a valid email.
     *
     * @return {@code true} if the form is valid
     */
    private boolean validateForm() {
        boolean valid;

        // TODO
        valid = false;
        return valid;
    }
}

