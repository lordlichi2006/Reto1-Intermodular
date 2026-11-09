package com.reto1.controller;

import com.reto1.model.Product;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

/**
 * Controller of the window Main View (shopHome.fxml).
 * Shop catalogue: search products and add them to the cart.
 * The title bar and the menu buttons are handled by {@link BaseController}.
 */
public class ShopHomeController extends BaseController {

    @FXML
    private TableView<Product> tblProducts;

    @FXML
    private TextField txtSearchbox;

    /**
     * Runs when the window opens.
     */
    @FXML
    private void initialize() {
        // TODO: loadProducts()
    }

    /**
     * Fills tblProducts with every product (name, brand, price, stock).
     */
    private void loadProducts() {
        // TODO: productDao.findAll() and show the list in tblProducts
    }

    /**
     * txtSearchbox (onKeyTyped): filters the table by the typed text.
     *
     * @param event key event
     */
    @FXML
    void search(KeyEvent event) {
        // TODO: productDao.findByName(txtSearchbox.getText()) and show the result
    }

    /**
     * Add To Cart button: adds the selected product with the units of txtAmount.
     */
    @FXML
    private void addToCart() {
        // TODO: product selected? units is a number between 1 and the stock?
        //       add a CartItem to the cart of loggedUser (add units if it was
        //       already there) and save it with userDao.update(customer)
    }
}
