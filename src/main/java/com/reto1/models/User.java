package com.reto1.models;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.types.ObjectId;

/**
 * Common data of every person that can log in (collection "users").
 * Customers and workers are stored in the same collection; the field "_t"
 * (discriminator) says which subclass each document belongs to.
 *
 * @author Ekaitz.Rivero
 */
@BsonDiscriminator(key = "_t")
public class User {

    /** MongoDB _id. The POJO codec maps the property "id" to "_id". */
    private ObjectId id;
    private String name;
    private String email;
    /** Password hash (never the plain password; hashing is added in Fase 8). */
    private String password;
    /** DNI / NIE / CIF of the user. */
    private String idDocument;

    public User() {
        this.name = "";
        this.email = "";
        this.password = "";
        this.idDocument = "";
    }

    public User(String name, String email, String password, String idDocument) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.idDocument = idDocument;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdDocument() {
        return idDocument;
    }

    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", name=" + name + ", email=" + email + ", idDocument=" + idDocument + '}';
    }
}
