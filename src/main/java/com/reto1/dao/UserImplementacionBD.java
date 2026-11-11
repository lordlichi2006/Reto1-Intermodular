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
import com.reto1.model.CartItem;
import com.reto1.model.Customer;
import com.reto1.model.CustomerType;
import com.reto1.model.Role;
import com.reto1.model.User;
import com.reto1.model.Worker;
import com.reto1.util.LoggerManager;

/**
 * MongoDB implementation of {@link UserDao}.
 * <p>
 * Customers and workers are saved in the same collection. The field
 * "userType" ("customer" or "worker") says which class each document is.
 */
public class UserImplementacionBD implements UserDao {

    /** Name of the collection in MongoDB. */
    private static final String COLLECTION = "users";
    /** Values of the field "userType". */
    private static final String CUSTOMER = "customer";
    private static final String WORKER = "worker";

    private static final Logger LOGGER = LoggerManager.getLogger();

    /** The "users" collection. Each document is like a row of a table. */
    private final MongoCollection<Document> collection;

    /**
     * Gets the "users" collection of the application database.
     */
    public UserImplementacionBD() {
        collection = Database.getDatabase().getCollection(COLLECTION);
    }

    @Override
    public User login(String email, String password) throws DatabaseException {
        Document filter;
        Document doc;
        User user;

        user = null;

        filter = new Document("email", email).append("password", password);
        try {
            doc = collection.find(filter).first();
            if (doc != null) {
                user = toUser(doc);
            }
            LOGGER.info("LOGIN users: " + email);
        } catch (MongoException e) {
            LOGGER.severe("Error in LOGIN users: " + e.getMessage());
            throw new DatabaseException("Unable to check the login. Is MongoDB running?");
        }
        return user;
    }

    @Override
    public User findByEmail(String email) throws DatabaseException {
        Document doc;
        User user;

        user = null;
        try {
            doc = collection.find(new Document("email", email)).first();
            if (doc != null) {
                user = toUser(doc);
            }
            LOGGER.info("SELECT users by email: " + email);
        } catch (MongoException e) {
            LOGGER.severe("Error in SELECT users by email: " + e.getMessage());
            throw new DatabaseException("Unable to load the user.");
        }
        return user;
    }

    @Override
    public boolean existsByEmail(String email) throws DatabaseException {
        boolean exists;

        try {

            exists = collection.find(new Document("email", email)).first() != null;
            LOGGER.info("SELECT users exists email: " + email);
        } catch (MongoException e) {
            LOGGER.severe("Error in SELECT users exists email: " + e.getMessage());
            throw new DatabaseException("Unable to check the email.");
        }
        return exists;
    }

    @Override
    public ArrayList<User> findAll() throws DatabaseException {
        ArrayList<User> users;

        users = new ArrayList<>();
        try {

            for (Document doc : collection.find().sort(new Document("name", 1))) {
                users.add(toUser(doc));
            }
            LOGGER.info("SELECT users: " + users.size() + " found");
        } catch (MongoException e) {
            LOGGER.severe("Error in SELECT users: " + e.getMessage());
            throw new DatabaseException("Unable to load the users.");
        }
        return users;
    }

    @Override
    public User findById(ObjectId id) throws DatabaseException {
        Document doc;
        User user;

        user = null;
        try {
            doc = collection.find(new Document("_id", id)).first();
            if (doc != null) {
                user = toUser(doc);
            }
            LOGGER.info("SELECT users by id: " + id);
        } catch (MongoException e) {
            LOGGER.severe("Error in SELECT users by id: " + e.getMessage());
            throw new DatabaseException("Unable to load the user.");
        }
        return user;
    }

    @Override
    public void insert(User user) throws DatabaseException {
        Document doc;

        doc = toDocument(user);
        try {
            collection.insertOne(doc);

            user.setId(doc.getObjectId("_id"));
            LOGGER.info("INSERT users: " + user.getEmail());
        } catch (MongoException e) {
            LOGGER.severe("Error in INSERT users: " + e.getMessage());
            throw new DatabaseException("Unable to save the user. Is the email already used?");
        }
    }

