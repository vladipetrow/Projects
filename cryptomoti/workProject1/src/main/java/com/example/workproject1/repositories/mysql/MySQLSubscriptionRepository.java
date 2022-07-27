package com.example.workproject1.repositories.mysql;

import com.example.workproject1.repositories.SubscriptionRepository;
import com.example.workproject1.repositories.models.AgencyDAO;
import com.example.workproject1.repositories.models.SubscriptionDAO;
import com.example.workproject1.repositories.models.UserDAO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.*;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import static com.example.workproject1.repositories.mysql.MySQLSubscriptionRepository.Queries.*;

public class MySQLSubscriptionRepository implements SubscriptionRepository {
    private final TransactionTemplate txTemplate;
    private final JdbcTemplate jdbc;

    public MySQLSubscriptionRepository(TransactionTemplate txTemplate, JdbcTemplate jdbc) {
        this.txTemplate = txTemplate;
        this.jdbc = jdbc;
    }

    public SubscriptionDAO createSubscription(int user_id, int agency_id, Timestamp expires_at) {

        return txTemplate.execute(status -> {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            Timestamp timestamp = expires_at;

            Calendar expiration = Calendar.getInstance();

            expiration.setTime(timestamp);

            expiration.add(Calendar.DAY_OF_WEEK, 30);

            timestamp.setTime(expiration.getTime().getTime());

            if(user_id != 0) {
                jdbc.update(conn -> {
                    PreparedStatement ps = conn.prepareStatement(
                            INSERT_SUBSCRIPTION, Statement.RETURN_GENERATED_KEYS);
                    ps.setObject(1, user_id);
                    ps.setObject(2, null);
                    ps.setTimestamp(3, expires_at);
                    return ps;
                }, keyHolder);
            }else {
                jdbc.update(conn -> {
                    PreparedStatement ps = conn.prepareStatement(
                            INSERT_SUBSCRIPTION, Statement.RETURN_GENERATED_KEYS);
                    ps.setObject(1, null);
                    ps.setObject(2, agency_id);
                    ps.setTimestamp(3, expires_at);
                    return ps;
                }, keyHolder);
            }

            int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
            return new SubscriptionDAO(id, user_id, agency_id, timestamp);
        });
    }

    public SubscriptionDAO getSubscriptionId(int id) {
        return jdbc.queryForObject(GET_SUBSCRIPTION,
                (rs, rowNum) -> fromResultSet(rs), id);
    }

    public  List<AgencyDAO> listSubscribedAgencyByID(int agency_id) {
         return  jdbc.query(GET_AGENCY_SUBSCRIPTION,
                (rs, rowNum) -> fromResultSetAgency(rs), agency_id);
    }

    public List<UserDAO> listSubscribedUserByID(int user_id) {
        return jdbc.query(GET_USER_SUBSCRIPTION,
                (rs, rowNum) -> fromResultSetUser(rs), user_id);
    }

    public Timestamp getUserExpirationDate(int user_id){
        return jdbc.queryForObject(GET_USER_EXPIRATION_DATE,
                (rs, rowNum) -> fromResultSetTime(rs), user_id);
    }

    public Timestamp getAgencyExpirationDate(int agency_id){
        return jdbc.queryForObject(GET_AGENCY_EXPIRATION_DATE,
                (rs, rowNum) -> fromResultSetTime(rs), agency_id);
    }

    public void deleteSubscription(int id) {
        txTemplate.execute(status -> {
            jdbc.update(DELETE_SUBSCRIPTIONS, id);
            return null;
        });
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
                rs.getTimestamp("expires_at")
        );
    }
    private Timestamp fromResultSetTime(ResultSet rs) throws SQLException {
        return rs.getTimestamp("expires_at");
    }
    static class Queries {
        public static final String GET_USER_EXPIRATION_DATE = ""+
                "SELECT expires_at FROM subscriptions sb WHERE sb.user_id = ?";

        public static final String GET_AGENCY_EXPIRATION_DATE = ""+
                "SELECT expires_at FROM subscriptions sb WHERE sb.agency_id = ?";
        public static final String INSERT_SUBSCRIPTION =
                "INSERT INTO SUBSCRIPTIONS (user_id, agency_id, expires_at) VALUES (?, ?, ?)";

        public static final String GET_SUBSCRIPTION = "" +
                "SELECT \n" +
                "    p.id,\n" +
                "    p.user_id,\n" +
                "    p.agency_id,\n" +
                "    p.expires_at \n" +
                "FROM\n" +
                "    SUBSCRIPTIONS p \n" +
                "WHERE p.id = ?";
        public static final String GET_USER_SUBSCRIPTION = "" +
        "SELECT " +
        "p.id, \n" +
        "p.first_name, \n" +
        "p.last_name, \n" +
        "p.email, \n" +
        "sb.expires_at \n" +
        "FROM \n" +
        "Users p \n" +
        "Join SUBSCRIPTIONS sb on p.id = sb.user_id \n" +
        "WHERE sb.user_id = ?" ;

        public static final String GET_AGENCY_SUBSCRIPTION = "" +
         "SELECT \n" +
         "p.id, \n"+
         "p.name_of_agency, \n" +
         "p.email, \n" +
         "p.phone_number, \n" +
         "p.address \n"  +
         "FROM \n" +
         "AGENCY p \n" +
         "JOIN SUBSCRIPTIONS sb on p.id = sb.agency_id \n" +
         "WHERE sb.agency_id = ?";

        public static final String DELETE_SUBSCRIPTIONS = "DELETE FROM SUBSCRIPTIONS WHERE id = ?";
    }
}
