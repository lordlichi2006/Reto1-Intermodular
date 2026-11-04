package com.reto1.dao;

import java.util.ArrayList;
import java.util.List;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CountOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.reto1.model.User;

/**
 * MongoDB implementation of {@link UserDao}.
 * <p>
 * The collection is typed with {@link User}: thanks to the "_t" discriminator
 * registered in {@link Database}, each document is read back as a
 * {@link com.reto1.model.Customer} or a {@link com.reto1.model.Worker}.
 */
public class UserImplementacionBD implements UserDao {

    /** Name of the collection in MongoDB. */
    private static final String COLLECTION = "users";

    /** Typed collection: documents are converted to User objects automatically. */
    private final MongoCollection<User> collection;

    /**
     * Uses the application database (config.properties).
     */
    public UserImplementacionBD() {
        this(Database.getDatabase());
    }

    /**
     * Uses the given database. Useful for tests on a separate database.
     *
     * @param db database that contains the "users" collection
     */
    public UserImplementacionBD(MongoDatabase db) {
        this.collection = db.getCollection(COLLECTION, User.class);
    }

    @Override
    public User login(String email, String passwordHash) {
        Bson filter;
        User user;

        // TODO (BCrypt phase): when passwords are hashed with BCrypt, the same
        // password produces a different hash each time, so it will not be
        // possible to search by hash. Then: findByEmail(email) and compare with
        // BCrypt.checkpw(plainPassword, user.getPassword()).
        filter = Filters.and(
                Filters.eq("email", email),
                Filters.eq("password", passwordHash));
        user = collection.find(filter).first();
        return user;
    }

    @Override
    public User findByEmail(String email) {
        User user;

        user = collection.find(Filters.eq("email", email)).first();
        return user;
    }

    @Override
    public boolean existsByEmail(String email) {
        long count;

        // limit(1): MongoDB stops counting as soon as it finds one document
        count = collection.countDocuments(Filters.eq("email", email), new CountOptions().limit(1));
        return count > 0;
    }

    @Override
    public List<User> findAll() {
        List<User> users;

        users = collection.find().sort(Sorts.ascending("name")).into(new ArrayList<>());
        return users;
    }

    @Override
    public User findById(ObjectId id) {
        User user;

        user = collection.find(Filters.eq("_id", id)).first();
        return user;
    }

    @Override
    public void insert(User user) {
        collection.insertOne(user);
    }

    @Override
    public boolean update(User user) {
        UpdateResult result;

        result = collection.replaceOne(Filters.eq("_id", user.getId()), user);
        return result.getMatchedCount() > 0;
    }

    @Override
    public boolean delete(ObjectId id) {
        DeleteResult result;

        result = collection.deleteOne(Filters.eq("_id", id));
        return result.getDeletedCount() > 0;
    }
}
