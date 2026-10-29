package com.reto1.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class ShopDeliveriesController extends BaseController {

    @FXML
    private Button btnCart;

    private void initialize() {
        btnCart.setDisable(true); // Disable on start
    }

}
