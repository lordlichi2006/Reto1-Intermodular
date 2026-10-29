package com.reto1.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

/**
 * Controller of the customer shop template (shopTemplate.fxml).
 * The title bar buttons (minimize, maximize, close, drag) are inherited
 * from {@link BaseController}; they must not be redeclared here.
 */
public class ShopTemplateController extends BaseController {

    @FXML
    private BorderPane mainPanel;
    @FXML
    private Button btnHome;
    @FXML
    private Button btnCart;
    @FXML
    private Button btnDelivers;
    @FXML
    private Button btnProfile;
}
