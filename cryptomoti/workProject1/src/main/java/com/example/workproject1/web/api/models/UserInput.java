package com.example.workproject1.web.api.models;

import java.util.Objects;

public class UserInput {
    private final String firstName;
    private final String lastName;
    private final String email;
    private String passwordHash;

    public UserInput(String firstName, String lastName, String email, String passwordHash) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = passwordHash;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInput userInput = (UserInput) o;
        return Objects.equals(getFirstName(), userInput.getFirstName()) && Objects.equals(getLastName(),
                userInput.getLastName()) && Objects.equals(getEmail(), userInput.getEmail())
                && Objects.equals(getPasswordHash(), userInput.getPasswordHash());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getEmail(), getPasswordHash());
    }

    @Override
    public String toString() {
        return "UserInput{" +
                "first_name='" + firstName + '\'' +
                ", last_name='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + passwordHash + '\'' +
                '}';
    }
}
