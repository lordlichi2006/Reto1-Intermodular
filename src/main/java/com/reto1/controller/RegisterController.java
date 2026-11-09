package com.reto1.controller;

import javafx.fxml.FXML;

/**
 * Controller of the window Register (registerView.fxml, not created yet).
 * Sign up of a new customer. When it finishes, it goes back to the login.
 * The title bar is handled by {@link BaseController}.
 */
public class RegisterController extends BaseController {

    /**
     * Runs when the window opens.
     */
    @FXML
    private void initialize() {
        // TODO: clear the error label
    }

    /**
     * Register button: creates the customer.
     */
    @FXML
    private void register() {
        // TODO: if validateForm(): userDao.existsByEmail(email)? if not,
        //       userDao.insert(new Customer(...)), showInfo(...) and backToLogin()
    }

    /**
     * Back button: returns to the login window without saving.
     */
    @FXML
    private void backToLogin() {
        // TODO: switchScene("loginView", 270, 340, true)
    }

    /**
     * Checks the fields: all filled, valid email, password of 6 or more
     * characters, DNI/NIE with a valid format.
     *
     * @return {@code true} if every field is valid
     */
    private boolean validateForm() {
        boolean valid;

        // TODO
        valid = false;
        return valid;
    }
}
