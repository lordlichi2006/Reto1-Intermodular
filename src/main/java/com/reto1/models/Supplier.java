/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.reto1.models;

import org.bson.types.ObjectId;

/**
 *
 * @author 20ala
 */
public class Supplier {
    private ObjectId id;
    private String name;
    private String cif;
    private String email;
    private String telephone;

    public Supplier() {
    }

    public Supplier(String name, String cif, String email, String telephone) {
        this.name = name;
        this.cif = cif;
        this.email = email;
        this.telephone = telephone;
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

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public String toString() {
        return "Supplier{" + "name=" + name + ", cif=" + cif + ", email=" + email + ", telephone=" + telephone + '}';
    }
    
    
}
