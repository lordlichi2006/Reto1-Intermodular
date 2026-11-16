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
     * Data access object used to check the credentials.
     */
    private UserDao userDao;

    /**
     * Sets the data access object used to check the credentials.
     * The tests use it to pass a fake implementation that does not need MongoDB.
     *
     * @param userDao the data access object to use
     */
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

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

        if (userDao == null) {
            userDao = new UserImplementacionBD();
        }
        User user = null;
        boolean serverError = false;

        try {
            user = userDao.login(email, password);
        } catch (DatabaseException e) {
            lblError.setText("Server Error, please try again later.");
            serverError = true;
        }
        if (user == null) {
            if (!serverError) {
                lblError.setText("Incorrect Password or Email.");
                logger.log(Level.INFO, "Unsuccessful login attempt with email: {0}", email);
            }
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
}
