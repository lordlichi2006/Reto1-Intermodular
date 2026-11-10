package com.reto1.dao;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.reto1.exceptions.DatabaseException;
import com.reto1.model.DiscountPack;
import com.reto1.util.LoggerManager;

/**
 * MongoDB implementation of {@link DiscountPackDao}.
 */
public class DiscountPackImplementacionBD implements DiscountPackDao {

    /** Name of the collection in MongoDB. */
    private static final String COLLECTION = "discountPacks";

    private static final Logger LOGGER = LoggerManager.getLogger();

    /** The "discountPacks" collection. Each document is like a row of a table. */
    private final MongoCollection<Document> collection;

    /**
     * Gets the "discountPacks" collection of the application database.
     */
    public DiscountPackImplementacionBD() {
        collection = Database.getDatabase().getCollection(COLLECTION);
    }

    @Override
    public ArrayList<DiscountPack> findAll() throws DatabaseException {
        ArrayList<DiscountPack> packs;

        packs = new ArrayList<>();
        try {
            // sort { name: 1 }  ->  ORDER BY name ASC
            for (Document doc : collection.find().sort(new Document("name", 1))) {
                packs.add(toPack(doc));
            }
            LOGGER.info("SELECT discountPacks: " + packs.size() + " found");
        } catch (MongoException e) {
            LOGGER.severe("Error in SELECT discountPacks: " + e.getMessage());
            throw new DatabaseException("Unable to load the offers.");
        }
        return packs;
    }

    @Override
    public DiscountPack findById(ObjectId id) throws DatabaseException {
        Document doc;
        DiscountPack pack;

        pack = null;
        try {
            doc = collection.find(new Document("_id", id)).first();
            if (doc != null) {
                pack = toPack(doc);
            }
            LOGGER.info("SELECT discountPacks by id: " + id);
        } catch (MongoException e) {
            LOGGER.severe("Error in SELECT discountPacks by id: " + e.getMessage());
            throw new DatabaseException("Unable to load the offer.");
        }
        return pack;
    }

    @Override
    public void insert(DiscountPack pack) throws DatabaseException {
        Document doc;

        doc = toDocument(pack);
        try {
            collection.insertOne(doc);
            // MongoDB has added the generated _id to the document
            pack.setId(doc.getObjectId("_id"));
            LOGGER.info("INSERT discountPacks: " + pack.getName());
        } catch (MongoException e) {
            LOGGER.severe("Error in INSERT discountPacks: " + e.getMessage());
            throw new DatabaseException("Unable to save the offer.");
        }
    }

    @Override
    public boolean update(DiscountPack pack) throws DatabaseException {
        UpdateResult result;
        boolean updated;

        try {
            result = collection.replaceOne(new Document("_id", pack.getId()), toDocument(pack));
            updated = result.getMatchedCount() > 0;
            LOGGER.info("UPDATE discountPacks: " + pack.getName());
        } catch (MongoException e) {
            LOGGER.severe("Error in UPDATE discountPacks: " + e.getMessage());
            throw new DatabaseException("Unable to save the changes of the offer.");
        }
        return updated;
    }

    @Override
    public boolean delete(ObjectId id) throws DatabaseException {
        DeleteResult result;
        boolean deleted;

        try {
            result = collection.deleteOne(new Document("_id", id));
            deleted = result.getDeletedCount() > 0;
            LOGGER.info("DELETE discountPacks: " + id);
        } catch (MongoException e) {
            LOGGER.severe("Error in DELETE discountPacks: " + e.getMessage());
            throw new DatabaseException("Unable to delete the offer.");
        }
        return deleted;
    }

    @Override
    public boolean existsByProduct(ObjectId productId) throws DatabaseException {
        boolean exists;

        // TODO: exists = collection.find(new Document("productIds", productId)).first() != null
        //       (searching a value in an array field finds the packs whose array contains it)
        //       (try/catch MongoException + log, like the other methods)
        exists = false;
        return exists;
    }

    /**
     * Converts a document of MongoDB into a DiscountPack (like reading a row
     * of a ResultSet).
     *
     * @param doc document read from the collection
     * @return the pack
     */
    private DiscountPack toPack(Document doc) {
        DiscountPack pack;

        pack = new DiscountPack();
        pack.setId(doc.getObjectId("_id"));
        pack.setName(doc.getString("name"));
        pack.setDiscountPercentage(doc.getDouble("discountPercentage"));
        pack.setProductIds(doc.getList("productIds", ObjectId.class));
        pack.setRemainingUnits(doc.getInteger("remainingUnits"));
        return pack;
    }

    /**
     * Converts a DiscountPack into a document to save it in MongoDB.
     *
     * @param pack pack to convert
     * @return the document with every field of the pack
     */
    private Document toDocument(DiscountPack pack) {
        Document doc;

        doc = new Document();
        // A new pack has no _id yet: MongoDB generates it when inserting
        if (pack.getId() != null) {
            doc.append("_id", pack.getId());
        }
        doc.append("name", pack.getName());
        doc.append("discountPercentage", pack.getDiscountPercentage());
        doc.append("productIds", pack.getProductIds());
        doc.append("remainingUnits", pack.getRemainingUnits());
        return doc;
    }
}
