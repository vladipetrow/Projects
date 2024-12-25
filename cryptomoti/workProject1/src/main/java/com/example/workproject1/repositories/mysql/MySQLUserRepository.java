package com.example.workproject1.repositories.mysql;

import com.example.workproject1.repositories.UserRepository;
import com.example.workproject1.repositories.models.UserDAO;
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

import static com.example.workproject1.repositories.mysql.MySQLUserRepository.Queries.*;

public class MySQLUserRepository implements UserRepository {
    private final TransactionTemplate txTemplate;
    private final JdbcTemplate jdbc;
    public MySQLUserRepository(TransactionTemplate txTemplate, JdbcTemplate jdbc) {
        this.txTemplate = txTemplate;
        this.jdbc = jdbc;
    }

    public UserDAO createUser(
            String firstName, String lastName,
            String email, String passwordHash, String salt) {
        return txTemplate.execute(status -> {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbc.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(
                        INSERT_USER, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, firstName);
                ps.setString(2, lastName);
                ps.setString(3, email);
                ps.setString(4, passwordHash);
                ps.setString(5, salt);
                return ps;
            }, keyHolder);

            Integer id = Objects.requireNonNull(keyHolder.getKey()).intValue();
//            jdbc.update(INSERT_USER, keyHolder.getKey(), role);
            return new UserDAO(id, firstName, lastName, email, passwordHash, salt);
        });
    }

    @Override
    public UserDAO getUserByEmailAndPassword(String email, String passwordHash) {
            return jdbc.queryForObject(LOGIN_USER,
                    (rs, rowNum) -> fromResultSet(rs), email, passwordHash);
    }
    @Override
    public UserDAO getUserByEmail(String email) {
        return jdbc.queryForObject(GET_USER_BY_EMAIL,
                (rs, rowNum) -> fromResultSet(rs), email);
    }

    @Override
    public String getEmail(int id) {
        return jdbc.queryForObject(GET_EMAIL, String.class, id);
    }

    public UserDAO getUser(int id) {
        return jdbc.queryForObject(GET_USER,
                (rs, rowNum) -> fromResultSetListUsers(rs), id);
    }

    public List<UserDAO> listUsers(int page, int pageSize) {
        return jdbc.query(LIST_USERS,
                (rs, rowNum) -> fromResultSetListUsers(rs), page*pageSize, pageSize);
    }

    public void deleteUser(int id) {
        txTemplate.execute(status -> {
            jdbc.update(DELETE_USERS, id);
            return null;
        });
    }

    private UserDAO fromResultSet(ResultSet rs) throws SQLException {
        return new UserDAO(
                rs.getInt("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("passwordHash"),
                rs.getString("salt")
        );
    }
    private UserDAO fromResultSetListUsers(ResultSet rs) throws SQLException {
        return new UserDAO(
                rs.getInt("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email")
        );
    }

    static class Queries {
        public static final String GET_USER_BY_EMAIL = "SELECT * FROM users WHERE email = ?";
        public static final String LOGIN_USER = "" +
                "SELECT \n" +
                " p.id, p.first_name, p.last_name, p.email, p.passwordHash, p.salt" +
                "  FROM " +
                "    users p\n" +
                "WHERE p.email = ? AND p.passwordHash = ?";
        public static final String INSERT_USER =
                "INSERT INTO Users (first_name, last_name, email, passwordHash, salt) VALUES (?, ?, ?, ?, ?)";
        public static final String GET_EMAIL = "SELECT email FROM users WHERE id = ?";

        public static final String GET_USER = "" +
                "SELECT \n" +
                "    p.id,\n" +
                "    p.first_name,\n" +
                "    p.last_name,\n" +
                "    p.email,\n" +
                "    p.passwordHash \n" +
                "FROM\n" +
                "    users p \n" +
                "WHERE p.id = ?";
        public static final String LIST_USERS = "" +
                "SELECT \n" +
                "    si.id,\n" +
                "    si.first_name,\n" +
                "    si.last_name,\n" +
                "    si.email,\n" +
                "    si.passwordHash \n" +
                "FROM\n" +
                "    users si \n" +
                "LIMIT ?, ?";
        public static final String DELETE_USERS = "DELETE FROM users WHERE id = ?";
    }

}
