package com.reto1.dao;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.reto1.exceptions.DatabaseException;
import com.reto1.model.Product;
import com.reto1.model.ProductSupplier;
import com.reto1.util.LoggerManager;

/**
 * MongoDB implementation of {@link ProductDao}.
 */
public class ProductImplementacionBD implements ProductDao {

    /** Name of the collection in MongoDB. */
    private static final String COLLECTION = "products";

    private static final Logger LOGGER = LoggerManager.getLogger();

    /** The "products" collection. Each document is like a row of a table. */
    private final MongoCollection<Document> collection;

    /**
     * Gets the "products" collection of the application database.
     */
    public ProductImplementacionBD() {
        collection = Database.getDatabase().getCollection(COLLECTION);
    }

    @Override
    public ArrayList<Product> findAll() throws DatabaseException {
        ArrayList<Product> products;

        products = new ArrayList<>();
        try {
            // sort { name: 1 }  ->  ORDER BY name ASC
            for (Document doc : collection.find().sort(new Document("name", 1))) {
                products.add(toProduct(doc));
            }
            LOGGER.info("SELECT products: " + products.size() + " found");
        } catch (MongoException e) {
            LOGGER.severe("Error in SELECT products: " + e.getMessage());
            throw new DatabaseException("Unable to load the products.");
        }
        return products;
    }

    @Override
    public Product findById(ObjectId id) throws DatabaseException {
        Document doc;
        Product product;

        product = null;
        try {
            doc = collection.find(new Document("_id", id)).first();
            if (doc != null) {
                product = toProduct(doc);
            }
            LOGGER.info("SELECT products by id: " + id);
        } catch (MongoException e) {
            LOGGER.severe("Error in SELECT products by id: " + e.getMessage());
            throw new DatabaseException("Unable to load the product.");
        }
        return product;
    }

    @Override
    public ArrayList<Product> findByName(String text) throws DatabaseException {
        Document filter;
        ArrayList<Product> products;

        products = new ArrayList<>();
        // { name: { $regex: "text", $options: "i" } }  ->  WHERE name LIKE '%text%'
        // Pattern.quote: characters like "." or "*" are searched as normal text.
        // "i" = ignore upper/lower case.
        filter = new Document("name", new Document("$regex", Pattern.quote(text)).append("$options", "i"));
        try {
            for (Document doc : collection.find(filter).sort(new Document("name", 1))) {
                products.add(toProduct(doc));
            }
            LOGGER.info("SELECT products by name: " + text);
        } catch (MongoException e) {
            LOGGER.severe("Error in SELECT products by name: " + e.getMessage());
            throw new DatabaseException("Unable to search the products.");
        }
        return products;
    }

    @Override
    public void insert(Product product) throws DatabaseException {
        Document doc;

        doc = toDocument(product);
        try {
            collection.insertOne(doc);
            // MongoDB has added the generated _id to the document
            product.setId(doc.getObjectId("_id"));
            LOGGER.info("INSERT products: " + product.getName());
        } catch (MongoException e) {
            LOGGER.severe("Error in INSERT products: " + e.getMessage());
            throw new DatabaseException("Unable to save the product.");
        }
    }

    @Override
    public boolean update(Product product) throws DatabaseException {
        UpdateResult result;
        boolean updated;

        try {
            result = collection.replaceOne(new Document("_id", product.getId()), toDocument(product));
            updated = result.getMatchedCount() > 0;
            LOGGER.info("UPDATE products: " + product.getName());
        } catch (MongoException e) {
            LOGGER.severe("Error in UPDATE products: " + e.getMessage());
            throw new DatabaseException("Unable to save the changes of the product.");
        }
        return updated;
    }

    @Override
    public boolean updateStock(ObjectId id, int stock) throws DatabaseException {
        Document update;
        UpdateResult result;
        boolean updated;

        // { $set: { stock: 5 } }  ->  UPDATE products SET stock = 5
        // Only "stock" changes; the rest of the document stays the same
        update = new Document("$set", new Document("stock", stock));
        try {
            result = collection.updateOne(new Document("_id", id), update);
            updated = result.getMatchedCount() > 0;
            LOGGER.info("UPDATE products stock: " + id + " -> " + stock);
        } catch (MongoException e) {
            LOGGER.severe("Error in UPDATE products stock: " + e.getMessage());
            throw new DatabaseException("Unable to change the stock.");
        }
        return updated;
    }

    @Override
    public boolean decreaseStock(ObjectId id, int units) throws DatabaseException {
        boolean decreased;

        // TODO: updateOne with
        //       filter: new Document("_id", id).append("stock", new Document("$gte", units))
        //               (only if stock >= units)
        //       update: new Document("$inc", new Document("stock", -units))
        //       decreased = result.getModifiedCount() > 0
        //       (try/catch MongoException + log, like the other methods)
        decreased = false;
        return decreased;
    }

    @Override
    public boolean delete(ObjectId id) throws DatabaseException {
        DeleteResult result;
        boolean deleted;

        try {
            result = collection.deleteOne(new Document("_id", id));
            deleted = result.getDeletedCount() > 0;
            LOGGER.info("DELETE products: " + id);
        } catch (MongoException e) {
            LOGGER.severe("Error in DELETE products: " + e.getMessage());
            throw new DatabaseException("Unable to delete the product.");
        }
        return deleted;
    }

    /**
     * Converts a document of MongoDB into a Product (like reading a row of a
     * ResultSet).
     *
     * @param doc document read from the collection
     * @return the product
     */
    private Product toProduct(Document doc) {
        Product product;
        ArrayList<ProductSupplier> suppliers;
        ProductSupplier supplier;

        product = new Product();
        product.setId(doc.getObjectId("_id"));
        product.setName(doc.getString("name"));
        product.setBrand(doc.getString("brand"));
        product.setPrice(doc.getDouble("price"));
        product.setStock(doc.getInteger("stock"));
        product.setDescription(doc.getString("description"));
        // The suppliers are an array of documents inside the product
        suppliers = new ArrayList<>();
        for (Document line : doc.getList("suppliers", Document.class)) {
            supplier = new ProductSupplier();
            supplier.setSupplierId(line.getObjectId("supplierId"));
            supplier.setName(line.getString("name"));
            supplier.setCif(line.getString("cif"));
            suppliers.add(supplier);
        }
        product.setSuppliers(suppliers);
        return product;
    }

    /**
     * Converts a Product into a document to save it in MongoDB.
     *
     * @param product product to convert
     * @return the document with every field of the product
     */
    private Document toDocument(Product product) {
        Document doc;
        ArrayList<Document> suppliers;

        suppliers = new ArrayList<>();
        for (ProductSupplier supplier : product.getSuppliers()) {
            suppliers.add(new Document("supplierId", supplier.getSupplierId())
                    .append("name", supplier.getName())
                    .append("cif", supplier.getCif()));
        }

        doc = new Document();
        // A new product has no _id yet: MongoDB generates it when inserting
        if (product.getId() != null) {
            doc.append("_id", product.getId());
        }
        doc.append("name", product.getName());
        doc.append("brand", product.getBrand());
        doc.append("price", product.getPrice());
        doc.append("stock", product.getStock());
        // The description is optional: if it is empty it is not saved
        if (product.getDescription() != null) {
            doc.append("description", product.getDescription());
        }
        doc.append("suppliers", suppliers);
        return doc;
    }
}
