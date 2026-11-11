package com.reto1.controller;

import java.util.logging.Level;

import com.reto1.dao.UserImplementacionBD;
import com.reto1.exceptions.DatabaseException;
import com.reto1.model.User;
import com.reto1.model.Worker;
import com.reto1.util.LoggerManager;
import com.reto1.dao.UserDao;

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
        String password;

        logger.fine("Login button pressed.");
        lblError.setText("");

        email = txtEmail.getText();
        password = txtPassword.getText();

        UserDao userDao = new UserImplementacionBD();
        User user = null;

        try {
            user = userDao.login(email, password);
        } catch (DatabaseException e) {
            lblError.setText("Server Error, please try again later.");
        }
        if (user == null) {
            lblError.setText("Incorrect Password or Email.");
            logger.log(Level.INFO, "Unsuccessful login attempt with email: {0}", email);
            return;
        }

        loggedUser = user;
        LoggerManager.setCurrentUser(email);
        logger.log(Level.INFO, "Successful login attempt with email: {0}", email);

        if (user instanceof Worker) {
            switchScene("adminShipments", 1280, 720, true);
        } else {
            switchScene("shopHome", 860, 540, true);
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
