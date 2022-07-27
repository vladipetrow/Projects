package com.example.workproject1.coreServices.models;

public class Agency {
    public Integer id;
    public String name_of_agency;
    public String email;
    public String passwordHash;
    public String salt;
    public String phone_number;
    public String address;

    public Agency(Integer id, String name_of_agency, String email, String passwordHash, String salt, String phone_number, String address) {
        this.id = id;
        this.name_of_agency = name_of_agency;
        this.email = email;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.phone_number = phone_number;
        this.address = address;
    }
}
