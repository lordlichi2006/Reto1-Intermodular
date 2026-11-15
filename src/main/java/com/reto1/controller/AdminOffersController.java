package com.reto1.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controller of the window Admin Offers (adminOffers.fxml).
 * Add, edit and remove discount packs.
 * The title bar and the menu buttons are handled by {@link BaseController}.
 */
public class AdminOffersController extends BaseController {

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
     * Fills tblOffers with every pack.
     */
    private void loadOffers() {
    }

    /**
     * Click on a pack: selects its products in tblOfferProducts.
     */
    @FXML
    private void selectOffer() {
    }

    /**
     * Add offer button: clears the form and changes to "new" mode.
     */
    @FXML
    private void addOffer() {
    }

    /**
     * Edit offer button: loads the selected pack in the form.
     */
    @FXML
    private void editOffer() {
    }

    /**
     * Remove offer button: deletes the selected pack.
     */
    @FXML
    private void removeOffer() {
    }

    /**
     * Confirm button: inserts or updates the pack of the form.
     */
    @FXML
    private void saveOffer() {
    }

    /**
     * Empties every field of the form and the product selection.
     */
    private void clearForm() {
    }

    /**
     * Checks the form: name filled, discount between 0 and 100, units a whole
     * number >= 0, at least one product selected.
     *
     * @return {@code true} if the form is valid
     */
    private boolean validateForm() {
        boolean valid;

        valid = false;
        return valid;
    }
}
