package com.reto1.controller;

import java.util.logging.Level;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

/**
 * Controller of the login window (loginView.fxml).
 * The title bar is handled by {@link BaseController}.
 */
public class LoginController extends BaseController {

    @FXML
    private BorderPane mainPanel;
    @FXML
    private TextField txtEmail;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Button btnLogin;
    @FXML
    private Button btnRegister;
    @FXML
    private Label lblError;

    @FXML
    private void initialize() {
        initializeLogger();
        logger.log(Level.FINE, "{0} Initialized", getClass().getName());

        lblError.setText("");
    }

    @FXML
    private void login(ActionEvent event) {
        logger.fine("Login button pressed.");
        lblError.setText("");

        String email = txtEmail.getText();
        String password = txtPassword.getText();

        // TODO Fase 8: replace with AuthService.login(email, password)
        if (!true) {
            lblError.setText("Incorrect Password or Username.");
            logger.log(Level.INFO, "Unsuccessful login attempt with email: {0}", email);
            return;
        }
        logger.log(Level.INFO, "Successful login attempt with email: {0}", email);
    }

    @FXML
    private void register(ActionEvent event) {
        // TODO Fase 9: open the register window
    }
}
