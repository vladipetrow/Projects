package com.example.workproject1.repositories;

import com.example.workproject1.repositories.models.SubscriptionTierDAO;
import java.util.List;

public interface SubscriptionTierRepository {
    List<SubscriptionTierDAO> getAllTiers();
    List<SubscriptionTierDAO> getTiersByType(String tierType);
    SubscriptionTierDAO getTierByName(String tierName);
    SubscriptionTierDAO getTierById(int id);
}



