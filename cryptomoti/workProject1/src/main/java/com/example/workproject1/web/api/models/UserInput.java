package com.example.workproject1.web.api.models;

public class UserInput {
    public final String first_name;
    public final String last_name;

    public final String email;
    public final String password;

    public UserInput(String first_name, String last_name, String email, String password) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
    }
}
