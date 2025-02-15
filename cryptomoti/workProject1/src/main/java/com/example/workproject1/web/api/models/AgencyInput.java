package com.example.workproject1.web.api.models;

import java.util.Objects;

public class AgencyInput {
    private String nameOfAgency;
    private String email;
    private String passwordHash;
    private String phoneNumber;
    private String address;

    public AgencyInput(String nameOfAgency, String email, String passwordHash, String phoneNumber, String address) {
        this.nameOfAgency = nameOfAgency;
        this.email = email;
        this.passwordHash = passwordHash;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public AgencyInput() {
    }

    public String getNameOfAgency() {
        return nameOfAgency;
    }

    public void setName0fAgency(String nameOfAgency) {
        this.nameOfAgency = nameOfAgency;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgencyInput that = (AgencyInput) o;
        return Objects.equals(nameOfAgency, that.nameOfAgency) && Objects.equals(email, that.email) && Objects.equals(passwordHash, that.passwordHash) && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameOfAgency, email, passwordHash, phoneNumber, address);
    }

    @Override
    public String toString() {
        return "AgencyInput{" +
                "name_of_agency='" + nameOfAgency + '\'' +
                ", email='" + email + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", phone_number='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
