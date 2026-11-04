package com.reto1.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.reto1.model.Product;

/**
 * MongoDB implementation of {@link ProductDao}.
 */
public class ProductImplementacionBD implements ProductDao {

    /** Name of the collection in MongoDB. */
    private static final String COLLECTION = "products";

    /** Typed collection: documents are converted to Product objects automatically. */
    private final MongoCollection<Product> collection;

    /**
     * Uses the application database (config.properties).
     */
    public ProductImplementacionBD() {
        this(Database.getDatabase());
    }

    /**
     * Uses the given database. Useful for tests on a separate database.
     *
     * @param db database that contains the "products" collection
     */
    public ProductImplementacionBD(MongoDatabase db) {
        this.collection = db.getCollection(COLLECTION, Product.class);
    }

    @Override
    public List<Product> findAll() {
        List<Product> products;

        products = collection.find().sort(Sorts.ascending("name")).into(new ArrayList<>());
        return products;
    }

    @Override
    public Product findById(ObjectId id) {
        Product product;

        product = collection.find(Filters.eq("_id", id)).first();
        return product;
    }

    @Override
    public List<Product> findByName(String text) {
        String regex;
        List<Product> products;

        // Pattern.quote: characters like "." or "*" typed by the user are
        // searched literally, not as regex symbols. "i" = ignore case.
        regex = Pattern.quote(text);
        products = collection.find(Filters.regex("name", regex, "i"))
                .sort(Sorts.ascending("name"))
                .into(new ArrayList<>());
        return products;
    }

    @Override
    public void insert(Product product) {
        collection.insertOne(product);
    }

    @Override
    public boolean update(Product product) {
        UpdateResult result;

        result = collection.replaceOne(Filters.eq("_id", product.getId()), product);
        return result.getMatchedCount() > 0;
    }

    @Override
    public boolean updateStock(ObjectId id, int stock) {
        UpdateResult result;

        // Only the "stock" field changes; the rest of the document stays the same
        result = collection.updateOne(Filters.eq("_id", id), Updates.set("stock", stock));
        return result.getMatchedCount() > 0;
    }

    @Override
    public boolean delete(ObjectId id) {
        DeleteResult result;

        result = collection.deleteOne(Filters.eq("_id", id));
        return result.getDeletedCount() > 0;
    }
}
