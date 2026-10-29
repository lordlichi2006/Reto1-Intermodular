package com.reto1.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.Level;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.StageStyle;

public class BaseController {

    // for resizing the window
    @FXML
    private HBox titleBar;
    private double xOffset;
    private double yOffset;

    @FXML
    private BorderPane mainPanel;
    protected static Logger logger;
    protected boolean unsavedChanges = false;

    protected void switchScene(String fxml, int resolutionX, int resolutionY) {

        try {
            Stage stage = (Stage) mainPanel.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
            Scene scene = new Scene(fxmlLoader.load(), resolutionX, resolutionY);
            stage.setScene(scene);
            stage.show();
            centerStage(stage); // after loading new view, centers stage.
            logger.log(Level.INFO, "User has switched to {0}.fxml view", fxml);

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error trying to switch scene: {0}", e.getMessage());
        }

    }

    public void centerStage(Stage stage) {
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        
        // calculate the center of the screen
        double x = bounds.getMinX() + (bounds.getWidth() - stage.getWidth()) / 2;
        double y = bounds.getMinY() + (bounds.getHeight() - stage.getHeight()) / 2;

        stage.setX(x);
        stage.setY(y);
    }

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
            logger.setLevel(Level.ALL); //  set to all so each handler can choose which logging level you want
            file.setLevel(Level.INFO); // only SEVERE, WARNING and INFO
            console.setLevel(Level.ALL); // ALL includes Debug

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    // handle menu buttons
    @FXML
    private void menuHome()      {switchScene("shopHome",       860, 540);}
    @FXML
    private void menuCart()      {switchScene("shopCart",       860, 540);}
    @FXML 
    private void menuDeliveries(){switchScene("shopDeliveries", 860, 540);}
    @FXML
    private void menuProfile()   {switchScene("shopProfile",    860, 540);}

    // Window Controls (moving, minimize,maximize and close)
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
        logger.log(Level.INFO, "User has minimized the window.");

    }
    @FXML
    private void closeWindow() {
        Stage stage = (Stage) titleBar.getScene().getWindow();
        stage.close();
        logger.log(Level.INFO, "User has exited the applicaiton.");

    }
    @FXML
    private void maximizeWindow() {
        Stage stage = (Stage) titleBar.getScene().getWindow();
        stage.setMaximized(!stage.isMaximized());
        logger.log(Level.FINE, "User has maximized the window");

    }
}
