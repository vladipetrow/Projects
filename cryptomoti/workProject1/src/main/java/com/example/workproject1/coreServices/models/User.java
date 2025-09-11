package com.example.workproject1.coreServices.models;

import java.sql.Timestamp;
import java.util.Objects;

public class User {
    private final Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String passwordHash;
    private String salt;
    private Integer userSubscribed;
    private Timestamp createdAt;

    public User(Integer id, String firstName, String lastName, String email, String passwordHash, String salt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.salt = salt;
    }

    public User(Integer id, String firstName, String lastName, String email, String passwordHash, String salt, 
                Integer userSubscribed, Timestamp createdAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.userSubscribed = userSubscribed;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public Integer getUserSubscribed() {
        return userSubscribed;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(firstName, user.firstName)
                && Objects.equals(lastName, user.lastName) && Objects.equals(email, user.email)
                && Objects.equals(passwordHash, user.passwordHash) && Objects.equals(salt, user.salt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, passwordHash, salt);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}