package com.reto1.dao;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.reto1.models.Product;
import com.reto1.models.Supplier;

/**
 * Temporary connection check: inserts one product and reads it back.
 * It will be replaced in Fase 5 by the schema initializer and sample data.
 */
public class QuickStart {

    public static void main(String[] args) {
        try {
            MongoDatabase database = Database.getDatabase();
            MongoCollection<Product> products = database.getCollection("products", Product.class);

            ArrayList<Supplier> suppliers = new ArrayList<>();
            suppliers.add(new Supplier("Dulces Haribo Iberia", "B12345678", "pedidos@haribo.test", "600000000"));

            // The collection "products" is created automatically on first insert
            products.insertOne(new Product("Ositos de oro", "Haribo", 1.50, 200, "Bolsa de gominolas de frutas de 100 g", suppliers));

            Product product = products.find(eq("name", "Ositos de oro")).first();
            System.out.println(product != null ? product : "No matching documents found.");
        } finally {
            Database.close();
        }
    }
}
