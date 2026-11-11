
package com.reto1.model;

/**
 * Employee of the shop (stored in "users" with userType = "worker").
 * The role decides which management windows can be opened.
 *
 * @author Ekaitz.Rivero
 */
public class Worker extends User {

    private Role role;

    /**
     * Creates an empty worker by initializing the inherited user attributes.
     */
    public Worker() {
        super();
    }

    /**
     * Creates a worker with the specified personal information and role.
     *
     * @param name       the worker's name
     * @param email      the worker's email address
     * @param password   the worker's password
     * @param idDocument the worker's identification document (DNI, NIE or CIF)
     * @param role       the worker's assigned role
     */
    public Worker(String name, String email, String password, String idDocument, Role role) {
        super(name, email, password, idDocument);
        this.role = role;
    }

    /**
     * Returns the worker's assigned role.
     *
     * @return the worker's role, or {@code null} if no role has been assigned
     */
    public Role getRole() {
        return role;
    }

    /**
     * Sets the worker's assigned role.
     *
     * @param role the role to assign to the worker
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * Returns a string representation of the worker, including inherited user information
     * and the assigned role.
     *
     * @return a string containing the worker's user information and role
     */
    @Override
    public String toString() {
        return "Worker{" + super.toString() + ", role=" + role + '}';
    }
}
