package com.reto1.controllers;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.Level;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;

public class BaseController {

    @FXML
    private HBox titleBar;
    private double xOffset;
    private double yOffset;

    protected Logger logger;

    protected void initializeBase() {
        logger = Logger.getLogger(getClass().getName());

        try {
            FileHandler file = new FileHandler("application.log", true);
            ConsoleHandler console = new ConsoleHandler();
            
            // Formats the log output so its easier to read
            file.setFormatter(new SimpleFormatter()); 
            console.setFormatter(new SimpleFormatter());

            logger.addHandler(file);
            logger.addHandler(console);

            // Remove default handlers since we have our custom ones
            logger.setUseParentHandlers(false); 
            
            // Sets the levels of the logger
            logger.setLevel(Level.ALL); //  set to all so each handler can choose which logging level you want
            file.setLevel(Level.INFO); // only SEVERE, WARNING and INFO
            console.setLevel(Level.ALL); // ALL includes Debug

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @FXML
    private void onDragStart(MouseEvent event) {
        Stage stage = (Stage) titleBar.getScene().getWindow();

        xOffset = event.getScreenX() - stage.getX();
        yOffset = event.getScreenY() - stage.getY();
    }

    @FXML
    private void onDrag(MouseEvent event) {
        Stage stage = (Stage) titleBar.getScene().getWindow();

        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }

    @FXML
    private void minimizeWindow() {
        Stage stage = (Stage) titleBar.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) titleBar.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void maximizeWindow() {
        Stage stage = (Stage) titleBar.getScene().getWindow();
        stage.setMaximized(!stage.isMaximized());
    }
}
