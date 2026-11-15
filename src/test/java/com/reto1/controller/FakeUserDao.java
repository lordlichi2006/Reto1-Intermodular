package com.reto1.controller;

import java.util.ArrayList;

import org.bson.types.ObjectId;

import com.reto1.dao.UserDao;
import com.reto1.exceptions.DatabaseException;
import com.reto1.model.User;


public class FakeUserDao implements UserDao {

    /**
     * User returned by {@link #login(String, String)}. Null means wrong credentials.
     */
    private User userToReturn;

    /**
     * When true, {@link #login(String, String)} throws a {@link DatabaseException}.
     */
    private boolean databaseDown;

    /**
     * Last email received by {@link #login(String, String)}.
     */
    private String lastEmail;

    /**
     * Last password received by {@link #login(String, String)}.
     */
    private String lastPassword;

    /**
     * Sets the user that the login will return.
     *
     * @param userToReturn the user, or null to simulate wrong credentials
     */
    public void setUserToReturn(User userToReturn) {
        this.userToReturn = userToReturn;
    }

    /**
     * Simulates that MongoDB is not available.
     *
     * @param databaseDown true to make the login throw a {@link DatabaseException}
     */
    public void setDatabaseDown(boolean databaseDown) {
        this.databaseDown = databaseDown;
    }

    /**
     * Gets the last email received by the login.
     *
     * @return the email typed in the window
     */
    public String getLastEmail() {
        return lastEmail;
    }

    /**
     * Gets the last password received by the login.
     *
     * @return the password typed in the window
     */
    public String getLastPassword() {
        return lastPassword;
    }

    /**
     * Saves the credentials and returns the configured user, or throws
     * a {@link DatabaseException} if the database is marked as down.
     *
     * @param email email typed by the user
     * @param password password typed by the user
     * @return the configured user, or null
     * @throws DatabaseException if the database is marked as down
     */
    @Override
    public User login(String email, String password) throws DatabaseException {
        lastEmail = email;
        lastPassword = password;
        if (databaseDown) {
            throw new DatabaseException("Fake database error");
        }
        return userToReturn;
    }

    /**
     * Not used by the login tests.
     *
     * @param email ignored
     * @return always null
     */
    @Override
    public User findByEmail(String email) {
        return null;
    }

    /**
     * Not used by the login tests.
     *
     * @param email ignored
     * @return always false
     */
    @Override
    public boolean existsByEmail(String email) {
        return false;
    }

    /**
     * Not used by the login tests.
     *
     * @return an empty list
     */
    @Override
    public ArrayList<User> findAll() {
        return new ArrayList<>();
    }

    /**
     * Not used by the login tests.
     *
     * @param id ignored
     * @return always null
     */
    @Override
    public User findById(ObjectId id) {
        return null;
    }

    /**
     * Not used by the login tests.
     *
     * @param user ignored
     */
    @Override
    public void insert(User user) {
    }

    /**
     * Not used by the login tests.
     *
     * @param user ignored
     * @return always false
     */
    @Override
    public boolean update(User user) {
        return false;
    }

    /**
     * Not used by the login tests.
     *
     * @param id ignored
     * @return always false
     */
    @Override
    public boolean delete(ObjectId id) {
        return false;
    }
}
