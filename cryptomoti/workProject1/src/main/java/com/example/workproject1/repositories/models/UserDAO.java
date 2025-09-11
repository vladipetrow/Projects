package com.example.workproject1.repositories.models;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * Data Access Object for User entities.
 * Immutable data transfer object with Builder pattern.
 */
public final class UserDAO {
    private final Integer id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String passwordHash;
    private final String salt;
    private final Integer userSubscribed;
    private final Timestamp createdAt;

    private UserDAO(Builder builder) {
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.passwordHash = builder.passwordHash;
        this.salt = builder.salt;
        this.userSubscribed = builder.userSubscribed;
        this.createdAt = builder.createdAt;
    }

    // Getters only - no setters for immutability
    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
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

    public Integer getUserSubscribed() {
        return userSubscribed;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    /**
     * Gets the user's full name.
     * 
     * @return concatenated first and last name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Checks if the user has a valid email.
     * 
     * @return true if email is not null and not empty
     */
    public boolean hasValidEmail() {
        return email != null && !email.trim().isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDAO)) return false;
        UserDAO userDAO = (UserDAO) o;
        return Objects.equals(id, userDAO.id) && 
               Objects.equals(firstName, userDAO.firstName) &&
               Objects.equals(lastName, userDAO.lastName) && 
               Objects.equals(email, userDAO.email) &&
               Objects.equals(passwordHash, userDAO.passwordHash) && 
               Objects.equals(salt, userDAO.salt) &&
               Objects.equals(userSubscribed, userDAO.userSubscribed) &&
               Objects.equals(createdAt, userDAO.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, passwordHash, salt, userSubscribed, createdAt);
    }

    @Override
    public String toString() {
        return "UserDAO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", userSubscribed=" + userSubscribed +
                ", createdAt=" + createdAt +
                '}';
    }

    /**
     * Builder pattern for creating UserDAO instances.
     * Follows Effective Java Item 2 - Builder pattern for many parameters.
     */
    public static class Builder {
        private Integer id;
        private String firstName;
        private String lastName;
        private String email;
        private String passwordHash;
        private String salt;
        private Integer userSubscribed;
        private Timestamp createdAt;

        public Builder() {}

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
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

        public Builder userSubscribed(Integer userSubscribed) {
            this.userSubscribed = userSubscribed;
            return this;
        }

        public Builder createdAt(Timestamp createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public UserDAO build() {
            return new UserDAO(this);
        }
    }
}