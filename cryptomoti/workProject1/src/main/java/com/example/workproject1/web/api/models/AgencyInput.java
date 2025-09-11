package com.example.workproject1.web.api.models;

/**
 * Input model for agency registration and updates.
 * Simple POJO for JSON deserialization.
 */
public class AgencyInput {
    private String agencyName;
    private String email;
    private String password;
    private String phoneNumber;
    private String address;
    private String description;
    private String website;

    // Default constructor for JSON deserialization
    public AgencyInput() {
    }

    // Getters
    public String getAgencyName() {
        return agencyName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public String getWebsite() {
        return website;
    }

    // Setters
    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public String toString() {
        return "AgencyInput{" +
                "agencyName='" + agencyName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", website='" + website + '\'' +
                '}';
    }
}

