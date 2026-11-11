package com.reto1.dao;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import com.reto1.exceptions.DatabaseException;
import com.reto1.model.Shipment;
import com.reto1.model.ShipmentLine;
import com.reto1.model.ShipmentStatus;
import com.reto1.util.LoggerManager;

/**
 * MongoDB implementation of {@link ShipmentDao}.
 */
public class ShipmentImplementacionBD implements ShipmentDao {

    /** Name of the collection in MongoDB. */
    private static final String COLLECTION = "shipments";

    private static final Logger LOGGER = LoggerManager.getLogger();

    /** The "shipments" collection. Each document is like a row of a table. */
    private final MongoCollection<Document> collection;

    /**
     * Gets the "shipments" collection of the application database.
     */
    public ShipmentImplementacionBD() {
        collection = Database.getDatabase().getCollection(COLLECTION);
    }

    @Override
    public void insert(Shipment shipment) throws DatabaseException {
        Document doc;

        doc = toDocument(shipment);
        try {
            collection.insertOne(doc);

            shipment.setId(doc.getObjectId("_id"));
            LOGGER.info("INSERT shipments: " + shipment.getId());
        } catch (MongoException e) {
            LOGGER.severe("Error in INSERT shipments: " + e.getMessage());
            throw new DatabaseException("Unable to save the shipment.");
        }
    }

    @Override
    public Shipment findById(ObjectId id) throws DatabaseException {
        Document doc;
        Shipment shipment;

        shipment = null;
        try {
            doc = collection.find(new Document("_id", id)).first();
            if (doc != null) {
                shipment = toShipment(doc);
            }
            LOGGER.info("SELECT shipments by id: " + id);
        } catch (MongoException e) {
            LOGGER.severe("Error in SELECT shipments by id: " + e.getMessage());
            throw new DatabaseException("Unable to load the shipment.");
        }
        return shipment;
    }

    @Override
    public ArrayList<Shipment> findAll() throws DatabaseException {
        ArrayList<Shipment> shipments;

        shipments = new ArrayList<>();
        try {

            for (Document doc : collection.find().sort(new Document("createdAt", -1))) {
                shipments.add(toShipment(doc));
            }
            LOGGER.info("SELECT shipments: " + shipments.size() + " found");
        } catch (MongoException e) {
            LOGGER.severe("Error in SELECT shipments: " + e.getMessage());
            throw new DatabaseException("Unable to load the shipments.");
        }
        return shipments;
    }

    @Override
    public ArrayList<Shipment> findByCustomer(ObjectId customerId) throws DatabaseException {
        ArrayList<Shipment> shipments;

        shipments = new ArrayList<>();
        try {
            for (Document doc : collection.find(new Document("customerId", customerId))
                    .sort(new Document("createdAt", -1))) {
                shipments.add(toShipment(doc));
            }
            LOGGER.info("SELECT shipments by customer: " + customerId);
        } catch (MongoException e) {
            LOGGER.severe("Error in SELECT shipments by customer: " + e.getMessage());
            throw new DatabaseException("Unable to load the shipments.");
        }
        return shipments;
    }

    @Override
    public boolean existsInProgressByCustomer(ObjectId customerId) throws DatabaseException {
        boolean exists;

        // TODO: filter new Document("customerId", customerId)
        //           .append("status", new Document("$in", Arrays.asList("IN_PROGRESS", "IN_SHIPMENT")))
        //       exists = collection.find(filter).first() != null
        //       (try/catch MongoException + log, like the other methods)
        exists = false;
        return exists;
    }

    @Override
    public boolean updateStatus(ObjectId id, ShipmentStatus status) throws DatabaseException {
        Document update;
        UpdateResult result;
        boolean updated;


        update = new Document("$set", new Document("status", status.name()));
        try {
            result = collection.updateOne(new Document("_id", id), update);
            updated = result.getMatchedCount() > 0;
            LOGGER.info("UPDATE shipments status: " + id + " -> " + status);
        } catch (MongoException e) {
            LOGGER.severe("Error in UPDATE shipments status: " + e.getMessage());
            throw new DatabaseException("Unable to change the status of the shipment.");
        }
        return updated;
    }

    /**
     * Converts a document of MongoDB into a Shipment (like reading a row of a
     * ResultSet).
     *
     * @param doc document read from the collection
     * @return the shipment
     */
    private Shipment toShipment(Document doc) {
        Shipment shipment;
        ArrayList<ShipmentLine> lines;
        ShipmentLine line;

        shipment = new Shipment();
        shipment.setId(doc.getObjectId("_id"));
        shipment.setCustomerId(doc.getObjectId("customerId"));
        shipment.setCreatedAt(doc.getDate("createdAt"));
        shipment.setAddress(doc.getString("address"));
        shipment.setTotal(doc.getDouble("total"));
        shipment.setTrackingId(doc.getString("trackingId"));
        shipment.setShipmentCompany(doc.getString("shipmentCompany"));
        shipment.setStatus(ShipmentStatus.valueOf(doc.getString("status")));

        lines = new ArrayList<>();
        for (Document lineDoc : doc.getList("lines", Document.class)) {
            line = new ShipmentLine();
            line.setProductId(lineDoc.getObjectId("productId"));
            line.setName(lineDoc.getString("name"));
            line.setPrice(lineDoc.getDouble("price"));
            line.setUnits(lineDoc.getInteger("units"));
            lines.add(line);
        }
        shipment.setLines(lines);
        return shipment;
    }

    /**
     * Converts a Shipment into a document to save it in MongoDB.
     *
     * @param shipment shipment to convert
     * @return the document with every field of the shipment
     */
    private Document toDocument(Shipment shipment) {
        Document doc;
        ArrayList<Document> lines;

        lines = new ArrayList<>();
        for (ShipmentLine line : shipment.getLines()) {
            lines.add(new Document("productId", line.getProductId())
                    .append("name", line.getName())
                    .append("price", line.getPrice())
                    .append("units", line.getUnits()));
        }

        doc = new Document();

        if (shipment.getId() != null) {
            doc.append("_id", shipment.getId());
        }
        doc.append("customerId", shipment.getCustomerId());
        doc.append("createdAt", shipment.getCreatedAt());
        doc.append("address", shipment.getAddress());
        doc.append("lines", lines);
        doc.append("total", shipment.getTotal());
        doc.append("status", shipment.getStatus().name());

        if (shipment.getTrackingId() != null) {
            doc.append("trackingId", shipment.getTrackingId());
        }
        if (shipment.getShipmentCompany() != null) {
            doc.append("shipmentCompany", shipment.getShipmentCompany());
        }
        return doc;
    }
}
