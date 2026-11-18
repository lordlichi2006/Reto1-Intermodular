package com.reto1.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.types.ObjectId;

import com.reto1.dao.DiscountPackDao;
import com.reto1.dao.DiscountPackImplementacionBD;
import com.reto1.dao.ProductDao;
import com.reto1.dao.ProductImplementacionBD;
import com.reto1.dao.ShipmentDao;
import com.reto1.dao.ShipmentImplementacionBD;
import com.reto1.dao.SupplierDao;
import com.reto1.dao.SupplierImplementacionBD;
import com.reto1.dao.UserDao;
import com.reto1.dao.UserImplementacionBD;
import com.reto1.exceptions.DatabaseException;
import com.reto1.model.Customer;
import com.reto1.model.CustomerType;
import com.reto1.model.DiscountPack;
import com.reto1.model.Product;
import com.reto1.model.ProductSupplier;
import com.reto1.model.Role;
import com.reto1.model.Shipment;
import com.reto1.model.ShipmentLine;
import com.reto1.model.ShipmentStatus;
import com.reto1.model.Supplier;
import com.reto1.model.User;
import com.reto1.model.Worker;

/**
 * Loads the initial data of the application in the 5 collections:
 * users (3 admins, 2 workers and 5 customers), suppliers, products,
 * discount packs and shipments.
 * <p>
 * Each collection is only filled when it is empty, so the data is not
 * duplicated every time the program starts. The collections are filled in
 * order because they are related: products embed their suppliers, packs keep
 * the _id of their products and shipments keep the _id of their customer.
 * <p>
 * Every user has the password {@link #DEFAULT_PASSWORD} and an email like
 * name@gozoshop.com (for example alain@gozoshop.com).
 */
public final class FillData {

    /** Password of every initial user. */
    public static final String DEFAULT_PASSWORD = "1234";

    /** Milliseconds in one day, used to date the initial shipments in the past. */
    private static final long DAY_MILLIS = 86400000L;

    /** Logger of the application, used to write every operation in the log. */
    private static final Logger LOGGER = LoggerManager.getLogger();

    /** Not used: the class only has static methods. */
    private FillData() {
    }

