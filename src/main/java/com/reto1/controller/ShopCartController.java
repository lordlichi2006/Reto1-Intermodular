package com.reto1.controller;

import javafx.fxml.FXML;

/**
 * Controller of the window Cart View (shopCart.fxml).
 * Customer cart: change units, remove items and purchase.
 * The title bar and the menu buttons are handled by {@link BaseController}.
 */
public class ShopCartController extends BaseController {

    /**
     * Runs when the window opens.
     */
    @FXML
    private void initialize() {
    }

    /**
     * Fills tblCart with the cart of loggedUser and shows the total.
     */
    private void loadCart() {
    }

    /**
     * Fills cmbAddress with the saved addresses of loggedUser.
     */
    private void loadAddresses() {
    }

    /**
     * Edit amount: changes the units of the selected cart line.
     */
    @FXML
    private void changeAmount() {
    }

    /**
     * Remove button: removes the selected line from the cart.
     */
    @FXML
    private void removeFromCart() {
    }

    /**
     * Purchase button: creates the shipment and empties the cart.
     */
    @FXML
    private void purchase() {
    }
}
