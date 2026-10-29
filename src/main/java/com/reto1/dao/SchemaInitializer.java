package com.reto1.dao;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.ValidationAction;
import com.mongodb.client.model.ValidationLevel;
import com.mongodb.client.model.ValidationOptions;

/**
 * Creates the 5 collections of the application with their $jsonSchema
 * validation and indexes. The schemas are read from resources/schemas/*.json.
 * <p>
 * It can be run as many times as needed: if a collection already exists,
 * its validator is updated (collMod) instead of creating it again.
 */
public final class SchemaInitializer {

    /** Collection names; each one has a schemas/&lt;name&gt;.json file. */
    public static final String[] COLLECTIONS = {"users", "products", "suppliers", "discountPacks", "shipments"};

    private SchemaInitializer() {
    }

    /**
     * Creates or updates every collection and its indexes.
     *
     * @param db database where the collections are created
     */
    public static void init(MongoDatabase db) {
        List<String> existing = db.listCollectionNames().into(new ArrayList<>());

        for (String name : COLLECTIONS) {
            Document schema = loadSchema(name);

            if (existing.contains(name)) {
                // The collection exists: replace its validator with the current schema
                db.runCommand(new Document("collMod", name)
                        .append("validator", new Document("$jsonSchema", schema))
                        .append("validationLevel", "strict")
                        .append("validationAction", "error"));
            } else {
                ValidationOptions validation = new ValidationOptions()
                        .validator(new Document("$jsonSchema", schema))
                        .validationLevel(ValidationLevel.STRICT)
                        .validationAction(ValidationAction.ERROR);
                db.createCollection(name, new CreateCollectionOptions().validationOptions(validation));
            }
        }
        createIndexes(db);
    }

    /**
     * Unique indexes avoid duplicated users and suppliers;
     * the customerId index speeds up "my shipments".
     */
    private static void createIndexes(MongoDatabase db) {
        db.getCollection("users").createIndex(Indexes.ascending("email"), new IndexOptions().unique(true));
        db.getCollection("suppliers").createIndex(Indexes.ascending("cif"), new IndexOptions().unique(true));
        db.getCollection("shipments").createIndex(Indexes.ascending("customerId"));
    }

    private static Document loadSchema(String collection) {
        String path = "/schemas/" + collection + ".json";
        try (InputStream in = SchemaInitializer.class.getResourceAsStream(path)) {
            if (in == null) {
                throw new IllegalStateException("Schema file not found: " + path);
            }
            return Document.parse(new String(in.readAllBytes(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new IllegalStateException("Schema file could not be read: " + path, e);
        }
    }

    /**
     * Creates the collections in the application database (config.properties).
     *
     * @param args not used
     */
    public static void main(String[] args) {
        try {
            init(Database.getDatabase());
            System.out.println("Collections ready: " + String.join(", ", COLLECTIONS));
        } finally {
            Database.close();
        }
    }
}
