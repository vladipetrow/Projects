package com.example.workproject1.repositories.mysql;

import com.example.workproject1.coreServices.models.SubscriptionStatus;
import com.example.workproject1.repositories.SubscriptionRepository;
import com.example.workproject1.repositories.models.AgencyDAO;
import com.example.workproject1.repositories.models.SubscriptionDAO;
import com.example.workproject1.repositories.models.UserDAO;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.*;
import java.util.List;
import java.util.Objects;

import static com.example.workproject1.repositories.mysql.MySQLSubscriptionRepository.Queries.INSERT_SUBSCRIPTION;

public class MySQLSubscriptionRepository implements SubscriptionRepository {
    private final TransactionTemplate txTemplate;
    private final JdbcTemplate jdbc;

    public MySQLSubscriptionRepository(TransactionTemplate txTemplate, JdbcTemplate jdbc) {
        this.txTemplate = txTemplate;
        this.jdbc = jdbc;
    }

    public SubscriptionDAO createSubscription(int user_id, int agency_id, Timestamp expires_at, String invoice_id, double price) {
        return txTemplate.execute(status -> {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            if (user_id != 0) {
                jdbc.update(conn -> {
                    PreparedStatement ps = conn.prepareStatement(
                            INSERT_SUBSCRIPTION, Statement.RETURN_GENERATED_KEYS);
                    ps.setObject(1, user_id);
                    ps.setObject(2, null);
                    ps.setTimestamp(3, expires_at);
                    ps.setString(4, invoice_id); // Add invoice ID
                    ps.setString(5, SubscriptionStatus.PENDING.name()); // Initial status
                    ps.setDouble(6, price); // Add price
                    return ps;
                }, keyHolder);
            } else {
                jdbc.update(conn -> {
                    PreparedStatement ps = conn.prepareStatement(
                            INSERT_SUBSCRIPTION, Statement.RETURN_GENERATED_KEYS);
                    ps.setObject(1, null);
                    ps.setObject(2, agency_id);
                    ps.setTimestamp(3, expires_at);
                    ps.setString(4, invoice_id); // Add invoice ID
                    ps.setString(5, SubscriptionStatus.PENDING.name()); // Initial status
                    ps.setDouble(6, price); // Add price
                    return ps;
                }, keyHolder);
            }

            int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
            return new SubscriptionDAO.Builder()
                    .id(id)
                    .userId(user_id)
                    .agencyId(agency_id)
                    .expirationDate(expires_at)
                    .chargeId(invoice_id)
                    .paymentStatus(SubscriptionStatus.PENDING.name())
                    .price(price)
                    .build();
        });
    }

    @Override
    public SubscriptionDAO getSubscriptionId(int id) {
        return jdbc.queryForObject(Queries.GET_SUBSCRIPTION_BY_ID, (rs, rowNum) -> fromResultSet(rs), id);
    }

    @Override
    public List<AgencyDAO> listSubscribedAgencyByID(int agencyId) {
        return jdbc.query(Queries.GET_AGENCY_SUBSCRIPTION, (rs, rowNum) -> fromResultSetAgency(rs), agencyId);
    }

    @Override
    public List<UserDAO> listSubscribedUserByID(int userId) {
        return jdbc.query(Queries.GET_USER_SUBSCRIPTION, (rs, rowNum) -> fromResultSetUser(rs), userId);
    }

