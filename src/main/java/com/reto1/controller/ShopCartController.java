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
        // TODO: loadCart() and loadAddresses()
    }

    /**
     * Fills tblCart with the cart of loggedUser and shows the total.
     */
    private void loadCart() {
        // TODO: if the cart is empty, disable the Purchase button
    }

    /**
     * Fills cmbAddress with the saved addresses of loggedUser.
     */
    private void loadAddresses() {
        // TODO: if there are no addresses, tell the user to add one in Profile
    }

    /**
     * Edit amount: changes the units of the selected cart line.
     */
    @FXML
    private void changeAmount() {
        // TODO: line selected? units between 1 and the stock?
        //       save with userDao.update(customer) and recalculate the total
    }

    /**
     * Remove button: removes the selected line from the cart.
     */
    @FXML
    private void removeFromCart() {
        // TODO: line selected? confirm(...)? remove it, userDao.update(customer)
    }

    /**
     * Purchase button: creates the shipment and empties the cart.
     */
    @FXML
    private void purchase() {
        // TODO: cart not empty? address chosen?
        //       for each line: productDao.decreaseStock(productId, units)
        //       shipmentDao.insert(new Shipment(...)), add its _id to the
        //       customer, empty the cart, userDao.update(customer)
        //       and open shopDeliveries
    }
}
