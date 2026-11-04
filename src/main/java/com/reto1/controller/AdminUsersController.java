package com.reto1.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AdminUsersController extends BaseController {

    @FXML
    private Button btnProducts;

    private void initialize() {
        btnProducts.setDisable(true); // Disable on start
    }

}
