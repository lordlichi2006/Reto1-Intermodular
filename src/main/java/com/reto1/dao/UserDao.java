package com.reto1.dao;

import java.util.List;

import org.bson.types.ObjectId;

import com.reto1.model.User;

/**
 * Data access operations for the "users" collection (customers and workers).
 * <p>
 * The objects returned are {@link com.reto1.model.Customer} or
 * {@link com.reto1.model.Worker} depending on the "_t" discriminator.
 * <p>
 * Driver errors ({@code MongoException}, {@code MongoWriteException}) are not
 * caught here: they are propagated so the controller can show them.
 */
public interface UserDao {

    /**
     * Checks the login data of a user.
     *
     * @param email        email typed by the user
     * @param passwordHash hash of the password typed by the user
     * @return the user (Customer or Worker) if email and password match, or
     *         {@code null} if they do not
     */
    User login(String email, String passwordHash);

    /**
     * Finds a user by email.
     *
     * @param email email to search
     * @return the user, or {@code null} if there is no user with that email
     */
    User findByEmail(String email);

    /**
     * Checks if an email is already registered (useful before a sign up).
     *
     * @param email email to check
     * @return {@code true} if a user with that email exists
     */
    boolean existsByEmail(String email);

    /**
     * Returns every user, sorted by name.
     *
     * @return list of users (empty if there are none)
     */
    List<User> findAll();

    /**
     * Finds a user by its _id.
     *
     * @param id _id of the user
     * @return the user, or {@code null} if it does not exist
     */
    User findById(ObjectId id);

    /**
     * Saves a new user. The driver generates the _id and sets it in the object.
     *
     * @param user user to save
     * @throws com.mongodb.MongoWriteException if the schema validation fails or
     *                                         the email is duplicated
     */
    void insert(User user);

    /**
     * Replaces the stored user with the given one (same _id).
     *
     * @param user user with the new data
     * @return {@code true} if a user with that _id was found
     * @throws com.mongodb.MongoWriteException if the new data is not valid
     */
    boolean update(User user);

    /**
     * Deletes a user.
     *
     * @param id _id of the user
     * @return {@code true} if a user was deleted
     */
    boolean delete(ObjectId id);
}
