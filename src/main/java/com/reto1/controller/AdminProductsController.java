package com.reto1.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AdminProductsController extends BaseController {

    @FXML
    private Button btnShipments;

    private void initialize() {
        btnShipments.setDisable(true); // Disable on start
    }

}
