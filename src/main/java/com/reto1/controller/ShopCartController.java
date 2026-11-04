package com.reto1.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ShopCartController extends BaseController {

    @FXML
    private Button btnCart;

    private void initialize() {
        btnCart.setDisable(true); // Disable on start
    }

}
