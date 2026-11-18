/**
 * Data access layer of the application.
 * <p>
 * There is one DAO interface for each MongoDB collection (users, products,
 * suppliers, discountPacks and shipments) and one implementation with the
 * MongoDB driver ({@code XxxImplementacionBD}). {@link com.reto1.dao.Database}
 * opens the connection and {@link com.reto1.dao.SchemaInitializer} creates the
 * collections with their $jsonSchema validation and their indexes. These are
 * the only classes that run operations against the database.
 */
package com.reto1.dao;