    @Override
    public boolean update(User user) throws DatabaseException {
        UpdateResult result;
        boolean updated;

        try {
            result = collection.replaceOne(new Document("_id", user.getId()), toDocument(user));
            updated = result.getMatchedCount() > 0;
            LOGGER.info("UPDATE users: " + user.getEmail());
        } catch (MongoException e) {
            LOGGER.severe("Error in UPDATE users: " + e.getMessage());
            throw new DatabaseException("Unable to save the changes of the user.");
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
            LOGGER.info("DELETE users: " + id);
        } catch (MongoException e) {
            LOGGER.severe("Error in DELETE users: " + e.getMessage());
            throw new DatabaseException("Unable to delete the user.");
        }
        return deleted;
    }

    /**
     * Converts a document of MongoDB into a Customer or a Worker
     * (like reading a row of a ResultSet).
     *
     * @param doc document read from the collection
     * @return a Customer if userType is "customer", a Worker if not
     */
    private User toUser(Document doc) {
        User user;
        Customer customer;
        Worker worker;
        ArrayList<CartItem> cart;
        CartItem item;

        if (CUSTOMER.equals(doc.getString("userType"))) {
            customer = new Customer();
            customer.setType(CustomerType.valueOf(doc.getString("type")));
            customer.setTotalSpent(doc.getDouble("totalSpent"));
            customer.setAddresses(doc.getList("addresses", String.class));
            customer.setShipmentIds(doc.getList("shipmentIds", ObjectId.class));

            cart = new ArrayList<>();
            for (Document line : doc.getList("cart", Document.class)) {
                item = new CartItem();
                item.setProductId(line.getObjectId("productId"));
                item.setName(line.getString("name"));
                item.setBrand(line.getString("brand"));
                item.setPrice(line.getDouble("price"));
                item.setUnits(line.getInteger("units"));
                cart.add(item);
            }
            customer.setCart(cart);
            user = customer;
        } else {
            worker = new Worker();
            worker.setRole(Role.valueOf(doc.getString("role")));
            user = worker;
        }

        user.setId(doc.getObjectId("_id"));
        user.setName(doc.getString("name"));
        user.setEmail(doc.getString("email"));
        user.setPassword(doc.getString("password"));
        user.setIdDocument(doc.getString("idDocument"));
        return user;
    }

    /**
     * Converts a Customer or a Worker into a document to save it in MongoDB.
     *
     * @param user user to convert
     * @return the document with every field of the user
     */
    private Document toDocument(User user) {
        Document doc;
        Customer customer;
        Worker worker;
        ArrayList<Document> cart;

        doc = new Document();

        if (user.getId() != null) {
            doc.append("_id", user.getId());
        }
        doc.append("name", user.getName());
        doc.append("email", user.getEmail());
        doc.append("password", user.getPassword());
        doc.append("idDocument", user.getIdDocument());

        if (user instanceof Customer) {
            customer = (Customer) user;
            cart = new ArrayList<>();
            for (CartItem item : customer.getCart()) {
                cart.add(new Document("productId", item.getProductId())
                        .append("name", item.getName())
                        .append("brand", item.getBrand())
                        .append("price", item.getPrice())
                        .append("units", item.getUnits()));
            }
            doc.append("userType", CUSTOMER);
            doc.append("type", customer.getType().name());
            doc.append("totalSpent", customer.getTotalSpent());
            doc.append("addresses", customer.getAddresses());
            doc.append("cart", cart);
            doc.append("shipmentIds", customer.getShipmentIds());
        } else {
            worker = (Worker) user;
            doc.append("userType", WORKER);
            doc.append("role", worker.getRole().name());
        }
        return doc;
    }
}
