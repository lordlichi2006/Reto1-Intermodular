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
import com.reto1.model.Supplier;
import com.reto1.util.LoggerManager;

/**
 * MongoDB implementation of {@link SupplierDao}.
 */
public class SupplierImplementacionBD implements SupplierDao {

    /** Name of the collection in MongoDB. */
    private static final String COLLECTION = "suppliers";

    private static final Logger LOGGER = LoggerManager.getLogger();

    /** The "suppliers" collection. Each document is like a row of a table. */
    private final MongoCollection<Document> collection;

    /**
     * Gets the "suppliers" collection of the application database.
     */
    public SupplierImplementacionBD() {
        collection = Database.getDatabase().getCollection(COLLECTION);
    }

    @Override
    public ArrayList<Supplier> findAll() throws DatabaseException {
        ArrayList<Supplier> suppliers;

        suppliers = new ArrayList<>();
        try {
            // sort { name: 1 }  ->  ORDER BY name ASC
            for (Document doc : collection.find().sort(new Document("name", 1))) {
                suppliers.add(toSupplier(doc));
            }
            LOGGER.info("SELECT suppliers: " + suppliers.size() + " found");
        } catch (MongoException e) {
            LOGGER.severe("Error in SELECT suppliers: " + e.getMessage());
            throw new DatabaseException("Unable to load the suppliers.");
        }
        return suppliers;
    }

    @Override
    public Supplier findById(ObjectId id) throws DatabaseException {
        Document doc;
        Supplier supplier;

        supplier = null;
        try {
            doc = collection.find(new Document("_id", id)).first();
            if (doc != null) {
                supplier = toSupplier(doc);
            }
            LOGGER.info("SELECT suppliers by id: " + id);
        } catch (MongoException e) {
            LOGGER.severe("Error in SELECT suppliers by id: " + e.getMessage());
            throw new DatabaseException("Unable to load the supplier.");
        }
        return supplier;
    }

    @Override
    public Supplier findByCif(String cif) throws DatabaseException {
        Document doc;
        Supplier supplier;

        supplier = null;
        try {
            doc = collection.find(new Document("cif", cif)).first();
            if (doc != null) {
                supplier = toSupplier(doc);
            }
            LOGGER.info("SELECT suppliers by cif: " + cif);
        } catch (MongoException e) {
            LOGGER.severe("Error in SELECT suppliers by cif: " + e.getMessage());
            throw new DatabaseException("Unable to load the supplier.");
        }
        return supplier;
    }

    @Override
    public void insert(Supplier supplier) throws DatabaseException {
        Document doc;

        doc = toDocument(supplier);
        try {
            collection.insertOne(doc);
            // MongoDB has added the generated _id to the document
            supplier.setId(doc.getObjectId("_id"));
            LOGGER.info("INSERT suppliers: " + supplier.getCif());
        } catch (MongoException e) {
            LOGGER.severe("Error in INSERT suppliers: " + e.getMessage());
            throw new DatabaseException("Unable to save the supplier. Is the CIF already used?");
        }
    }

    @Override
    public boolean update(Supplier supplier) throws DatabaseException {
        UpdateResult result;
        boolean updated;

        try {
            result = collection.replaceOne(new Document("_id", supplier.getId()), toDocument(supplier));
            updated = result.getMatchedCount() > 0;
            LOGGER.info("UPDATE suppliers: " + supplier.getCif());
        } catch (MongoException e) {
            LOGGER.severe("Error in UPDATE suppliers: " + e.getMessage());
            throw new DatabaseException("Unable to save the changes of the supplier.");
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
            LOGGER.info("DELETE suppliers: " + id);
        } catch (MongoException e) {
            LOGGER.severe("Error in DELETE suppliers: " + e.getMessage());
            throw new DatabaseException("Unable to delete the supplier.");
        }
        return deleted;
    }

    /**
     * Converts a document of MongoDB into a Supplier (like reading a row of a
     * ResultSet).
     *
     * @param doc document read from the collection
     * @return the supplier
     */
    private Supplier toSupplier(Document doc) {
        Supplier supplier;

        supplier = new Supplier();
        supplier.setId(doc.getObjectId("_id"));
        supplier.setName(doc.getString("name"));
        supplier.setCif(doc.getString("cif"));
        supplier.setEmail(doc.getString("email"));
        supplier.setTelephone(doc.getString("telephone"));
        return supplier;
    }

    /**
     * Converts a Supplier into a document to save it in MongoDB.
     *
     * @param supplier supplier to convert
     * @return the document with every field of the supplier
     */
    private Document toDocument(Supplier supplier) {
        Document doc;

        doc = new Document();
        // A new supplier has no _id yet: MongoDB generates it when inserting
        if (supplier.getId() != null) {
            doc.append("_id", supplier.getId());
        }
        doc.append("name", supplier.getName());
        doc.append("cif", supplier.getCif());
        doc.append("email", supplier.getEmail());
        // The telephone is optional: only saved if it has a value
        if (supplier.getTelephone() != null) {
            doc.append("telephone", supplier.getTelephone());
        }
        return doc;
    }
}
