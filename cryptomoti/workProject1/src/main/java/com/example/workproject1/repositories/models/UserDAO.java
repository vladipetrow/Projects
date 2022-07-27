package com.example.workproject1.repositories.models;

public class UserDAO {
    public Integer id;
    public String first_name;
    public String last_name;

    public String email;
    public String passwordHash;
    public String salt;
    public UserDAO(Integer id, String first_name, String last_name, String email, String passwordHash, String salt) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.salt = salt;
    }

    public UserDAO(Integer id, String first_name, String last_name, String email) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
    }
}