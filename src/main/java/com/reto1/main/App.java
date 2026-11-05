package com.reto1.main;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mongodb.MongoException;
import com.reto1.dao.Database;
import com.reto1.dao.SchemaInitializer;
import com.reto1.util.FillData;
import com.reto1.util.LoggerManager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * JavaFX entry point of GozoShop. Prepares the database and opens the login
 * window.
 */
public class App extends Application {

    private static final Logger LOGGER = LoggerManager.getLogger();

    /**
     * Prepares the database (collections + initial data) and opens the login
     * window (view/loginView.fxml).
     *
     * @param stage main window created by JavaFX
     * @throws IOException if the FXML file cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        Parent root;

        prepareDatabase();

        root = FXMLLoader.load(App.class.getResource("/com/reto1/view/loginView.fxml"));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(root, 270, 340));
        stage.show();
    }

    /**
     * Creates the collections with their validation and, if the database is
     * empty, inserts the initial data. If MongoDB is not running, the error
     * is logged and the login window still opens.
     */
    private void prepareDatabase() {
        try {
            SchemaInitializer.init(Database.getDatabase());
            FillData.fillIfEmpty();
        } catch (MongoException e) {
            LOGGER.log(Level.SEVERE, "MongoDB is not available: {0}", e.getMessage());
        }
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
