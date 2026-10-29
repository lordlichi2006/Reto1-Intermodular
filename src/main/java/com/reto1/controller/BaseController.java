package com.reto1.controller;

import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Parent class of every window controller.
 * Holds the custom title bar behaviour (drag, minimize, maximize, close)
 * so the child controllers do not repeat it.
 */
public class BaseController {

    /** Custom title bar. Injected by FXML in every child window (fx:id="titleBar"). */
    @FXML
    protected HBox titleBar;

    private double xOffset;
    private double yOffset;

    protected Logger logger;

    protected void initializeLogger() {
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
            logger.setLevel(Level.ALL); // set to all so each handler can choose which logging level you want
            file.setLevel(Level.INFO); // only SEVERE, WARNING and INFO
            console.setLevel(Level.ALL); // ALL includes Debug

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @FXML
    protected void onDragStart(MouseEvent event) {
        Stage stage = (Stage) titleBar.getScene().getWindow();

        xOffset = event.getScreenX() - stage.getX();
        yOffset = event.getScreenY() - stage.getY();
    }

    @FXML
    protected void onDrag(MouseEvent event) {
        Stage stage = (Stage) titleBar.getScene().getWindow();

        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }

    @FXML
    protected void minimizeWindow() {
        Stage stage = (Stage) titleBar.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    protected void closeWindow() {
        Stage stage = (Stage) titleBar.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void maximizeWindow() {
        Stage stage = (Stage) titleBar.getScene().getWindow();
        stage.setMaximized(!stage.isMaximized());
    }
}
