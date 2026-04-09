package model;

/**
 * Donation Model Class
 * Represents a donation transaction linked to a Donor.
 * Stores amount, date, and purpose of the donation.
 */
public class Donation {

    private int donationId;
    private int donorId;
    private double amount;
    private String date;         // Format: YYYY-MM-DD
    private String purpose;

    // Constructor
    public Donation(int donationId, int donorId, double amount, String date, String purpose) {
        this.donationId = donationId;
        this.donorId = donorId;
        this.amount = amount;
        this.date = date;
        this.purpose = purpose;
    }

    // -------------------- Getters --------------------

    public int getDonationId() {
        return donationId;
    }

    public int getDonorId() {
        return donorId;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getPurpose() {
        return purpose;
    }

    // -------------------- Setters --------------------

    public void setDonationId(int donationId) {
        this.donationId = donationId;
    }

    public void setDonorId(int donorId) {
        this.donorId = donorId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    // -------------------- Utility --------------------

    @Override
    public String toString() {
        return "Donation{donationId=" + donationId + ", donorId=" + donorId +
               ", amount=" + amount + ", date='" + date + "', purpose='" + purpose + "'}";
    }
}
