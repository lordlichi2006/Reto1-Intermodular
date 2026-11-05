package com.reto1.dao;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.concurrent.TimeUnit;

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

/**
 * Single access point to MongoDB. There is only one connection (MongoClient)
 * for the whole application; it is closed in App.stop().
 * <p>
 * The POJO codec converts the model classes (com.reto1.model) to MongoDB
 * documents and back automatically.
 */
public final class Database {

    /** Address of the MongoDB server. */
    private static final String URI = "mongodb://localhost:27017";
    /** Name of the application database. */
    private static final String DB_NAME = "reto1";

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
     * Only used by tests, to work on a separate database.
     *
     * @param name database name
     * @return the database
     */
    static synchronized MongoDatabase getDatabase(String name) {
        PojoCodecProvider pojoProvider;
        CodecRegistry registry;

        if (client == null) {
            // User, Customer and Worker are registered explicitly so the codec can
            // read the "_t" discriminator and build the right subclass.
            pojoProvider = PojoCodecProvider.builder()
                    .register(User.class, Customer.class, Worker.class)
                    .automatic(true)
                    .build();

            registry = fromRegistries(
                    MongoClientSettings.getDefaultCodecRegistry(),
                    fromProviders(pojoProvider));

            client = MongoClients.create(MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(URI))
                    .codecRegistry(registry)
                    // If MongoDB is not running, fail after 5 s instead of 30 s
                    .applyToClusterSettings(cluster -> cluster.serverSelectionTimeout(5, TimeUnit.SECONDS))
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
