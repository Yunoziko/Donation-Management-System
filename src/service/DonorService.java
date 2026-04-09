package service;

import model.Donor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DonorService - Service/Business Logic Layer
 * Manages all CRUD operations on Donor objects.
 * Uses ArrayList as in-memory data store.
 */
public class DonorService {

    // In-memory list acting as the data store
    private List<Donor> donors = new ArrayList<>();
    private int idCounter = 1; // Auto-increment donor ID

    // -------------------- Add Donor --------------------

    /**
     * Adds a new donor after validating input.
     * @return true if added successfully, false if validation fails.
     */
    public boolean addDonor(String name, String contact, String email) {
        // Input Validation
        if (name == null || name.trim().isEmpty()) return false;
        if (contact == null || contact.trim().isEmpty()) return false;
        if (!contact.matches("\\d{10}")) return false; // Must be 10-digit number
        if (email == null || !email.contains("@")) return false;

        Donor donor = new Donor(idCounter++, name.trim(), contact.trim(), email.trim());
        donors.add(donor);
        return true;
    }

    // -------------------- Get All Donors --------------------

    /**
     * Returns a copy of the full donor list.
     */
    public List<Donor> getAllDonors() {
        return new ArrayList<>(donors);
    }

    // -------------------- Search Donor by ID --------------------

    /**
     * Finds and returns a donor by their ID.
     */
    public Optional<Donor> getDonorById(int id) {
        return donors.stream().filter(d -> d.getId() == id).findFirst();
    }

    // -------------------- Delete Donor --------------------

    /**
     * Deletes a donor by ID.
     * @return true if donor found and deleted, false otherwise.
     */
    public boolean deleteDonor(int id) {
        return donors.removeIf(d -> d.getId() == id);
    }

    // -------------------- Update Donor --------------------

    /**
     * Updates the name, contact, and email of an existing donor.
     * @return true if updated successfully, false if not found or validation fails.
     */
    public boolean updateDonor(int id, String name, String contact, String email) {
        Optional<Donor> found = getDonorById(id);
        if (!found.isPresent()) return false;

        // Input Validation
        if (name == null || name.trim().isEmpty()) return false;
        if (contact == null || !contact.matches("\\d{10}")) return false;
        if (email == null || !email.contains("@")) return false;

        Donor donor = found.get();
        donor.setName(name.trim());
        donor.setContact(contact.trim());
        donor.setEmail(email.trim());
        return true;
    }

    // -------------------- Donor Count --------------------

    public int getDonorCount() {
        return donors.size();
    }
}
