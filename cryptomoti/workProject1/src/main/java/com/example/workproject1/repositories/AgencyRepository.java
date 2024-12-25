package com.example.workproject1.repositories;

import com.example.workproject1.repositories.models.AgencyDAO;
import java.util.List;

public interface AgencyRepository {
    AgencyDAO createAgency(String name_of_agency, String email, String passwordHash, String salt, String phone_number, String address);
    AgencyDAO getAgencyByEmailAndPassword(String email, String password);
    AgencyDAO getAgencyByEmail(String email);
    AgencyDAO getAgency(int id);
    String getEmail(int id);
    List<AgencyDAO> listAgency(int page, int pageSize);
    void deleteAgency(int id);
    void updatePassword(int userId, String password);
}
