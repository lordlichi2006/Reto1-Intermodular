package com.reto1.controller;

import javafx.fxml.FXML;

/**
 * Controller of the window Profile View (shopProfile.fxml).
 * Customer data and saved addresses; log out.
 * The title bar and the menu buttons are handled by {@link BaseController}.
 */
public class ShopProfileController extends BaseController {

    /**
     * Runs when the window opens.
     */
    @FXML
    private void initialize() {
        // TODO: loadProfile()
    }

    /**
     * Shows the data of loggedUser in txtName, txtEmail, cmbType and lstAddresses.
     */
    private void loadProfile() {
        // TODO
    }

    /**
     * Context menu of lstAddresses: adds the typed address to the list.
     */
    @FXML
    private void addAddress() {
        // TODO: text not empty? add it to the list (saved with saveChanges)
    }

    /**
     * Context menu of lstAddresses: removes the selected address.
     */
    @FXML
    private void removeAddress() {
        // TODO: address selected? remove it from the list (saved with saveChanges)
    }

    /**
     * Save changes button: saves name, email, type and addresses.
     */
    @FXML
    private void saveChanges() {
        // TODO: name and email filled? valid email? email not used by another
        //       user? userDao.update(customer), update loggedUser, showInfo(...)
    }
}
