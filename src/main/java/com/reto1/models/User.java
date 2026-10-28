/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.reto1.models;

import org.bson.types.ObjectId;

/**
 *
 * @author Ekaitz.Rivero
 */
public class User {

    private ObjectId id;
    private String name;
    private String email;
    private String password;
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
        return "User{" + "name=" + name + ", email=" + email + ", password=" + password + ", idDocument=" + idDocument + '}';
    }

}