    @Override
    public String getEmailByRoleAndId(int id, String role) {
        if ("ROLE_USER".equals(role)) {
            return jdbc.queryForObject(
                    "SELECT email FROM USERS WHERE id = ?",
                    String.class,
                    id
            );
        } else if ("ROLE_AGENCY".equals(role)) {
            return jdbc.queryForObject(
                    "SELECT email FROM AGENCY WHERE id = ?",
                    String.class,
                    id
            );
        } else {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
    }

    @Override
    public Timestamp getUserExpirationDate(int userId) {
        try {
            return jdbc.queryForObject(Queries.GET_USER_EXPIRATION_DATE, (rs, rowNum) -> fromResultSetTime(rs), userId);
        } catch (EmptyResultDataAccessException e) {
            // User has no subscription
            return null;
        }
    }

    @Override
    public Timestamp getAgencyExpirationDate(int agencyId) {
        try {
            return jdbc.queryForObject(Queries.GET_AGENCY_EXPIRATION_DATE, (rs, rowNum) -> fromResultSetTime(rs), agencyId);
        } catch (EmptyResultDataAccessException e) {
            // Agency has no subscription
            return null;
        }
    }

    @Override
    public void deleteSubscription(int id) {
        txTemplate.execute(status -> {
            jdbc.update(Queries.DELETE_SUBSCRIPTIONS, id);
            return null;
        });
    }

    @Override
    public void updateSubscriptionStatus(String invoiceId, SubscriptionStatus status) {
        jdbc.update(
                "UPDATE SUBSCRIPTIONS SET payment_status = ? WHERE charge_id = ?",
                status.name(),
                invoiceId
        );
    }

    @Override
    public void updateSubscriptionExpiration(String chargeId, Timestamp expirationDate) {
        jdbc.update(
                "UPDATE SUBSCRIPTIONS SET expires_at = ? WHERE charge_id = ?",
                expirationDate,
                chargeId
        );
    }

    @Override
    public void updateExistingSubscription(int userId, int agencyId, Timestamp expirationDate, String chargeId, double price) {
        if (userId > 0) {
            jdbc.update(
                    "UPDATE SUBSCRIPTIONS SET expires_at = ?, charge_id = ?, payment_status = ?, price = ? WHERE user_id = ?",
                    expirationDate, chargeId, "PENDING", price, userId
            );
        } else {
            jdbc.update(
                    "UPDATE SUBSCRIPTIONS SET expires_at = ?, charge_id = ?, payment_status = ?, price = ? WHERE agency_id = ?",
                    expirationDate, chargeId, "PENDING", price, agencyId
            );
        }
    }

    @Override
    public SubscriptionDAO getSubscriptionByUserOrAgency(int userId, int agencyId) {
        if (userId > 0) {
            return jdbc.queryForObject(
                    "SELECT * FROM SUBSCRIPTIONS WHERE user_id = ?",
                    (rs, rowNum) -> fromResultSet(rs),
                    userId
            );
        } else {
            return jdbc.queryForObject(
                    "SELECT * FROM SUBSCRIPTIONS WHERE agency_id = ?",
                    (rs, rowNum) -> fromResultSet(rs),
                    agencyId
            );
        }
    }

    @Override
    public boolean hasUserSubscription(int userId) {
        try {
            Integer count = jdbc.queryForObject(
                    "SELECT COUNT(*) FROM SUBSCRIPTIONS WHERE user_id = ?",
                    Integer.class,
                    userId
            );
            return count != null && count > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean hasAgencySubscription(int agencyId) {
        try {
            Integer count = jdbc.queryForObject(
                    "SELECT COUNT(*) FROM SUBSCRIPTIONS WHERE agency_id = ?",
                    Integer.class,
                    agencyId
            );
            return count != null && count > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public SubscriptionDAO findByInvoiceId(String invoiceId) {
        return jdbc.queryForObject(
                "SELECT * FROM SUBSCRIPTIONS WHERE charge_id = ?",
                (rs, rowNum) -> fromResultSet(rs),
                invoiceId
        );
    }

    @Override
    public List<SubscriptionDAO> getAllActiveSubscriptions() {
        return jdbc.query(
                "SELECT * FROM subscriptions WHERE payment_status = 'ACTIVE' AND expires_at IS NOT NULL AND expires_at > NOW()",
                (rs, rowNum) -> fromResultSet(rs)
        );
    }

    private AgencyDAO fromResultSetAgency(ResultSet rs) throws SQLException {
        return new AgencyDAO.Builder()
                .id(rs.getInt("id"))
                .agencyName(rs.getString("name_of_agency"))
                .email(rs.getString("email"))
                .phoneNumber(rs.getString("phone_number"))
                .address(rs.getString("address"))
                .build();
    }

    private UserDAO fromResultSetUser(ResultSet rs) throws SQLException {
        return new UserDAO.Builder()
                .id(rs.getInt("id"))
                .firstName(rs.getString("firstName"))
                .lastName(rs.getString("lastName"))
                .email(rs.getString("email"))
                .build();
    }

    private SubscriptionDAO fromResultSet(ResultSet rs) throws SQLException {
        return new SubscriptionDAO.Builder()
                .id(rs.getInt("id"))
                .userId(rs.getInt("user_id"))
                .agencyId(rs.getInt("agency_id"))
                .expirationDate(rs.getTimestamp("expires_at"))
                .chargeId(rs.getString("charge_id"))
                .paymentStatus(rs.getString("payment_status"))
                .price(rs.getDouble("price"))
                .subscriptionTier(rs.getString("subscription_tier"))
                .createdAt(rs.getTimestamp("created_at"))
                .updatedAt(rs.getTimestamp("updated_at"))
                .build();
    }

    private Timestamp fromResultSetTime(ResultSet rs) throws SQLException {
        return rs.getTimestamp("expires_at");
    }

    static class Queries {
        public static final String GET_USER_EXPIRATION_DATE =
                "SELECT expires_at FROM subscriptions WHERE user_id = ? AND expires_at IS NOT NULL";

        public static final String GET_AGENCY_EXPIRATION_DATE =
                "SELECT expires_at FROM subscriptions WHERE agency_id = ? AND expires_at IS NOT NULL";

        public static final String INSERT_SUBSCRIPTION =
                "INSERT INTO subscriptions (user_id, agency_id, expires_at, charge_id, payment_status, price) VALUES (?, ?, ?, ?, ?, ?)";

        public static final String GET_SUBSCRIPTION_BY_ID =
                "SELECT * FROM subscriptions WHERE id = ?";

        public static final String GET_USER_SUBSCRIPTION =
                "SELECT u.id, u.first_name, u.last_name, u.email, s.expires_at FROM users u JOIN subscriptions s ON u.id = s.user_id WHERE s.user_id = ? AND s.expires_at IS NOT NULL";

        public static final String GET_AGENCY_SUBSCRIPTION =
                "SELECT a.id, a.name_of_agency, a.email, a.phone_number, a.address FROM agency a JOIN subscriptions s ON a.id = s.agency_id WHERE s.agency_id = ? AND s.expires_at IS NOT NULL";

        public static final String DELETE_SUBSCRIPTIONS =
                "DELETE FROM subscriptions WHERE id = ?";
    }
}

