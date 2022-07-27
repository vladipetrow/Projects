package com.example.workproject1.security;

public class EmailWithPassword {
    public String id;
    public String token;

    public EmailWithPassword(String id, String token) {
        this.id = id;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String email) {
        this.id = email;
    }

}
