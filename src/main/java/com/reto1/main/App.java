package com.reto1.main;

import java.io.IOException;

import com.reto1.dao.Database;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * JavaFX entry point of GozoShop. Opens the login window.
 */
public class App extends Application {

    /**
     * Opens the login window (view/loginView.fxml).
     *
     * @param stage main window created by JavaFX
     * @throws IOException if the FXML file cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        Parent root;

        root = FXMLLoader.load(App.class.getResource("/com/reto1/view/loginView.fxml"));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(root, 270, 340));
        stage.show();
    }

    /**
     * Closes the MongoDB connection when the application exits.
     */
    @Override
    public void stop() {
        Database.close();
    }

    /**
     * Starts JavaFX. Launcher calls this method.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        launch();
    }
}
