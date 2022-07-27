package com.example.workproject1.web.api.models;

public class AgencyInput {
    public String name_of_agency;
    public String email;
    public String passwordHash;
    public String phone_number;
    public String address;

    public AgencyInput(String name_of_agency, String email, String passwordHash, String phone_number, String address) {
        this.name_of_agency = name_of_agency;
        this.email = email;
        this.passwordHash = passwordHash;
        this.phone_number = phone_number;
        this.address = address;
    }
}
