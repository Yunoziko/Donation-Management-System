package service;

import model.Donation;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DonationService - Service/Business Logic Layer
 * Manages all operations related to Donation records.
 * Provides methods to add, view, and generate reports on donations.
 */
public class DonationService {

    // In-memory store for donations
    private List<Donation> donations = new ArrayList<>();
    private int idCounter = 1; // Auto-increment donation ID

    // -------------------- Add Donation --------------------

    /**
     * Adds a new donation after validating input.
     * @param donorId  ID of the donor making the donation
     * @param amount   Donation amount (must be > 0)
     * @param date     Date in YYYY-MM-DD format
     * @param purpose  Purpose/note for the donation
     * @return true if added successfully, false on validation failure
     */
    public boolean addDonation(int donorId, double amount, String date, String purpose) {
        // Validation
        if (donorId <= 0) return false;
        if (amount <= 0) return false;
        if (date == null || date.trim().isEmpty()) return false;
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) return false; // YYYY-MM-DD format check
        if (purpose == null || purpose.trim().isEmpty()) return false;

        Donation donation = new Donation(idCounter++, donorId, amount, date.trim(), purpose.trim());
        donations.add(donation);
        return true;
    }

    // -------------------- Get All Donations --------------------

    /**
     * Returns a copy of all donations.
     */
    public List<Donation> getAllDonations() {
        return new ArrayList<>(donations);
    }

    // -------------------- Donations by Donor --------------------

    /**
     * Returns a list of all donations made by a specific donor.
     * @param donorId The donor's ID
     */
    public List<Donation> getDonationsByDonor(int donorId) {
        return donations.stream()
                .filter(d -> d.getDonorId() == donorId)
                .collect(Collectors.toList());
    }

    // -------------------- Total Amount --------------------

    /**
     * Returns the total sum of all donations.
     */
    public double getTotalDonationAmount() {
        return donations.stream()
                .mapToDouble(Donation::getAmount)
                .sum();
    }

    // -------------------- Total by Donor --------------------

    /**
     * Returns the total donation amount contributed by a specific donor.
     * @param donorId The donor's ID
     */
    public double getTotalByDonor(int donorId) {
        return donations.stream()
                .filter(d -> d.getDonorId() == donorId)
                .mapToDouble(Donation::getAmount)
                .sum();
    }

    // -------------------- Donation Count --------------------

    public int getDonationCount() {
        return donations.size();
    }
}
