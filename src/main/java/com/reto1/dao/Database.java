package com.reto1.dao;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.reto1.model.Customer;
import com.reto1.model.User;
import com.reto1.model.Worker;
import com.reto1.util.AppConfig;

/**
 * Single access point to the MongoDB deployment.
 * Maps the classes of com.reto1.models to documents automatically (POJO codec).
 * Connection data is read from config.properties.
 */
public class Database {

    private static final String URI = AppConfig.get("mongo.uri", "mongodb://localhost:27017");
    private static final String DB_NAME = AppConfig.get("mongo.database", "reto1");

    private static MongoClient client;

    private Database() {
    }

    /**
     * @return the application database, opening the connection the first time
     */
    public static MongoDatabase getDatabase() {
        return getDatabase(DB_NAME);
    }

    /**
     * Returns a database by name using the shared connection.
     * Used by tests to work on a separate database.
     *
     * @param name database name
     * @return the database
     */
    static synchronized MongoDatabase getDatabase(String name) {
        if (client == null) {
            // User, Customer and Worker are registered explicitly so the codec can
            // read the "_t" discriminator and build the right subclass.
            PojoCodecProvider pojoProvider = PojoCodecProvider.builder()
                    .register(User.class, Customer.class, Worker.class)
                    .automatic(true)
                    .build();

            CodecRegistry registry = fromRegistries(
                    MongoClientSettings.getDefaultCodecRegistry(),
                    fromProviders(pojoProvider));

            client = MongoClients.create(MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(URI))
                    .codecRegistry(registry)
                    .build());
        }
        return client.getDatabase(name);
    }

    /**
     * Closes the connection. Called from App.stop() when the application exits.
     */
    public static synchronized void close() {
        if (client != null) {
            client.close();
            client = null;
        }
    }
}
