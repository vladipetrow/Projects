package com.example.workproject1.coreServices;

import com.example.workproject1.repositories.UserRepository;
import com.example.workproject1.repositories.AgencyRepository;
import com.example.workproject1.repositories.models.AgencyDAO;
import com.example.workproject1.repositories.models.UserDAO;
import com.example.workproject1.security.PasswordUtil;
import com.google.api.client.util.Value;
import org.springframework.stereotype.Service;

@Service
public class PasswordResetService {

    private final UserRepository userRepository;
    private final AgencyRepository agencyRepository;
    @Value("${passwordPeperUser}")
    private static String PEPPER_USER;
    @Value("${passwordPeperAgency}")
    private static String PEPPER_AGENCY;

    public PasswordResetService(UserRepository userRepository, AgencyRepository agencyRepository) {
        this.userRepository = userRepository;
        this.agencyRepository = agencyRepository;
    }

    /**
     * Reset password for a user or agency by email.
     */
    public void resetPassword(String email, String newPassword) {
        // Check if the email exists in the `users` table
        UserDAO user = userRepository.getUserByEmail(email);
        if (user != null) {
            updateUserPassword(user.id, user.salt, newPassword);
            return;
        }

        // Check if the email exists in the `agencies` table
        AgencyDAO agency = agencyRepository.getAgencyByEmail(email);
        if (agency != null) {
            updateAgencyPassword(agency.id, agency.salt, newPassword);
            return;
        }

        throw new IllegalArgumentException("Email not found in users or agencies.");
    }

    /**
     * Update the password for a user.
     */
    private void updateUserPassword(int userId, String salt, String newPassword) {
        String hashedPassword = PasswordUtil.sha256(salt + newPassword + PEPPER_USER);
        userRepository.updatePassword(userId, hashedPassword);
    }

    /**
     * Update the password for an agency.
     */
    private void updateAgencyPassword(int agencyId, String salt, String newPassword) {
        String hashedPassword = PasswordUtil.sha256(salt + newPassword + PEPPER_AGENCY);
        agencyRepository.updatePassword(agencyId, hashedPassword);
    }
}

