package com.example.workproject1.repositories.mysql;

import java.sql.*;

import com.example.workproject1.coreServices.models.ApartmentType;
import com.example.workproject1.repositories.PostRepository;
import com.example.workproject1.repositories.models.PostDAO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Objects;

import static com.example.workproject1.repositories.mysql.MySQLPostRepository.Queries.*;

public class MySQLPostRepository implements PostRepository {
    private final TransactionTemplate txTemplate;
    private final JdbcTemplate jdbc;

    public MySQLPostRepository(TransactionTemplate txTemplate, JdbcTemplate jdbc) {
        this.txTemplate = txTemplate;
        this.jdbc = jdbc;
    }

    public PostDAO createPost(
            String location, int price, int area, String description, int user_id, int agency_id, ApartmentType type) {
        return txTemplate.execute(status -> {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            if( user_id != 0) {
                jdbc.update(conn -> {
                    PreparedStatement ps = conn.prepareStatement(
                            INSERT_POST, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, location);
                    ps.setInt(2, price);
                    ps.setInt(3, area);
                    ps.setString(4, description);
                    ps.setObject(5, user_id);
                    ps.setObject(6, null);
                    ps.setString(7, type.toString());
                    return ps;
                }, keyHolder);
            }else {
                jdbc.update(conn -> {
                    PreparedStatement ps = conn.prepareStatement(
                            INSERT_POST, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, location);
                    ps.setInt(2, price);
                    ps.setInt(3, area);
                    ps.setString(4, description);
                    ps.setObject(5, null);
                    ps.setObject(6, agency_id);
                    ps.setString(7, type.toString());
                    return ps;
                }, keyHolder);
            }
            Timestamp post_date = new Timestamp(System.currentTimeMillis());
            Integer id = Objects.requireNonNull(keyHolder.getKey()).intValue();
            return new PostDAO(id, location, price, area, description, user_id, agency_id, type, post_date);
        });
    }

    public List<PostDAO> filterBy(String location, int price, ApartmentType type){
        return jdbc.query(filterPostBy(location, price, type), (rs, rowNum) -> fromResultSet(rs));
    }

    public List<PostDAO> getPostsForAgency(int agency_id){
        return jdbc.query(GET_POST_FOR_AGENCY,
                (rs, rowNum) -> fromResultSet(rs), agency_id);
    }

    public List<PostDAO> getPostsForUser(int user_id){
        return jdbc.query(GET_POST_FOR_USER,
                (rs, rowNum) -> fromResultSet(rs), user_id);
    }
    public PostDAO getPost(int id) {
        return jdbc.queryForObject(GET_POST,
                (rs, rowNum) -> fromResultSet(rs), id);
    }

    public List<PostDAO> listPosts(int page, int pageSize) {
        return jdbc.query(LIST_POSTS,
                (rs, rowNum) -> fromResultSet(rs),page*pageSize, pageSize);
    }

    public int getNumberOfPostsForUser(int user_id){
        return jdbc.queryForObject(GET_NUMBER_OF_POSTS_FOR_USER, (rs, rowNum) -> fromResultSetNumber(rs), user_id);
    }

    public int getNumberOfPostsForAgency(int agency_id) {
        return jdbc.queryForObject(GET_NUMBER_OF_POSTS_FOR_AGENCY, (rs, rowNum) -> fromResultSetNumber(rs), agency_id);
    }

    public void deletePost(int id) {
        txTemplate.execute(status -> {
            jdbc.update(DELETE_POSTS, id);
            return null;
        });
    }

    private PostDAO fromResultSet(ResultSet rs) throws SQLException {
        return new PostDAO(
                rs.getInt("post_id"),
                rs.getString("location"),
                rs.getInt("price"),
                rs.getInt("area"),
                rs.getString("description"),
                rs.getInt("user_id"),
                rs.getInt("agency_id"),
                ApartmentType.valueOf(rs.getString("name_type")),
                rs.getTimestamp("post_date")
        );
    }

    private int fromResultSetNumber(ResultSet rs) throws SQLException {
        return rs.getInt("number_of_posts");
    }

    static class Queries {

        public static final String INSERT_POST =
                "INSERT INTO Post (location, price, area, description, user_id , agency_id, type_of_apart_id)  VALUES (?, ?, ?, ?, ?, ?, (select id from type_of_apart where name_type = ?))";

        public static String filterPostBy(String location, int price, ApartmentType type) {
            return  "SELECT \n" +
                    "   * \n" +
                    "FROM\n" +
                    "    Post si\n" +
                    "     JOIN type_of_apart t ON  t.id = si.type_of_apart_id \n" +
                    "WHERE si.location = '"+location+"' AND si.price <= "+price+" AND t.name_type = '"+type+"'";
        }
        public static final String FILTER_POST = "" +
                "SELECT \n" +
                "* \n" +
                "FROM\n" +
                "    Post si\n" +
                "    JOIN type_of_apart t ON  t.id = si.type_of_apart_id \n" +
                "WHERE si.location = ? AND si.price <= ? AND t.name_type = ?";

        public static final String GET_NUMBER_OF_POSTS_FOR_USER = "" +
               "SELECT COUNT(p.post_id) as number_of_posts \n" +
               "FROM Users u \n" +
               "LEFT JOIN Post p ON u.id = p.user_id \n" +
                "WHERE u.id = ?";
        public static final String GET_NUMBER_OF_POSTS_FOR_AGENCY = "" +
                "SELECT COUNT(p.post_id) as number_of_posts \n" +
                "FROM Agency u \n" +
                "LEFT JOIN Post p ON u.id = p.agency_id \n" +
                "WHERE u.id = ?";
        public static final String GET_POST_FOR_USER = "" +
                "SELECT * \n"+
                "FROM \n"+
                " Post si \n"+
                "Join type_of_apart toa on si.type_of_apart_id = toa.id " +
                "Join \n"+
                "Users p ON si.user_id = ? ";
        public static final String GET_POST_FOR_AGENCY = "" +
                "SELECT * \n"+
                "FROM \n"+
                " Post si \n"+
                "Join type_of_apart toa on si.type_of_apart_id = toa.id " +
                "Join \n"+
                "agency p ON si.agency_id = ? ";
        public static final String GET_POST = "" +
                "SELECT * \n" +
                "FROM\n" +
                "    Post p \n" +
                "        JOIN\n" +
                "    type_of_apart si ON si.id = p.post_id \n" +
                "WHERE p.post_id = ?";
        public static final String LIST_POSTS = "" +
                "SELECT \n" +
                "*\n"+
                "FROM\n" +
                "    post si\n"+
                "JOIN type_of_apart tp "+
                "WHERE si.type_of_apart_id = tp.id \n"+
                "LIMIT ?, ?";
        public static final String DELETE_POSTS = "DELETE FROM post WHERE post_id = ?";
    }

}

