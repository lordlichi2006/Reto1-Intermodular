package com.reto1.util;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.types.ObjectId;

import com.reto1.dao.ProductDao;
import com.reto1.dao.ProductImplementacionBD;
import com.reto1.dao.UserDao;
import com.reto1.dao.UserImplementacionBD;
import com.reto1.exceptions.DatabaseException;
import com.reto1.model.Customer;
import com.reto1.model.CustomerType;
import com.reto1.model.Product;
import com.reto1.model.ProductSupplier;
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

    /** Password of every initial user. */
    public static final String DEFAULT_PASSWORD = "1234";

    private static final Logger LOGGER = LoggerManager.getLogger();

    private FillData() {
    }

    /**
     * Inserts the initial users if the database has no users yet.
     *
     * @return {@code true} if the data was inserted, {@code false} if the
     *         database already had users
     * @throws DatabaseException if MongoDB is not available
     */
    public static boolean fillIfEmpty() throws DatabaseException {

        UserDao userDao = new UserImplementacionBD();
        ProductDao productDao = new ProductImplementacionBD();

        // Check each collection independently
        boolean usersEmpty = userDao.findAll().isEmpty();
        boolean productsEmpty = productDao.findAll().isEmpty();

        // Insert users if empty
        if (usersEmpty) {
            for (User user : createUsers()) {
                userDao.insert(user);
            }

            LOGGER.log(Level.INFO, "FillData: initial users inserted");
        } else {
            LOGGER.log(Level.INFO, "FillData: users collection already has data");
        }

        // Insert products if empty
        if (productsEmpty) {
            // Create suppliers and products
            List<ProductSupplier> suppliers = createSuppliers();
            List<Product> products = createProducts(suppliers);

            for (Product product : products) {
                productDao.insert(product);
            }

            LOGGER.log(Level.INFO, "FillData: initial products inserted");
        } else {
            LOGGER.log(Level.INFO, "FillData: products collection already has data");
        }

        return usersEmpty || productsEmpty;
    }

    /**
     * Builds the list of initial users. The DNIs have a valid letter.
     *
     * @return 3 admins, 2 workers and 5 customers
     */
    private static ArrayList<User> createUsers() {
        ArrayList<User> users;

        users = new ArrayList<>();

        users.add(new Worker("Alain", "alain@gozoshop.com", DEFAULT_PASSWORD, "10000000Z", Role.ADMIN));
        users.add(new Worker("Ekaitz", "ekaitz@gozoshop.com", DEFAULT_PASSWORD, "10000001S", Role.ADMIN));
        users.add(new Worker("Ivan", "ivan@gozoshop.com", DEFAULT_PASSWORD, "10000002Q", Role.ADMIN));

        users.add(new Worker("Jon", "jon@gozoshop.com", DEFAULT_PASSWORD, "10000003V", Role.WORKER));
        users.add(new Worker("Julen", "julen@gozoshop.com", DEFAULT_PASSWORD, "10000004H", Role.WORKER));

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

    private static ArrayList<Product> createProducts(List<ProductSupplier> suppliers) {
        ArrayList<Product> products = new ArrayList<>();

        products.add(
                new Product("Salted Chips", "Lays", 3.0, 9, "Classic salted potato chips", List.of(suppliers.get(0))));

        products.add(new Product("Pringles Original", "Pringles", 2.5, 15, "Original flavored potato crisps",
                List.of(suppliers.get(0))));

        products.add(new Product("KitKat", "Nestle", 1.5, 30, "Chocolate wafer bar", List.of(suppliers.get(1))));

        products.add(new Product("Nesquik", "Nestle", 4.2, 12, "Chocolate milk powder", List.of(suppliers.get(1))));

        products.add(new Product("Coca-Cola", "Coca-Cola", 2.0, 25, "Classic carbonated soft drink",
                List.of(suppliers.get(2))));

        products.add(new Product("Fanta Orange", "Coca-Cola", 1.8, 20, "Orange flavored soft drink",
                List.of(suppliers.get(2))));

        products.add(new Product("Oreo", "Mondelez", 2.2, 18, "Chocolate sandwich cookies", List.of(suppliers.get(3))));

        products.add(
                new Product("Milka Chocolate", "Milka", 2.8, 14, "Alpine milk chocolate", List.of(suppliers.get(3))));

        products.add(new Product("Dove Soap", "Dove", 3.5, 10, "Moisturizing beauty bar", List.of(suppliers.get(4))));

        products.add(new Product("Lipton Ice Tea", "Lipton", 2.4, 16, "Refreshing lemon iced tea",
                List.of(suppliers.get(4))));

        return products;
    }

    private static List<ProductSupplier> createSuppliers() {
        List<ProductSupplier> suppliers = new ArrayList<>();

        suppliers.add(new ProductSupplier(new ObjectId(), "PepsiCo", "A12345678"));
        suppliers.add(new ProductSupplier(new ObjectId(), "Nestle", "B12345678"));
        suppliers.add(new ProductSupplier(new ObjectId(), "Coca-Cola", "C12345678"));
        suppliers.add(new ProductSupplier(new ObjectId(), "Mondelez", "D12345678"));
        suppliers.add(new ProductSupplier(new ObjectId(), "Unilever", "E12345678"));

        return suppliers;
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
