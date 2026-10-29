package com.reto1.dao;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.reto1.model.CartItem;
import com.reto1.model.Customer;
import com.reto1.model.CustomerType;
import com.reto1.model.Product;
import com.reto1.model.ProductSupplier;
import com.reto1.model.Role;
import com.reto1.model.Supplier;
import com.reto1.model.User;
import com.reto1.model.Worker;

/**
 * Temporary check of the models and the $jsonSchema validation.
 * Uses a throwaway database ("reto1_quickstart") that is dropped at the end,
 * so "reto1" is not touched. It will be replaced by JUnit tests in Fase 10.
 */
public class QuickStart {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        MongoDatabase db = Database.getDatabase("reto1_quickstart");
        try {
            db.drop();
            SchemaInitializer.init(db);
            MongoCollection<Product> products = db.getCollection("products", Product.class);
            MongoCollection<User> users = db.getCollection("users", User.class);
            MongoCollection<Supplier> suppliers = db.getCollection("suppliers", Supplier.class);

            // ---- Valid data: must be saved ----
            List<ProductSupplier> ps = new ArrayList<>();
            ps.add(new ProductSupplier(new ObjectId(), "Dulces Haribo Iberia", "B12345678"));
            Product bears = new Product("Ositos de oro", "Haribo", 1.50, 200, "Bolsa de 100 g", ps);
            mustSave("valid product", () -> products.insertOne(bears));

            List<String> addresses = new ArrayList<>();
            addresses.add("Calle Mayor 1, Bilbao");
            Customer ane = new Customer("Ane", "ane@gozo.test", "hash", "12345678Z", addresses, CustomerType.INDIVIDUAL);
            ane.getCart().add(new CartItem(bears.getId(), bears.getName(), bears.getBrand(), bears.getPrice(), 3));
            mustSave("valid customer", () -> users.insertOne(ane));
            mustSave("valid worker", () -> users.insertOne(new Worker("Jon", "jon@gozo.test", "hash", "87654321X", Role.ADMIN)));
            mustSave("valid supplier", () -> suppliers.insertOne(new Supplier("Dulces Haribo Iberia", "B12345678", "pedidos@haribo.test", "600000000")));

            // ---- Invalid data: MongoDB must reject it ----
            mustFail("negative price", () -> products.insertOne(new Product("Regaliz", "Fini", -1, 10, "", new ArrayList<>())));
            mustFail("negative stock", () -> products.insertOne(new Product("Regaliz", "Fini", 1, -5, "", new ArrayList<>())));
            mustFail("bad email", () -> users.insertOne(new Customer("Mikel", "not-an-email", "hash", "1Z", new ArrayList<>(), CustomerType.INDIVIDUAL)));
            mustFail("worker without role", () -> users.insertOne(new Worker("Iker", "iker@gozo.test", "hash", "2X", null)));
            mustFail("customer without type", () -> users.insertOne(new Customer("Leire", "leire@gozo.test", "hash", "3X", new ArrayList<>(), null)));
            mustFail("duplicated email", () -> users.insertOne(new Worker("Jon2", "jon@gozo.test", "hash", "4X", Role.WORKER)));
            mustFail("bad CIF", () -> suppliers.insertOne(new Supplier("Fini", "1234", "fini@fini.test", "600000001")));
            mustFail("duplicated CIF", () -> suppliers.insertOne(new Supplier("Otro", "B12345678", "otro@otro.test", "600000002")));

            // ---- Read back: discriminator rebuilds the right subclass ----
            for (User u : users.find()) {
                System.out.println("  read " + u.getClass().getSimpleName() + ": " + u.getEmail());
            }

            System.out.println("RESULT: " + passed + " passed, " + failed + " failed");
        } finally {
            db.drop();
            Database.close();
        }
    }

    private static void mustSave(String test, Runnable insert) {
        try {
            insert.run();
            ok(test);
        } catch (MongoWriteException e) {
            ko(test, "rejected but it is valid: " + e.getError().getMessage());
        }
    }

    private static void mustFail(String test, Runnable insert) {
        try {
            insert.run();
            ko(test, "was saved but it is invalid");
        } catch (MongoWriteException e) {
            // 121 = document failed validation, 11000 = duplicate key (unique index)
            ok(test + " (rejected, code " + e.getError().getCode() + ")");
        }
    }

    private static void ok(String msg) {
        passed++;
        System.out.println("  OK   " + msg);
    }

    private static void ko(String msg, String why) {
        failed++;
        System.out.println("  FAIL " + msg + " -> " + why);
    }
}
