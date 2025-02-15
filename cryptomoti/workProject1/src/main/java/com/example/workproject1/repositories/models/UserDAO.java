package com.example.workproject1.repositories.models;

import java.util.Objects;

public class UserDAO {
    private final Integer id;
    private String passwordHash;
    private String salt;
    private String firstName;
    private String lastName;
    private String email;

    public UserDAO(Integer id, String firstName, String lastName, String email, String passwordHash, String salt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.salt = salt;
    }

    public UserDAO(Integer id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
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

    public String getSalt() {
        return salt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDAO userDAO = (UserDAO) o;
        return Objects.equals(id, userDAO.id) && Objects.equals(firstName, userDAO.firstName)
                && Objects.equals(lastName, userDAO.lastName) && Objects.equals(email, userDAO.email)
                && Objects.equals(passwordHash, userDAO.passwordHash) && Objects.equals(salt, userDAO.salt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, passwordHash, salt);
    }

    @Override
    public String toString() {
        return "UserDAO{" +
                "id=" + id +
                ", first_name='" + firstName + '\'' +
                ", last_name='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", salt='" + salt + '\'' +
                '}';
    }
}