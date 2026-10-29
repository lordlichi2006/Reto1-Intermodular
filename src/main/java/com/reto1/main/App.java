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

    /** Folder (inside resources) where all the FXML views are stored. */
    private static final String VIEWS_PATH = "/com/reto1/controller/";

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("loginView"), 270, 340);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
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
     * Replaces the content of the current window with another view.
     *
     * @param fxml name of the FXML file without extension, e.g. "shopTemplate"
     * @throws IOException if the FXML file cannot be loaded
     */
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(VIEWS_PATH + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
