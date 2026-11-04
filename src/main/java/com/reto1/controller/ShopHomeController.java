package com.reto1.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ShopHomeController extends BaseController {

    @FXML
    private Button btnHome;

    private void initialize() {
        btnHome.setDisable(true); // Disable on start
    }

}
