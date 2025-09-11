package com.example.workproject1.repositories.mysql;

import java.sql.*;

import com.example.workproject1.coreServices.models.ApartmentType;
import com.example.workproject1.coreServices.models.TransactionType;
import com.example.workproject1.repositories.PostRepository;
import com.example.workproject1.repositories.models.PostDAO;
import com.example.workproject1.web.api.models.PostInput;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.workproject1.repositories.mysql.MySQLPostRepository.Queries.*;

public class MySQLPostRepository implements PostRepository {
    private final TransactionTemplate txTemplate;
    private final JdbcTemplate jdbc;

    public MySQLPostRepository(TransactionTemplate txTemplate, JdbcTemplate jdbc) {
        this.txTemplate = txTemplate;
        this.jdbc = jdbc;
    }

    public PostDAO createPost(
            String location, int price, int area, String description, int user_id, int agency_id,
            int typeOfApartId, TransactionType transactionType, List<String> imageUrls) {
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
                    ps.setInt(7, typeOfApartId);
                    ps.setString(8, transactionType.toString());
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
                    ps.setInt(7, typeOfApartId);
                    ps.setString(8, transactionType.toString());
                    return ps;
                }, keyHolder);
            }

            int postId = Objects.requireNonNull(keyHolder.getKey()).intValue();

            addImagesToPost(postId, imageUrls);
            return new PostDAO.Builder()
                    .postId(postId)
                    .location(location)
                    .price(price)
                    .area(area)
                    .description(description)
                    .typeOfApartId(typeOfApartId)
                    .transactionType(transactionType)
                    .userId(user_id)
                    .agencyId(agency_id)
                    .postDate(null)
                    .imageUrls(imageUrls)
                    .build();
        });
    }

    public List<PostDAO> filterBy(String location, int price, ApartmentType apartmentType, TransactionType transactionType){
        return jdbc.query(FILTER_POST, (rs, rowNum) -> fromResultSet(rs), location, location, price, apartmentType.toString(), transactionType.toString());
    }
    
    public List<PostDAO> filterByGeneral(String location, int price, TransactionType transactionType){
        return jdbc.query(FILTER_POST_GENERAL, (rs, rowNum) -> fromResultSet(rs), location, location, price, transactionType.toString());
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

    public int getTotalPostsCount() {
        return jdbc.queryForObject(GET_TOTAL_POSTS_COUNT, Integer.class);
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

    public void addImagesToPost(int postId, List<String> imageUrls) {
        for (String imageUrl : imageUrls) {
            jdbc.update(INSERT_IMAGE, postId, imageUrl);
        }
    }

    @Override
    public List<PostDAO> getUserPosts(int userId) {
        return jdbc.query(GET_USER_POSTS, (rs, rowNum) -> fromResultSet(rs), userId);
    }

    @Override
    public void incrementViewCount(int postId) {
        jdbc.update(INCREMENT_VIEW_COUNT, postId);
    }

    @Override
    public void updatePost(int id, PostInput postInput, List<String> newImageUrls, List<String> imagesToDelete) {
        txTemplate.execute(status -> {
            // Update post basic information
            jdbc.update(UPDATE_POST, 
                postInput.getLocation(),
                postInput.getPrice(),
                postInput.getArea(),
                postInput.getDescription(),
                postInput.getApartmentType().ordinal() + 1, // Convert enum to database ID
                postInput.getTransactionType().toString(),
                id
            );
            
            // Delete specified images
            if (imagesToDelete != null && !imagesToDelete.isEmpty()) {
                for (String imageUrl : imagesToDelete) {
                    jdbc.update(DELETE_IMAGE, imageUrl);
                }
            }
            
            // Add new images
            if (newImageUrls != null && !newImageUrls.isEmpty()) {
                for (String imageUrl : newImageUrls) {
                    jdbc.update(INSERT_IMAGE, id, imageUrl);
                }
            }
            
            return null;
        });
    }

    private List<String> getImagesForPost(int postId) {
        return jdbc.query(GET_IMAGES, (rs, rowNum) -> rs.getString("image_url"), postId);
    }

    private PostDAO fromResultSet(ResultSet rs) throws SQLException {
        int postId = rs.getInt("post_id");
        List<String> imageUrls = getImagesForPost(postId);
        boolean isPromoted = rs.getBoolean("is_promoted");
        int viewCount = rs.getInt("view_count");

        return new PostDAO.Builder()
                .postId(postId)
                .location(rs.getString("location"))
                .price(rs.getInt("price"))
                .area(rs.getInt("area"))
                .description(rs.getString("description"))
                .typeOfApartId(rs.getInt("type_of_apart_id"))
                .transactionType(TransactionType.valueOf(rs.getString("transaction_type")))
                .userId(rs.getInt("user_id"))
                .agencyId(rs.getInt("agency_id"))
                .postDate(rs.getTimestamp("post_date"))
                .imageUrls(imageUrls)
                .promoted(isPromoted)
                .viewCount(viewCount)
                .build();
    }

    /**
     * Optimized method to load posts with images in a single query.
     * This prevents N+1 query problem by fetching all images at once.
     */
    public List<PostDAO> listPostsWithImages(int page, int pageSize) {
        // First, get all posts
        List<PostDAO> posts = jdbc.query(LIST_POSTS,
                (rs, rowNum) -> fromResultSetWithoutImages(rs), page*pageSize, pageSize);
        
        if (posts.isEmpty()) {
            return posts;
        }
        
        // Get all post IDs
        List<Integer> postIds = posts.stream()
                .map(PostDAO::getPostId)
                .toList();
        
        // Fetch all images for these posts in one query
        Map<Integer, List<String>> imagesMap = getImagesForPosts(postIds);
        
        // Attach images to posts
        return posts.stream()
                .map(post -> new PostDAO.Builder()
                        .postId(post.getPostId())
                        .location(post.getLocation())
                        .price(post.getPrice())
                        .area(post.getArea())
                        .description(post.getDescription())
                        .typeOfApartId(post.getTypeOfApartId())
                        .transactionType(post.getTransactionType())
                        .userId(post.getUserId())
                        .agencyId(post.getAgencyId())
                        .postDate(post.getPostDate())
                        .imageUrls(imagesMap.getOrDefault(post.getPostId(), List.of()))
                        .promoted(post.isPromoted())
                        .viewCount(post.getViewCount())
                        .build())
                .toList();
    }

    /**
     * Creates PostDAO without images to avoid N+1 queries.
     */
    private PostDAO fromResultSetWithoutImages(ResultSet rs) throws SQLException {
        boolean isPromoted = rs.getBoolean("is_promoted");
        int viewCount = rs.getInt("view_count");

        return new PostDAO.Builder()
                .postId(rs.getInt("post_id"))
                .location(rs.getString("location"))
                .price(rs.getInt("price"))
                .area(rs.getInt("area"))
                .description(rs.getString("description"))
                .typeOfApartId(rs.getInt("type_of_apart_id"))
                .transactionType(TransactionType.valueOf(rs.getString("transaction_type")))
                .userId(rs.getInt("user_id"))
                .agencyId(rs.getInt("agency_id"))
                .postDate(rs.getTimestamp("post_date"))
                .imageUrls(List.of()) // Empty list, will be populated later
                .promoted(isPromoted)
                .viewCount(viewCount)
                .build();
    }

    /**
     * Fetches images for multiple posts in one query.
     */
    private Map<Integer, List<String>> getImagesForPosts(List<Integer> postIds) {
        if (postIds.isEmpty()) {
            return Map.of();
        }
        
        String placeholders = postIds.stream()
                .map(id -> "?")
                .collect(Collectors.joining(","));
        
        String query = "SELECT post_id, image_url FROM post_images WHERE post_id IN (" + placeholders + ")";
        
        Map<Integer, List<String>> imagesMap = new HashMap<>();
        
        jdbc.query(query, postIds.toArray(), (rs, rowNum) -> {
            int postId = rs.getInt("post_id");
            String imageUrl = rs.getString("image_url");
            imagesMap.computeIfAbsent(postId, k -> new ArrayList<>()).add(imageUrl);
            return null;
        });
        
        return imagesMap;
    }

    private int fromResultSetNumber(ResultSet rs) throws SQLException {
        return rs.getInt("number_of_posts");
    }

    static class Queries {

        public static final String INSERT_POST =
                "INSERT INTO post (location, price, area, description, user_id, agency_id, type_of_apart_id, transaction_type)  " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        public static final String INSERT_IMAGE = "INSERT INTO post_images (post_id, image_url) VALUES (?, ?)";

        public static final String GET_IMAGES = "SELECT image_url FROM post_images WHERE post_id = ?";

        public static String filterPostBy(String location, int price, ApartmentType type) {
            return  "SELECT \n" +
                    "   * \n" +
                    "FROM\n" +
                    "    post si\n" +
                    "     JOIN type_of_apart t ON  t.id = si.type_of_apart_id \n" +
                    "WHERE si.location = '"+location+"' AND si.price <= "+price+" AND t.name_type = '"+type+"'";
        }
        public static final String FILTER_POST = "" +
                "SELECT \n" +
                "    p.post_id, p.location, p.price, p.area, p.description, p.type_of_apart_id, p.transaction_type, \n" +
                "    p.user_id, p.agency_id, p.post_date, p.view_count, p.is_promoted, t.name_type \n" +
                "FROM\n" +
                "    post p\n" +
                "    JOIN type_of_apart t ON t.id = p.type_of_apart_id \n" +
                "WHERE (? = '' OR p.location LIKE CONCAT('%', ?, '%')) AND p.price <= ? AND t.name_type = ? AND p.transaction_type = ?";

        public static final String FILTER_POST_GENERAL = "" +
                "SELECT \n" +
                "    p.post_id, p.location, p.price, p.area, p.description, p.type_of_apart_id, p.transaction_type, \n" +
                "    p.user_id, p.agency_id, p.post_date, p.view_count, p.is_promoted, t.name_type \n" +
                "FROM\n" +
                "    post p\n" +
                "    JOIN type_of_apart t ON t.id = p.type_of_apart_id \n" +
                "WHERE (? = '' OR p.location LIKE CONCAT('%', ?, '%')) AND p.price <= ? AND p.transaction_type = ?";

        public static final String GET_NUMBER_OF_POSTS_FOR_USER = "" +
               "SELECT COUNT(p.post_id) as number_of_posts \n" +
               "FROM users u \n" +
                "LEFT JOIN post p ON u.id = p.user_id \n" +
                "WHERE u.id = ?";
        public static final String GET_NUMBER_OF_POSTS_FOR_AGENCY = "" +
                "SELECT COUNT(p.post_id) as number_of_posts \n" +
                "FROM agency u \n" +
                "LEFT JOIN post p ON u.id = p.agency_id \n" +
                "WHERE u.id = ?";
        public static final String GET_POST_FOR_USER = "" +
                "SELECT * \n"+
                "FROM \n"+
                " post si \n"+
                "Join type_of_apart toa on si.type_of_apart_id = toa.id " +
                "Join \n"+
                "users p ON si.user_id = ? ";
        public static final String GET_POST_FOR_AGENCY = "" +
                "SELECT * \n"+
                "FROM \n"+
                " post si \n"+
                "Join type_of_apart toa on si.type_of_apart_id = toa.id " +
                "Join \n"+
                "agency p ON si.agency_id = ? ";
        public static final String GET_POST = "" +
                "SELECT \n" +
                "    p.post_id, p.location, p.price, p.area, p.description, p.type_of_apart_id, p.transaction_type, \n" +
                "    p.user_id, p.agency_id, p.post_date, p.view_count, tp.name_type,\n" +
                "    CASE \n" +
                "        WHEN (p.user_id IS NOT NULL AND EXISTS (SELECT 1 FROM subscriptions s WHERE s.user_id = p.user_id AND s.payment_status = 'ACTIVE' AND s.expires_at IS NOT NULL AND s.expires_at > NOW())) \n" +
                "        OR (p.agency_id IS NOT NULL AND EXISTS (SELECT 1 FROM subscriptions s WHERE s.agency_id = p.agency_id AND s.payment_status = 'ACTIVE' AND s.expires_at IS NOT NULL AND s.expires_at > NOW())) \n" +
                "        THEN 1 ELSE 0 \n" +
                "    END as is_promoted\n" +
                "FROM\n" +
                "    post p\n" +
                "JOIN type_of_apart tp ON p.type_of_apart_id = tp.id \n" +
                "WHERE p.post_id = ?";
        public static final String LIST_POSTS = "" +
                "SELECT \n" +
                "    si.post_id, si.location, si.price, si.area, si.description, si.type_of_apart_id, si.transaction_type, \n" +
                "    si.user_id, si.agency_id, si.post_date, si.view_count, tp.name_type,\n" +
                "    CASE \n" +
                "        WHEN (si.user_id IS NOT NULL AND EXISTS (SELECT 1 FROM subscriptions s WHERE s.user_id = si.user_id AND s.payment_status = 'ACTIVE' AND s.expires_at IS NOT NULL AND s.expires_at > NOW())) \n" +
                "        OR (si.agency_id IS NOT NULL AND EXISTS (SELECT 1 FROM subscriptions s WHERE s.agency_id = si.agency_id AND s.payment_status = 'ACTIVE' AND s.expires_at IS NOT NULL AND s.expires_at > NOW())) \n" +
                "        THEN 1 ELSE 0 \n" +
                "    END as is_promoted\n"+
                "FROM\n" +
                "    post si\n"+
                "JOIN type_of_apart tp ON si.type_of_apart_id = tp.id \n"+
                "ORDER BY is_promoted DESC, si.post_date DESC \n"+
                "LIMIT ?, ?";
        public static final String GET_TOTAL_POSTS_COUNT = "SELECT COUNT(*) FROM post";
        public static final String DELETE_POSTS = "DELETE FROM post WHERE post_id = ?";
        public static final String GET_USER_POSTS = "" +
                "SELECT \n" +
                "    si.post_id, si.location, si.price, si.area, si.description, si.type_of_apart_id, si.transaction_type, \n" +
                "    si.user_id, si.agency_id, si.post_date, si.view_count, tp.name_type,\n" +
                "    CASE \n" +
                "        WHEN (si.user_id IS NOT NULL AND EXISTS (SELECT 1 FROM subscriptions s WHERE s.user_id = si.user_id AND s.payment_status = 'ACTIVE' AND s.expires_at IS NOT NULL AND s.expires_at > NOW())) \n" +
                "        OR (si.agency_id IS NOT NULL AND EXISTS (SELECT 1 FROM subscriptions s WHERE s.agency_id = si.agency_id AND s.payment_status = 'ACTIVE' AND s.expires_at IS NOT NULL AND s.expires_at > NOW())) \n" +
                "        THEN 1 ELSE 0 \n" +
                "    END as is_promoted\n"+
                "FROM\n" +
                "    post si\n"+
                "JOIN type_of_apart tp ON si.type_of_apart_id = tp.id \n"+
                "WHERE si.user_id = ? \n"+
                "ORDER BY si.post_date DESC";
        public static final String INCREMENT_VIEW_COUNT = "UPDATE post SET view_count = view_count + 1 WHERE post_id = ?";
        public static final String UPDATE_POST = "" +
                "UPDATE post SET " +
                "location = ?, " +
                "price = ?, " +
                "area = ?, " +
                "description = ?, " +
                "type_of_apart_id = ?, " +
                "transaction_type = ? " +
                "WHERE post_id = ?";
        public static final String DELETE_IMAGE = "DELETE FROM post_images WHERE image_url = ?";
    }

}

