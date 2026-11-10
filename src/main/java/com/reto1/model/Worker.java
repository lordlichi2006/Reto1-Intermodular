package com.reto1.model;

/**
 * Employee of the shop (stored in "users" with userType = "worker").
 * The role decides which management windows can be opened.
 *
 * @author Ekaitz.Rivero
 */
public class Worker extends User {

    private Role role;

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
        return "Worker{" + super.toString() + ", role=" + role + '}';
    }
}
