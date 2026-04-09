package model;

/**
 * Donor Model Class
 * Represents a donor in the Donation Management System.
 * Encapsulates donor-related data using OOP principles.
 */
public class Donor {

    private int id;
    private String name;
    private String contact;
    private String email;

    // Constructor
    public Donor(int id, String name, String contact, String email) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.email = email;
    }

    // -------------------- Getters --------------------

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }

    public String getEmail() {
        return email;
    }

    // -------------------- Setters --------------------

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // -------------------- Utility --------------------

    @Override
    public String toString() {
        return "Donor{id=" + id + ", name='" + name + "', contact='" + contact + "', email='" + email + "'}";
    }
}
