package com.example.workproject1.coreServices;

import com.example.workproject1.coreServices.models.SubscriptionTier;
import com.example.workproject1.repositories.SubscriptionTierRepository;
import com.example.workproject1.repositories.models.SubscriptionTierDAO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionTierService {
    private final SubscriptionTierRepository subscriptionTierRepository;

    public SubscriptionTierService(SubscriptionTierRepository subscriptionTierRepository) {
        this.subscriptionTierRepository = subscriptionTierRepository;
    }

    public List<SubscriptionTier> getAllTiers() {
        return subscriptionTierRepository.getAllTiers()
                .stream()
                .map(Mappers::fromSubscriptionTierDAO)
                .collect(Collectors.toList());
    }

    public List<SubscriptionTier> getTiersByType(String tierType) {
        return subscriptionTierRepository.getTiersByType(tierType)
                .stream()
                .map(Mappers::fromSubscriptionTierDAO)
                .collect(Collectors.toList());
    }

    public SubscriptionTier getTierByName(String tierName) {
        SubscriptionTierDAO tierDAO = subscriptionTierRepository.getTierByName(tierName);
        return Mappers.fromSubscriptionTierDAO(tierDAO);
    }

    public SubscriptionTier getTierById(int id) {
        SubscriptionTierDAO tierDAO = subscriptionTierRepository.getTierById(id);
        return Mappers.fromSubscriptionTierDAO(tierDAO);
    }
}