    /**
     * Inserts the initial data in every collection that is still empty.
     *
     * @return {@code true} if something was inserted, {@code false} if every
     *         collection already had data
     * @throws DatabaseException if MongoDB is not available
     */
    public static boolean fillIfEmpty() throws DatabaseException {
        UserDao userDao;
        SupplierDao supplierDao;
        ProductDao productDao;
        DiscountPackDao packDao;
        ShipmentDao shipmentDao;
        boolean usersEmpty;
        boolean suppliersEmpty;
        boolean productsEmpty;
        boolean packsEmpty;
        boolean shipmentsEmpty;

        userDao = new UserImplementacionBD();
        supplierDao = new SupplierImplementacionBD();
        productDao = new ProductImplementacionBD();
        packDao = new DiscountPackImplementacionBD();
        shipmentDao = new ShipmentImplementacionBD();

        usersEmpty = userDao.findAll().isEmpty();
        suppliersEmpty = supplierDao.findAll().isEmpty();
        productsEmpty = productDao.findAll().isEmpty();
        packsEmpty = packDao.findAll().isEmpty();
        shipmentsEmpty = shipmentDao.findAll().isEmpty();

        if (usersEmpty) {
            for (User user : createUsers()) {
                userDao.insert(user);
            }
            LOGGER.log(Level.INFO, "FillData: initial users inserted");
        } else {
            LOGGER.log(Level.INFO, "FillData: users collection already has data");
        }

        if (suppliersEmpty) {
            for (Supplier supplier : createSuppliers()) {
                supplierDao.insert(supplier);
            }
            LOGGER.log(Level.INFO, "FillData: initial suppliers inserted");
        } else {
            LOGGER.log(Level.INFO, "FillData: suppliers collection already has data");
        }

        if (productsEmpty) {
            for (Product product : createProducts(supplierDao)) {
                productDao.insert(product);
            }
            LOGGER.log(Level.INFO, "FillData: initial products inserted");
        } else {
            LOGGER.log(Level.INFO, "FillData: products collection already has data");
        }

        if (packsEmpty) {
            for (DiscountPack pack : createPacks(productDao.findAll())) {
                if (!pack.getProductIds().isEmpty()) {
                    packDao.insert(pack);
                }
            }
            LOGGER.log(Level.INFO, "FillData: initial discount packs inserted");
        } else {
            LOGGER.log(Level.INFO, "FillData: discountPacks collection already has data");
        }

        if (shipmentsEmpty) {
            fillShipments(userDao, shipmentDao, productDao.findAll());
            LOGGER.log(Level.INFO, "FillData: initial shipments inserted");
        } else {
            LOGGER.log(Level.INFO, "FillData: shipments collection already has data");
        }

        return usersEmpty || suppliersEmpty || productsEmpty || packsEmpty || shipmentsEmpty;
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

    /**
     * Builds the list of initial suppliers. The CIFs follow the format of the
     * validation schema (one letter, seven digits and a digit or letter).
     *
     * @return 5 suppliers (not saved yet)
     */
    private static ArrayList<Supplier> createSuppliers() {
        ArrayList<Supplier> suppliers;

        suppliers = new ArrayList<>();

        suppliers.add(new Supplier("PepsiCo", "A12345678", "orders@pepsico.com", "944000001"));
        suppliers.add(new Supplier("Nestle", "B12345678", "orders@nestle.com", "944000002"));
        suppliers.add(new Supplier("Coca-Cola", "C12345678", "orders@cocacola.com", "944000003"));
        suppliers.add(new Supplier("Mondelez", "D12345678", "orders@mondelez.com", "944000004"));
        suppliers.add(new Supplier("Unilever", "E12345678", "orders@unilever.com", "944000005"));

        return suppliers;
    }

    /**
     * Builds the list of initial products. Each product embeds the data of
     * its supplier, read from the "suppliers" collection by CIF, so the
     * supplierId of the product is the real _id of the supplier.
     *
     * @param supplierDao data access object of the suppliers
     * @return 10 products (not saved yet)
     * @throws DatabaseException if MongoDB is not available
     */
    private static ArrayList<Product> createProducts(SupplierDao supplierDao) throws DatabaseException {
        ArrayList<Product> products;
        List<ProductSupplier> pepsico;
        List<ProductSupplier> nestle;
        List<ProductSupplier> cocaCola;
        List<ProductSupplier> mondelez;
        List<ProductSupplier> unilever;

        pepsico = supplierByCif(supplierDao, "A12345678");
        nestle = supplierByCif(supplierDao, "B12345678");
        cocaCola = supplierByCif(supplierDao, "C12345678");
        mondelez = supplierByCif(supplierDao, "D12345678");
        unilever = supplierByCif(supplierDao, "E12345678");

        products = new ArrayList<>();

        products.add(new Product("Salted Chips", "Lays", 3.0, 9, "Classic salted potato chips", pepsico));
        products.add(new Product("Pringles Original", "Pringles", 2.5, 15, "Original flavored potato crisps",
                pepsico));
        products.add(new Product("KitKat", "Nestle", 1.5, 30, "Chocolate wafer bar", nestle));
        products.add(new Product("Nesquik", "Nestle", 4.2, 12, "Chocolate milk powder", nestle));
        products.add(new Product("Coca-Cola", "Coca-Cola", 2.0, 25, "Classic carbonated soft drink", cocaCola));
        products.add(new Product("Fanta Orange", "Coca-Cola", 1.8, 20, "Orange flavored soft drink", cocaCola));
        products.add(new Product("Oreo", "Mondelez", 2.2, 18, "Chocolate sandwich cookies", mondelez));
        products.add(new Product("Milka Chocolate", "Milka", 2.8, 14, "Alpine milk chocolate", mondelez));
        products.add(new Product("Dove Soap", "Dove", 3.5, 10, "Moisturizing beauty bar", unilever));
        products.add(new Product("Lipton Ice Tea", "Lipton", 2.4, 16, "Refreshing lemon iced tea", unilever));

        return products;
    }

    /**
     * Looks for a supplier by CIF and returns it in the format that the
     * products embed (supplierId, name and cif).
     *
     * @param supplierDao data access object of the suppliers
     * @param cif         CIF of the supplier
     * @return a list with the supplier, or an empty list if it does not exist
     * @throws DatabaseException if MongoDB is not available
     */
    private static List<ProductSupplier> supplierByCif(SupplierDao supplierDao, String cif)
            throws DatabaseException {
        ArrayList<ProductSupplier> result;
        Supplier supplier;

        result = new ArrayList<>();
        supplier = supplierDao.findByCif(cif);
        if (supplier != null) {
            result.add(new ProductSupplier(supplier.getId(), supplier.getName(), supplier.getCif()));
        }
        return result;
    }

    /**
     * Builds the initial discount packs with the _id of products already
     * saved in the database.
     *
     * @param products products read from the database
     * @return 3 packs (not saved yet)
     */
    private static ArrayList<DiscountPack> createPacks(ArrayList<Product> products) {
        ArrayList<DiscountPack> packs;
        DiscountPack snacks;
        DiscountPack chocolate;
        DiscountPack drinks;

        snacks = new DiscountPack("Snack Pack", 15, new ArrayList<>(), 20);
        addProductId(snacks, products, "Salted Chips");
        addProductId(snacks, products, "Pringles Original");

        chocolate = new DiscountPack("Chocolate Lovers", 20, new ArrayList<>(), 15);
        addProductId(chocolate, products, "KitKat");
        addProductId(chocolate, products, "Milka Chocolate");
        addProductId(chocolate, products, "Oreo");

        drinks = new DiscountPack("Party Drinks", 10, new ArrayList<>(), 30);
        addProductId(drinks, products, "Coca-Cola");
        addProductId(drinks, products, "Fanta Orange");

        packs = new ArrayList<>();
        packs.add(snacks);
        packs.add(chocolate);
        packs.add(drinks);
        return packs;
    }

    /**
     * Adds to a pack the _id of the product with the given name, if it exists.
     *
     * @param pack     pack that receives the product
     * @param products products read from the database
     * @param name     name of the product
     */
    private static void addProductId(DiscountPack pack, ArrayList<Product> products, String name) {
        Product product;

        product = findProduct(products, name);
        if (product != null) {
            pack.getProductIds().add(product.getId());
        }
    }

    /**
     * Inserts the initial shipments: one of each status, made by different
     * customers. Each customer also gets the _id of the shipment in its
     * shipmentIds array and, if the shipment is not cancelled, its total in
     * totalSpent.
     *
     * @param userDao     data access object of the users
     * @param shipmentDao data access object of the shipments
     * @param products    products read from the database
     * @throws DatabaseException if MongoDB is not available
     */
    private static void fillShipments(UserDao userDao, ShipmentDao shipmentDao, ArrayList<Product> products)
            throws DatabaseException {
        ArrayList<ShipmentLine> lines;

        lines = new ArrayList<>();
        addLine(lines, products, "Coca-Cola", 2);
        addLine(lines, products, "KitKat", 3);
        insertShipment(userDao, shipmentDao, "mikel@gozoshop.com", lines, ShipmentStatus.ARRIVED,
                "SEUR", "SEUR-100001", 12);

        lines = new ArrayList<>();
        addLine(lines, products, "Oreo", 1);
        addLine(lines, products, "Milka Chocolate", 2);
        insertShipment(userDao, shipmentDao, "izai@gozoshop.com", lines, ShipmentStatus.IN_SHIPMENT,
                "Correos", "CORR-200002", 3);

        lines = new ArrayList<>();
        addLine(lines, products, "Salted Chips", 6);
        addLine(lines, products, "Lipton Ice Tea", 8);
        insertShipment(userDao, shipmentDao, "begona@gozoshop.com", lines, ShipmentStatus.IN_PROGRESS,
                null, null, 1);

        lines = new ArrayList<>();
        addLine(lines, products, "Pringles Original", 1);
        insertShipment(userDao, shipmentDao, "adam@gozoshop.com", lines, ShipmentStatus.CANCELLED,
                null, null, 5);
    }

    /**
     * Adds a shipment line with the current data of a product, if it exists.
     *
     * @param lines    lines of the shipment
     * @param products products read from the database
     * @param name     name of the product
     * @param units    units bought
     */
    private static void addLine(ArrayList<ShipmentLine> lines, ArrayList<Product> products, String name,
            int units) {
        Product product;

        product = findProduct(products, name);
        if (product != null) {
            lines.add(new ShipmentLine(product.getId(), product.getName(), product.getPrice(), units));
        }
    }

    /**
     * Saves a shipment of a customer and updates the customer (shipmentIds
     * and totalSpent). Nothing is saved if the customer does not exist, has
     * no address or the shipment has no lines.
     *
     * @param userDao     data access object of the users
     * @param shipmentDao data access object of the shipments
     * @param email       email of the customer
     * @param lines       lines of the shipment
     * @param status      status of the shipment
     * @param company     shipping company, or null if not assigned yet
     * @param trackingId  tracking code, or null if not assigned yet
     * @param daysAgo     how many days ago the shipment was created
     * @throws DatabaseException if MongoDB is not available
     */
    private static void insertShipment(UserDao userDao, ShipmentDao shipmentDao, String email,
            ArrayList<ShipmentLine> lines, ShipmentStatus status, String company, String trackingId,
            int daysAgo) throws DatabaseException {
        User user;
        Customer customer;
        Shipment shipment;
        ArrayList<ObjectId> shipmentIds;
        double total;

        user = userDao.findByEmail(email);
        if (user instanceof Customer && !((Customer) user).getAddresses().isEmpty() && !lines.isEmpty()) {
            customer = (Customer) user;

            total = 0;
            for (ShipmentLine line : lines) {
                total = total + line.getPrice() * line.getUnits();
            }
            total = Math.round(total * 100) / 100.0;

            shipment = new Shipment(customer.getId(), customer.getAddresses().get(0), lines, total, company);
            shipment.setStatus(status);
            shipment.setTrackingId(trackingId);
            shipment.setCreatedAt(new Date(System.currentTimeMillis() - daysAgo * DAY_MILLIS));
            shipmentDao.insert(shipment);

            shipmentIds = new ArrayList<>(customer.getShipmentIds());
            shipmentIds.add(shipment.getId());
            customer.setShipmentIds(shipmentIds);
            if (status != ShipmentStatus.CANCELLED) {
                customer.setTotalSpent(customer.getTotalSpent() + total);
            }
            userDao.update(customer);
        }
    }

    /**
     * Looks for a product by its exact name. The loop stops as soon as the
     * product is found thanks to the boolean variable {@code found}.
     *
     * @param products products read from the database
     * @param name     name of the product
     * @return the first product with that name, or null if there is none
     */
    private static Product findProduct(ArrayList<Product> products, String name) {
        Product result;
        boolean found;
        int i;

        result = null;
        found = false;
        i = 0;
        while (i < products.size() && !found) {
            if (products.get(i).getName().equals(name)) {
                result = products.get(i);
                found = true;
            }
            i++;
        }
        return result;
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
