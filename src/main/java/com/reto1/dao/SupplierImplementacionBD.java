package com.reto1.dao;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.reto1.model.Supplier;

/**
 * MongoDB implementation of {@link SupplierDao}.
 */
public class SupplierImplementacionBD implements SupplierDao {

    /** Name of the collection in MongoDB. */
    private static final String COLLECTION = "suppliers";

    /** Typed collection: documents are converted to Supplier objects automatically. */
    private final MongoCollection<Supplier> collection;

    /**
     * Uses the application database (config.properties).
     */
    public SupplierImplementacionBD() {
        this(Database.getDatabase());
    }

    /**
     * Uses the given database. Useful for tests on a separate database.
     *
     * @param db database that contains the "suppliers" collection
     */
    public SupplierImplementacionBD(MongoDatabase db) {
        this.collection = db.getCollection(COLLECTION, Supplier.class);
    }

    @Override
    public List<Supplier> findAll() {
        List<Supplier> suppliers;

        suppliers = collection.find().sort(Sorts.ascending("name")).into(new ArrayList<>());
        return suppliers;
    }

    @Override
    public Supplier findById(ObjectId id) {
        Supplier supplier;

        supplier = collection.find(Filters.eq("_id", id)).first();
        return supplier;
    }

    @Override
    public Supplier findByCif(String cif) {
        Supplier supplier;

        supplier = collection.find(Filters.eq("cif", cif)).first();
        return supplier;
    }

    @Override
    public void insert(Supplier supplier) {
        collection.insertOne(supplier);
    }

    @Override
    public boolean update(Supplier supplier) {
        UpdateResult result;

        result = collection.replaceOne(Filters.eq("_id", supplier.getId()), supplier);
        return result.getMatchedCount() > 0;
    }

    @Override
    public boolean delete(ObjectId id) {
        DeleteResult result;

        result = collection.deleteOne(Filters.eq("_id", id));
        return result.getDeletedCount() > 0;
    }
}
