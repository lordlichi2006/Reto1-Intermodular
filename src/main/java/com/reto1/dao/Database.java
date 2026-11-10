package com.reto1.dao;

import java.util.ResourceBundle;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 * Opens the connection to MongoDB. The address and the database name are read
 * from config.properties (like configClase.properties with MySQL).
 * <p>
 * There is only one connection for the whole program: it is opened the first
 * time {@link #getDatabase()} is called and closed in App.stop().
 */
public final class Database {

    /** Connection to the MongoDB server ({@code null} until it is opened). */
    private static MongoClient client;
    /** Database of the application ({@code null} until it is opened). */
    private static MongoDatabase database;

    private Database() {
    }

    /**
     * Returns the database of the application, opening the connection the
     * first time.
     *
     * @return the database "reto1"
     */
    public static MongoDatabase getDatabase() {
        ResourceBundle config;

        if (database == null) {
            config = ResourceBundle.getBundle("config");
            client = MongoClients.create(config.getString("uri"));
            database = client.getDatabase(config.getString("database"));
        }
        return database;
    }

    /**
     * Closes the connection. Called from App.stop() when the program exits.
     */
    public static void close() {
        if (client != null) {
            client.close();
            client = null;
            database = null;
        }
    }
}
