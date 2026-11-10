package com.reto1.dao;

import java.util.ArrayList;

import org.bson.types.ObjectId;

import com.reto1.exceptions.DatabaseException;
import com.reto1.model.User;

/**
 * Data access operations for the "users" collection (customers and workers).
 * <p>
 * The objects returned are {@link com.reto1.model.Customer} or
 * {@link com.reto1.model.Worker}, depending on the field "userType".
 * <p>
 * If MongoDB fails, every method throws a {@link DatabaseException} that the
 * window catches to show the message.
 */
public interface UserDao {

    /**
     * Checks the login data of a user.
     *
     * @param email    email typed by the user
     * @param password password typed by the user
     * @return the user (Customer or Worker) if email and password match, or
     *         {@code null} if they do not
     * @throws DatabaseException if MongoDB fails
     */
    User login(String email, String password) throws DatabaseException;

    /**
     * Finds a user by email.
     *
     * @param email email to search
     * @return the user, or {@code null} if there is no user with that email
     * @throws DatabaseException if MongoDB fails
     */
    User findByEmail(String email) throws DatabaseException;

    /**
     * Checks if an email is already registered (useful before a sign up).
     *
     * @param email email to check
     * @return {@code true} if a user with that email exists
     * @throws DatabaseException if MongoDB fails
     */
    boolean existsByEmail(String email) throws DatabaseException;

    /**
     * Returns every user, sorted by name.
     *
     * @return list of users (empty if there are none)
     * @throws DatabaseException if MongoDB fails
     */
    ArrayList<User> findAll() throws DatabaseException;

    /**
     * Finds a user by its _id.
     *
     * @param id _id of the user
     * @return the user, or {@code null} if it does not exist
     * @throws DatabaseException if MongoDB fails
     */
    User findById(ObjectId id) throws DatabaseException;

    /**
     * Saves a new user. MongoDB generates the _id and it is set in the object.
     *
     * @param user user to save
     * @throws DatabaseException if MongoDB fails, the data is not valid or the
     *                           email is already used
     */
    void insert(User user) throws DatabaseException;

    /**
     * Replaces the stored user with the given one (same _id).
     *
     * @param user user with the new data
     * @return {@code true} if a user with that _id was found
     * @throws DatabaseException if MongoDB fails or the new data is not valid
     */
    boolean update(User user) throws DatabaseException;

    /**
     * Deletes a user.
     *
     * @param id _id of the user
     * @return {@code true} if a user was deleted
     * @throws DatabaseException if MongoDB fails
     */
    boolean delete(ObjectId id) throws DatabaseException;
}
