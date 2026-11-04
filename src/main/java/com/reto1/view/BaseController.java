package com.reto1.view;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.reto1.util.LoggerManager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Parent class of every window controller. Holds what all the windows share:
 * changing window, the menu buttons and the custom title bar (drag, minimize,
 * maximize, close), so the child controllers do not repeat it.
 */
public class BaseController {

    /** Logger shared by all the windows (writes to application.log). */
    protected static final Logger logger = LoggerManager.getLogger();

    /** Custom title bar, used to move the window. */
    @FXML
    protected HBox titleBar;
    /** Root panel of every window (fx:id="mainPanel" in all the FXML files). */
    @FXML
    private BorderPane mainPanel;

    /** Distance between the mouse and the window corner while dragging. */
    private double xOffset;
    private double yOffset;

    /**
     * Replaces the current window content with another FXML view.
     *
     * @param fxml        name of the FXML file without extension, e.g. "shopHome"
     * @param resolutionX width of the new scene
     * @param resolutionY height of the new scene
     * @param center      {@code true} to center the window on the screen
     */
    protected void switchScene(String fxml, int resolutionX, int resolutionY, boolean center) {
        Stage stage;
        FXMLLoader fxmlLoader;
        Scene scene;

        try {
            stage = (Stage) mainPanel.getScene().getWindow();
            // The FXML files are in the same package as this class (com/reto1/view)
            fxmlLoader = new FXMLLoader(BaseController.class.getResource(fxml + ".fxml"));
            scene = new Scene(fxmlLoader.load(), resolutionX, resolutionY);
            stage.setScene(scene);
            stage.show();
            if (center) {
                centerStage(stage);
            }
            logger.log(Level.INFO, "User has switched to {0}.fxml view", fxml);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error trying to switch scene: {0}", e.getMessage());
        }
    }

    /**
     * Moves the window to the center of the screen.
     *
     * @param stage window to center
     */
    public void centerStage(Stage stage) {
        Rectangle2D bounds;
        double x;
        double y;

        bounds = Screen.getPrimary().getVisualBounds();
        x = bounds.getMinX() + (bounds.getWidth() - stage.getWidth()) / 2;
        y = bounds.getMinY() + (bounds.getHeight() - stage.getHeight()) / 2;
        stage.setX(x);
        stage.setY(y);
    }

    // ---- Menu buttons of the shop (customer) ----

    @FXML
    private void menuHome() {
        switchScene("shopHome", 860, 540, false);
    }

    @FXML
    private void menuCart() {
        switchScene("shopCart", 860, 540, false);
    }

    @FXML
    private void menuDeliveries() {
        switchScene("shopDeliveries", 860, 540, false);
    }

    @FXML
    private void menuProfile() {
        switchScene("shopProfile", 860, 540, false);
    }

    // ---- Menu buttons of the management windows (worker / admin) ----

    @FXML
    private void adminMenuShipments() {
        switchScene("adminShipments", 1280, 720, false);
    }

    @FXML
    private void adminMenuProducts() {
        switchScene("adminProducts", 1280, 720, false);
    }

    @FXML
    private void adminMenuOffers() {
        switchScene("adminOffers", 1280, 720, false);
    }

    @FXML
    private void adminMenuUsers() {
        switchScene("adminUsers", 1280, 720, false);
    }

    @FXML
    private void adminMenuLog() {
        switchScene("adminLog", 1280, 720, false);
    }

    /**
     * LOGOUT button: goes back to the login window.
     */
    @FXML
    private void logOut() {
        // TODO: also forget the logged user when there is a session
        logger.log(Level.INFO, "User has logged out.");
        switchScene("loginView", 270, 340, true);
    }

    // ---- Title bar: move, minimize, maximize and close ----

    @FXML
    protected void onDragStart(MouseEvent event) {
        Stage stage;

        stage = (Stage) titleBar.getScene().getWindow();
        xOffset = event.getScreenX() - stage.getX();
        yOffset = event.getScreenY() - stage.getY();
    }

    @FXML
    protected void onDrag(MouseEvent event) {
        Stage stage;

        stage = (Stage) titleBar.getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }

    @FXML
    protected void minimizeWindow() {
        Stage stage;

        stage = (Stage) titleBar.getScene().getWindow();
        stage.setIconified(true);
        logger.log(Level.INFO, "User has minimized the window.");
    }

    @FXML
    protected void maximizeWindow() {
        Stage stage;

        stage = (Stage) titleBar.getScene().getWindow();
        stage.setMaximized(!stage.isMaximized());
        logger.log(Level.FINE, "User has maximized the window");
    }

    @FXML
    protected void closeWindow() {
        Stage stage;

        stage = (Stage) titleBar.getScene().getWindow();
        stage.close();
        logger.log(Level.INFO, "User has exited the application.");
    }
}
