package com.reto1.models;

/**
 *
 * @author Ekaitz.Rivero
 */
public class Worker extends User{
   Role role;

    public Worker() {
        super();
    }

    public Worker(String name, String email, String password, String idDocument, Role role) {
        super(name, email, password, idDocument);
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Worker{" + "role=" + role + '}';
    }
   
}
