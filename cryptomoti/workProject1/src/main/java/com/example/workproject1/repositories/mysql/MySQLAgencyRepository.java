package com.example.workproject1.repositories.mysql;

import com.example.workproject1.repositories.AgencyRepository;
import com.example.workproject1.repositories.models.AgencyDAO;
import com.example.workproject1.repositories.models.UserDAO;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import static com.example.workproject1.repositories.mysql.MySQLAgencyRepository.Queries.*;
import static com.example.workproject1.repositories.mysql.MySQLUserRepository.Queries.GET_EMAIL;

public class MySQLAgencyRepository implements AgencyRepository {

    private final TransactionTemplate txTemplate;
    private final JdbcTemplate jdbc;
    public MySQLAgencyRepository(TransactionTemplate txTemplate, JdbcTemplate jdbc) {
        this.txTemplate = txTemplate;
        this.jdbc = jdbc;
    }

    @Override
    public AgencyDAO createAgency(String name_of_agency, String email, String passwordHash, String salt, String phone_number, String address) {
        return txTemplate.execute(status -> {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbc.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(
                        INSERT_AGENCY, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, name_of_agency);
                ps.setString(2, email);
                ps.setString(3, passwordHash);
                ps.setString(4, salt);
                ps.setString(5, phone_number);
                ps.setString(6, address);
                return ps;
            }, keyHolder);

            Integer id = Objects.requireNonNull(keyHolder.getKey()).intValue();
//            jdbc.update(INSERT_USER, keyHolder.getKey(), role);
            return new AgencyDAO(id, name_of_agency, email, passwordHash, salt, phone_number, address);
        });
    }

    @Override
    public AgencyDAO getAgencyByEmailAndPassword(String email, String password) {
        System.out.println(email + password);
        return jdbc.queryForObject(LOGIN_AGENCY,
                (rs, rowNum) -> fromResultSet(rs), email, password);
    }

    @Override
    public AgencyDAO getAgencyByEmail(String email) {
        try {
            return jdbc.queryForObject(GET_AGENCY_BY_EMAIL,
                    (rs, rowNum) -> fromResultSet(rs), email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public String getEmail(int id) {
        return jdbc.queryForObject(GET_EMAIL, String.class, id);
    }

    @Override
    public AgencyDAO getAgency(int id) {
        return jdbc.queryForObject(GET_AGENCY,
                (rs, rowNum) -> fromResultSetListAgency(rs), id);
    }

    @Override
    public List<AgencyDAO> listAgency(int page, int pageSize) {
        return jdbc.query(LIST_AGENCY,
                (rs, rowNum) -> fromResultSetListAgency(rs), page*pageSize, pageSize);
    }

    @Override
    public void deleteAgency(int id) {
        txTemplate.execute(status -> {
            jdbc.update(DELETE_AGENCY, id);
            return null;
        });
    }

    public void updatePassword(int agencyId, String password) {
        jdbc.update("UPDATE agencies SET passwordHash = ? WHERE id = ?", password, agencyId);
    }

    private AgencyDAO fromResultSet(ResultSet rs) throws SQLException {
        return new AgencyDAO(
                rs.getInt("id"),
                rs.getString("name_of_agency"),
                rs.getString("email"),
                rs.getString("passwordHash"),
                rs.getString("salt"),
                rs.getString("phone_number"),
                rs.getString("address")
        );
    }
    private AgencyDAO fromResultSetListAgency(ResultSet rs) throws SQLException {
        return new AgencyDAO(
                rs.getInt("id"),
                rs.getString("name_of_agency"),
                rs.getString("email"),
                rs.getString("phone_number"),
                rs.getString("address")
        );
    }
    static class Queries {
        public static final String GET_AGENCY_BY_EMAIL = "SELECT * FROM Agency WHERE email = ?";
        public static final String LOGIN_AGENCY = "" +
                "SELECT \n" +
                " * " +
                "  FROM " +
                "    Agency p\n" +
                "WHERE p.email = ? AND p.passwordHash = ?";
        public static final String INSERT_AGENCY =
                "INSERT INTO Agency (name_of_agency, email, passwordHash, salt, phone_number, address) VALUES (?, ?, ?, ?, ?, ?)";
        public static final String UPDATE_PASSWORD_BY_EMAIL = "UPDATE users SET passwordHash = ? WHERE email = ?";
        public static final String GET_EMAIL = "SELECT email FROM Agency WHERE id = ?";

        public static final String GET_AGENCY = "" +
                "SELECT \n" +
                "    p.id,\n" +
                "    p.name_of_agency,\n" +
                "    p.email,\n" +
                "    p.phone_number,\n" +
                "    p.address \n" +
                "FROM\n" +
                "    Agency p \n" +
                "WHERE p.id = ?";
        public static final String LIST_AGENCY = "" +
                "SELECT \n" +
                "    p.id,\n" +
                "    p.name_of_agency,\n" +
                "    p.email,\n" +
                "    p.phone_number,\n" +
                "    p.address \n" +
                "FROM\n" +
                "    Agency p \n" +
                "LIMIT ?, ?";
        public static final String DELETE_AGENCY = "DELETE FROM Agency WHERE id = ?";
    }

}

