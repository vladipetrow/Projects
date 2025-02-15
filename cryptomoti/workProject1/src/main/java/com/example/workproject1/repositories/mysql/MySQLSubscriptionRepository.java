package com.example.workproject1.repositories.mysql;

import com.example.workproject1.coreServices.models.SubscriptionStatus;
import com.example.workproject1.repositories.SubscriptionRepository;
import com.example.workproject1.repositories.models.AgencyDAO;
import com.example.workproject1.repositories.models.SubscriptionDAO;
import com.example.workproject1.repositories.models.UserDAO;
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

    public SubscriptionDAO createSubscription(int user_id, int agency_id, Timestamp expires_at, String invoice_id) {
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
                    return ps;
                }, keyHolder);
            }

            int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
            return new SubscriptionDAO(id, user_id, agency_id, expires_at, invoice_id, SubscriptionStatus.PENDING.name());
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
        return jdbc.queryForObject(Queries.GET_USER_EXPIRATION_DATE, (rs, rowNum) -> fromResultSetTime(rs), userId);
    }

    @Override
    public Timestamp getAgencyExpirationDate(int agencyId) {
        return jdbc.queryForObject(Queries.GET_AGENCY_EXPIRATION_DATE, (rs, rowNum) -> fromResultSetTime(rs), agencyId);
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
                "UPDATE SUBSCRIPTIONS SET btc_status = ? WHERE invoice_id = ?",
                status.name(),
                invoiceId
        );
    }

    @Override
    public SubscriptionDAO findByInvoiceId(String invoiceId) {
        return jdbc.queryForObject(
                "SELECT * FROM SUBSCRIPTIONS WHERE invoice_id = ?",
                (rs, rowNum) -> fromResultSet(rs),
                invoiceId
        );
    }

    private AgencyDAO fromResultSetAgency(ResultSet rs) throws SQLException {
        return new AgencyDAO(
                rs.getInt("id"),
                rs.getString("name_of_agency"),
                rs.getString("email"),
                rs.getString("phone_number"),
                rs.getString("address")
        );
    }

    private UserDAO fromResultSetUser(ResultSet rs) throws SQLException {
        return new UserDAO(
                rs.getInt("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email")
        );
    }

    private SubscriptionDAO fromResultSet(ResultSet rs) throws SQLException {
        return new SubscriptionDAO(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getInt("agency_id"),
                rs.getTimestamp("expires_at"),
                rs.getString("btc_status"),
                rs.getString("invoice_id")
        );
    }

    private Timestamp fromResultSetTime(ResultSet rs) throws SQLException {
        return rs.getTimestamp("expires_at");
    }

    static class Queries {
        public static final String GET_USER_EXPIRATION_DATE =
                "SELECT expires_at FROM subscriptions WHERE user_id = ?";

        public static final String GET_AGENCY_EXPIRATION_DATE =
                "SELECT expires_at FROM subscriptions WHERE agency_id = ?";

        public static final String INSERT_SUBSCRIPTION =
                "INSERT INTO subscriptions (user_id, agency_id, expires_at) VALUES (?, ?, ?)";

        public static final String GET_SUBSCRIPTION_BY_ID =
                "SELECT * FROM subscriptions WHERE id = ?";

        public static final String GET_USER_SUBSCRIPTION =
                "SELECT u.id, u.first_name, u.last_name, u.email, s.expires_at FROM users u JOIN subscriptions s ON u.id = s.user_id WHERE s.user_id = ?";

        public static final String GET_AGENCY_SUBSCRIPTION =
                "SELECT a.id, a.nameOf_agency, a.email, a.phone_number, a.address FROM agency a JOIN subscriptions s ON a.id = s.agency_id WHERE s.agency_id = ?";

        public static final String DELETE_SUBSCRIPTIONS =
                "DELETE FROM subscriptions WHERE id = ?";
    }
}

