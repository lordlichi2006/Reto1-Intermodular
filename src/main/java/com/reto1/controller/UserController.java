package com.reto1.controller;

import java.util.List;

import org.bson.types.ObjectId;

import com.reto1.dao.UserDao;
import com.reto1.dao.UserImplementacionBD;
import com.reto1.model.User;

/**
 * Business logic of the users (customers and workers).
 * <p>
 * The windows (package view) call this class; this class calls the DAO.
 * The field is the interface {@link UserDao}, so this class does not know
 * that the data is stored in MongoDB.
 */
public class UserController {

    private final UserDao dao;

    /**
     * Creates the controller with the MongoDB implementation of the DAO.
     */
    public UserController() {
        this.dao = new UserImplementacionBD();
    }

    /**
     * Checks the login data.
     *
     * @param email    email typed by the user
     * @param password password typed by the user
     * @return the user (Customer or Worker), or {@code null} if the data is wrong
     */
    public User login(String email, String password) {
        User user;

        // TODO (BCrypt phase): hash the password before comparing it
        user = dao.login(email, password);
        return user;
    }

    /**
     * @param email email to check
     * @return {@code true} if a user with that email already exists
     */
    public boolean existsByEmail(String email) {
        return dao.existsByEmail(email);
    }

    /**
     * @return every user, sorted by name
     */
    public List<User> findAll() {
        return dao.findAll();
    }

    /**
     * @param id _id of the user
     * @return the user, or {@code null} if it does not exist
     */
    public User findById(ObjectId id) {
        return dao.findById(id);
    }

    /**
     * Saves a new user (for example, a customer that registers).
     *
     * @param user user to save
     */
    public void insert(User user) {
        dao.insert(user);
    }

    /**
     * @param user user with the new data (same _id)
     * @return {@code true} if the user was found and updated
     */
    public boolean update(User user) {
        return dao.update(user);
    }

    /**
     * @param id _id of the user
     * @return {@code true} if the user was deleted
     */
    public boolean delete(ObjectId id) {
        return dao.delete(id);
    }
}
