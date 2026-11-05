package com.reto1.controller;

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
    private TableView<?> tblProducts;
 
    @FXML
    private TextField txtSearchbox;

    @FXML
    void search(KeyEvent event) {

    }
}
