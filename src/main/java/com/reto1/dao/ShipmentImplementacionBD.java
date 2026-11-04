package com.reto1.dao;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import com.reto1.model.Shipment;
import com.reto1.model.ShipmentStatus;

/**
 * MongoDB implementation of {@link ShipmentDao}.
 */
public class ShipmentImplementacionBD implements ShipmentDao {

    /** Name of the collection in MongoDB. */
    private static final String COLLECTION = "shipments";

    /** Typed collection: documents are converted to Shipment objects automatically. */
    private final MongoCollection<Shipment> collection;

    /**
     * Uses the application database (config.properties).
     */
    public ShipmentImplementacionBD() {
        this(Database.getDatabase());
    }

    /**
     * Uses the given database. Useful for tests on a separate database.
     *
     * @param db database that contains the "shipments" collection
     */
    public ShipmentImplementacionBD(MongoDatabase db) {
        this.collection = db.getCollection(COLLECTION, Shipment.class);
    }

    @Override
    public void insert(Shipment shipment) {
        collection.insertOne(shipment);
    }

    @Override
    public Shipment findById(ObjectId id) {
        Shipment shipment;

        shipment = collection.find(Filters.eq("_id", id)).first();
        return shipment;
    }

    @Override
    public List<Shipment> findAll() {
        List<Shipment> shipments;

        shipments = collection.find().sort(Sorts.descending("createdAt")).into(new ArrayList<>());
        return shipments;
    }

    @Override
    public List<Shipment> findByCustomer(ObjectId customerId) {
        List<Shipment> shipments;

        // Uses the index on "customerId" created by SchemaInitializer
        shipments = collection.find(Filters.eq("customerId", customerId))
                .sort(Sorts.descending("createdAt"))
                .into(new ArrayList<>());
        return shipments;
    }

    @Override
    public boolean updateStatus(ObjectId id, ShipmentStatus status) {
        UpdateResult result;

        // The enum is stored as its name ("IN_SHIPMENT"...), as the schema expects
        result = collection.updateOne(Filters.eq("_id", id), Updates.set("status", status.name()));
        return result.getMatchedCount() > 0;
    }
}
