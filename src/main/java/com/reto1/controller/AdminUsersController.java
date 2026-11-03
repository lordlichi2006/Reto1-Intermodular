package com.reto1.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class AdminUsersController extends BaseController {

    @FXML
    private Button btnProducts;

    private void initialize() {
        btnProducts.setDisable(true); // Disable on start
    }

}
