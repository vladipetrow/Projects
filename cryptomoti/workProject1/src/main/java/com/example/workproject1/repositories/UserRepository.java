package com.example.workproject1.repositories;

import com.example.workproject1.repositories.models.SubscriptionDAO;
import com.example.workproject1.repositories.models.UserDAO;
import java.util.List;

public interface UserRepository {
    UserDAO createUser(String first_name, String last_name, String email, String password, String salt);
    UserDAO getUserByEmailAndPassword(String email, String password);
    UserDAO getUserByEmail(String email);
    UserDAO getUser(int id);
    List<UserDAO> listUsers(int page, int pageSize);
    void deleteUser(int id);
}
