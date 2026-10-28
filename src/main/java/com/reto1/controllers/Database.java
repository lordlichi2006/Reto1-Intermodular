package com.reto1.controllers;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 * Single access point to the local MongoDB deployment.
 * Maps the classes of com.reto1.models to documents automatically (POJO codec).
 */
public class Database {

    private static final String URI = "mongodb://localhost:27017";
    private static final String DB_NAME = "reto1";

    private static MongoClient client;

    private Database() {
    }

    public static synchronized MongoDatabase getDatabase() {
        if (client == null) {
            CodecRegistry registry = fromRegistries(
                    MongoClientSettings.getDefaultCodecRegistry(),
                    fromProviders(PojoCodecProvider.builder().automatic(true).build()));

            client = MongoClients.create(MongoClientSettings.builder()
                    .applyConnectionString(new com.mongodb.ConnectionString(URI))
                    .codecRegistry(registry)
                    .build());
        }
        return client.getDatabase(DB_NAME);
    }

    public static synchronized void close() {
        if (client != null) {
            client.close();
            client = null;
        }
    }
}
