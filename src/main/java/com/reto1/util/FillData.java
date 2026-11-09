package com.reto1.util;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.reto1.dao.UserDao;
import com.reto1.dao.UserImplementacionBD;
import com.reto1.model.Customer;
import com.reto1.model.CustomerType;
import com.reto1.model.Role;
import com.reto1.model.User;
import com.reto1.model.Worker;

/**
 * Loads the initial data of the application: 3 admins, 2 workers and
 * 5 customers. It only runs when the "users" collection is empty, so the
 * data is not duplicated every time the program starts.
 * <p>
 * Every user has the password {@link #DEFAULT_PASSWORD} and an email like
 * name@gozoshop.com (for example alain@gozoshop.com).
 */
public final class FillData {

    /** Password of every initial user (it will be hashed in the BCrypt phase). */
    public static final String DEFAULT_PASSWORD = "1234";

    private static final Logger LOGGER = LoggerManager.getLogger();

    private FillData() {
    }

    /**
     * Inserts the initial users if the database has no users yet.
     *
     * @return {@code true} if the data was inserted, {@code false} if the
     *         database already had users
     * @throws com.mongodb.MongoException if MongoDB is not available
     */
    public static boolean fillIfEmpty() {
        UserDao userDao;
        boolean isEmpty;

        userDao = new UserImplementacionBD();
        isEmpty = userDao.findAll().isEmpty();

        if (isEmpty) {
            for (User user : createUsers()) {
                userDao.insert(user);
            }
            LOGGER.log(Level.INFO, "FillData: initial users inserted");
        } else {
            LOGGER.log(Level.INFO, "FillData: the database already has data, nothing inserted");
        }
        return isEmpty;
    }

    /**
     * Builds the list of initial users. The DNIs have a valid letter.
     *
     * @return 3 admins, 2 workers and 5 customers
     */
    private static ArrayList<User> createUsers() {
        ArrayList<User> users;

        users = new ArrayList<>();

        // ---- Admins (the team) ----
        users.add(new Worker("Alain", "alain@gozoshop.com", DEFAULT_PASSWORD, "10000000Z", Role.ADMIN));
        users.add(new Worker("Ekaitz", "ekaitz@gozoshop.com", DEFAULT_PASSWORD, "10000001S", Role.ADMIN));
        users.add(new Worker("Ivan", "ivan@gozoshop.com", DEFAULT_PASSWORD, "10000002Q", Role.ADMIN));

        // ---- Workers ----
        users.add(new Worker("Jon", "jon@gozoshop.com", DEFAULT_PASSWORD, "10000003V", Role.WORKER));
        users.add(new Worker("Julen", "julen@gozoshop.com", DEFAULT_PASSWORD, "10000004H", Role.WORKER));

        // ---- Customers ----
        users.add(createCustomer("Mikel", "mikel@gozoshop.com", "10000005L",
                "Calle Mayor 1, Bilbao", CustomerType.INDIVIDUAL));
        users.add(createCustomer("Izai", "izai@gozoshop.com", "10000006C",
                "Gran Via 25, Bilbao", CustomerType.INDIVIDUAL));
        users.add(createCustomer("Adam", "adam@gozoshop.com", "10000007K",
                "Calle Autonomia 12, Barakaldo", CustomerType.INDIVIDUAL));
        users.add(createCustomer("Begoña", "begona@gozoshop.com", "10000008E",
                "Poligono Asua 3, Erandio", CustomerType.BUSINESS));
        users.add(createCustomer("Unai", "unai@gozoshop.com", "10000009T",
                "Calle San Juan 7, Getxo", CustomerType.INDIVIDUAL));

        return users;
    }

    /**
     * Creates a customer with one saved address.
     *
     * @param name       customer name
     * @param email      customer email
     * @param idDocument DNI
     * @param address    first saved address
     * @param type       individual or business
     * @return the new customer (not saved yet)
     */
    private static Customer createCustomer(String name, String email, String idDocument,
            String address, CustomerType type) {
        ArrayList<String> addresses;
        Customer customer;

        addresses = new ArrayList<>();
        addresses.add(address);
        customer = new Customer(name, email, DEFAULT_PASSWORD, idDocument, addresses, type);
        return customer;
    }
}
