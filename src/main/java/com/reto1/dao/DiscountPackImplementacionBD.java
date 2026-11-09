package com.reto1.dao;

import java.util.ArrayList;

import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.reto1.model.DiscountPack;

/**
 * MongoDB implementation of {@link DiscountPackDao}.
 */
public class DiscountPackImplementacionBD implements DiscountPackDao {

    /** Name of the collection in MongoDB. */
    private static final String COLLECTION = "discountPacks";

    /** Typed collection: documents are converted to DiscountPack objects automatically. */
    private final MongoCollection<DiscountPack> collection;

    /**
     * Uses the application database (config.properties).
     */
    public DiscountPackImplementacionBD() {
        this(Database.getDatabase());
    }

    /**
     * Uses the given database. Useful for tests on a separate database.
     *
     * @param db database that contains the "discountPacks" collection
     */
    public DiscountPackImplementacionBD(MongoDatabase db) {
        this.collection = db.getCollection(COLLECTION, DiscountPack.class);
    }

    @Override
    public ArrayList<DiscountPack> findAll() {
        ArrayList<DiscountPack> packs;

        packs = collection.find().sort(Sorts.ascending("name")).into(new ArrayList<>());
        return packs;
    }

    @Override
    public DiscountPack findById(ObjectId id) {
        DiscountPack pack;

        pack = collection.find(Filters.eq("_id", id)).first();
        return pack;
    }

    @Override
    public void insert(DiscountPack pack) {
        collection.insertOne(pack);
    }

    @Override
    public boolean update(DiscountPack pack) {
        UpdateResult result;

        result = collection.replaceOne(Filters.eq("_id", pack.getId()), pack);
        return result.getMatchedCount() > 0;
    }

    @Override
    public boolean delete(ObjectId id) {
        DeleteResult result;

        result = collection.deleteOne(Filters.eq("_id", id));
        return result.getDeletedCount() > 0;
    }

    @Override
    public boolean existsByProduct(ObjectId productId) {
        boolean exists;

        // TODO: countDocuments(Filters.eq("productIds", productId))
        //       (eq on an array field finds the packs whose array contains it)
        //       exists = count > 0
        exists = false;
        return exists;
    }
}
