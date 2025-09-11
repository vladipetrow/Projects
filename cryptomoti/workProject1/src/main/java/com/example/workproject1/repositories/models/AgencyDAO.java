package com.example.workproject1.repositories.models;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * Data Access Object for Agency entities.
 * Immutable data transfer object with Builder pattern.
 */
public final class AgencyDAO {
    private final Integer id;
    private final String agencyName;
    private final String email;
    private final String passwordHash;
    private final String salt;
    private final String phoneNumber;
    private final String address;
    private final Integer agencySubscribed;
    private final Timestamp createdAt;

    private AgencyDAO(Builder builder) {
        this.id = builder.id;
        this.agencyName = builder.agencyName;
        this.email = builder.email;
        this.passwordHash = builder.passwordHash;
        this.salt = builder.salt;
        this.phoneNumber = builder.phoneNumber;
        this.address = builder.address;
        this.agencySubscribed = builder.agencySubscribed;
        this.createdAt = builder.createdAt;
    }

    // Getters only - no setters for immutability
    public Integer getId() {
        return id;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getSalt() {
        return salt;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public Integer getAgencySubscribed() {
        return agencySubscribed;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    /**
     * Checks if the agency has a valid email.
     * 
     * @return true if email is not null and not empty
     */
    public boolean hasValidEmail() {
        return email != null && !email.trim().isEmpty();
    }

    /**
     * Checks if the agency has a valid phone number.
     * 
     * @return true if phone number is not null and not empty
     */
    public boolean hasValidPhoneNumber() {
        return phoneNumber != null && !phoneNumber.trim().isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AgencyDAO)) return false;
        AgencyDAO agencyDAO = (AgencyDAO) o;
        return Objects.equals(id, agencyDAO.id) && 
               Objects.equals(agencyName, agencyDAO.agencyName) && 
               Objects.equals(email, agencyDAO.email) &&
               Objects.equals(passwordHash, agencyDAO.passwordHash) && 
               Objects.equals(salt, agencyDAO.salt) &&
               Objects.equals(phoneNumber, agencyDAO.phoneNumber) && 
               Objects.equals(address, agencyDAO.address) &&
               Objects.equals(agencySubscribed, agencyDAO.agencySubscribed) &&
               Objects.equals(createdAt, agencyDAO.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, agencyName, email, passwordHash, salt, phoneNumber, address, agencySubscribed, createdAt);
    }

    @Override
    public String toString() {
        return "AgencyDAO{" +
                "id=" + id +
                ", agencyName='" + agencyName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", agencySubscribed=" + agencySubscribed +
                ", createdAt=" + createdAt +
                '}';
    }

    /**
     * Builder pattern for creating AgencyDAO instances.
     * Follows Effective Java Item 2 - Builder pattern for many parameters.
     */
    public static class Builder {
        private Integer id;
        private String agencyName;
        private String email;
        private String passwordHash;
        private String salt;
        private String phoneNumber;
        private String address;
        private Integer agencySubscribed;
        private Timestamp createdAt;

        public Builder() {}

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder agencyName(String agencyName) {
            this.agencyName = agencyName;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder passwordHash(String passwordHash) {
            this.passwordHash = passwordHash;
            return this;
        }

        public Builder salt(String salt) {
            this.salt = salt;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder agencySubscribed(Integer agencySubscribed) {
            this.agencySubscribed = agencySubscribed;
            return this;
        }

        public Builder createdAt(Timestamp createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public AgencyDAO build() {
            return new AgencyDAO(this);
        }
    }
}

