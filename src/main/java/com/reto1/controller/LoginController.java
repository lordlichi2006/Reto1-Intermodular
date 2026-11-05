package com.reto1.controller;

import java.util.logging.Level;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Controller of the login window (loginView.fxml).
 * The title bar is handled by {@link BaseController}.
 */
public class LoginController extends BaseController {

    @FXML
    private TextField txtEmail;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Label lblError;

    /**
     * Runs when the window opens: clears the error text.
     */
    @FXML
    private void initialize() {
        lblError.setText("");
    }

    /**
     * Enter button. Opens the shop (customer) or the management windows (worker).
     *
     * @param event button event
     */
    @FXML
    private void login(ActionEvent event) {
        String email;
        boolean correctLogin;
        boolean isWorker;

        logger.fine("Login button pressed.");
        lblError.setText("");
        email = txtEmail.getText();

        // TODO: temporary values until the login uses userDao.login(email, password)
        correctLogin = true;
        isWorker = true;

        if (!correctLogin) {
            lblError.setText("Incorrect Password or Username.");
            logger.log(Level.INFO, "Unsuccessful login attempt with email: {0}", email);
        } else {
            logger.log(Level.INFO, "Successful login attempt with email: {0}", email);
            if (isWorker) {
                switchScene("adminShipments", 1280, 720, true);
            } else {
                switchScene("shopHome", 860, 540, true);
            }
        }
    }

    /**
     * Register button.
     *
     * @param event button event
     */
    @FXML
    private void register(ActionEvent event) {
        // TODO register functionality
    }
}
