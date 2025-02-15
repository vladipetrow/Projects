package com.example.workproject1.coreServices.models;

import java.util.Objects;

public class Agency {
    private final Integer id;
    private String nameOfAgency;
    private String email;
    private String passwordHash;
    private String salt;
    private String phoneNumber;
    private String address;

    public Agency(Integer id, String nameOfAgency, String email, String passwordHash, String salt, String phoneNumber, String address) {
        this.id = id;
        this.nameOfAgency = nameOfAgency;
        this.email = email;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public String getNameOfAgency() {
        return nameOfAgency;
    }

    public void setNameOfAgency(String nameOfAgency) {
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

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
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
        Agency agency = (Agency) o;
        return Objects.equals(id, agency.id) && Objects.equals(nameOfAgency, agency.nameOfAgency)
                && Objects.equals(email, agency.email) && Objects.equals(passwordHash, agency.passwordHash)
                && Objects.equals(salt, agency.salt) && Objects.equals(phoneNumber, agency.phoneNumber) && Objects.equals(address, agency.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nameOfAgency, email, passwordHash, salt, phoneNumber, address);
    }

    @Override
    public String toString() {
        return "Agency{" +
                "id=" + id +
                ", name_of_agency='" + nameOfAgency + '\'' +
                ", email='" + email + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", salt='" + salt + '\'' +
                ", phone_number='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
