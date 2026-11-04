package com.reto1.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AdminOffersController extends BaseController {

    @FXML
    private Button btnUsers;

    private void initialize() {
        btnUsers.setDisable(true); // Disable on start
    }

}
