package com.example.workproject1.web.api;

import com.example.workproject1.coreServices.Mappers;
import com.example.workproject1.coreServices.PostService.PostService;
import com.example.workproject1.coreServices.models.ApartmentType;
import com.example.workproject1.coreServices.models.Post;
import com.example.workproject1.coreServices.models.TransactionType;
import com.example.workproject1.repositories.models.PostDAO;
import com.example.workproject1.security.JwtUtil;
import com.example.workproject1.web.api.models.PostInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/posts")
public class PostController {
    
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);
    private final PostService postService;
    private final JwtUtil jwtUtil;

    public PostController(PostService postService, JwtUtil jwtUtil) {
        this.postService = postService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(value = "/add", consumes = "multipart/form-data")
    public ResponseEntity<?> createPost(@RequestParam("postInput") String postInputJson,  
                                       @CookieValue("Authorization") String token,
                                       @RequestParam("images") List<MultipartFile> images) {
        try {
            logger.info("Creating new post with {} images", images.size());
            
            // Validate token
            if (token == null || !jwtUtil.validateToken(token)) {
                logger.warn("Invalid or missing token for post creation");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Authentication required"));
            }

            // Parse post input
            ObjectMapper objectMapper = new ObjectMapper();
            PostInput postInput = objectMapper.readValue(postInputJson, PostInput.class);
            
            // Extract user/agency ID from token
            String role = jwtUtil.getRoleFromToken(token);
            int userId = "ROLE_USER".equals(role) ? jwtUtil.getIdFromToken(token) : 0;
            int agencyId = "ROLE_AGENCY".equals(role) ? jwtUtil.getIdFromToken(token) : 0;
            
            logger.debug("Creating post for userId: {}, agencyId: {}", userId, agencyId);

            // Create post (validation is now handled inside PostService)
            Post post = postService.createPost(postInput, userId, agencyId, images);
            
            logger.info("Post created successfully with ID: {}", post.getPostId());
            return ResponseEntity.ok(post);
            
        } catch (Exception e) {
            logger.error("Error creating post: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create post: " + e.getMessage()));
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterByLocationPriceType(@RequestParam String location, 
                                                      @RequestParam int price, 
                                                      @RequestParam ApartmentType apartmentType,
                                                      @RequestParam TransactionType transactionType) {
        try {
            logger.info("Filtering posts by location: {}, price: {}, apartmentType: {}, transactionType: {}", 
                       location, price, apartmentType, transactionType);
            List<PostDAO> posts = postService.filterBy(location, price, apartmentType, transactionType);
            logger.debug("Found {} filtered posts", posts.size());
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            logger.error("Error filtering posts: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to filter posts"));
        }
    }

    @GetMapping("list/posts")
    public ResponseEntity<?> listPosts(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "20") int pageSize,
                                      @RequestParam(required = false) String location,
                                      @RequestParam(required = false) String type,
                                      @RequestParam(required = false) Integer price,
                                      @RequestParam(defaultValue = "BUY") String transactionType) {
        try {
            logger.info("Fetching posts - page: {}, pageSize: {}, location: {}, type: {}, price: {}, transactionType: {}", 
                       page, pageSize, location, type, price, transactionType);
            
            // Validate input parameters
            if (page < 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Page number must be non-negative"));
            }
            if (pageSize <= 0 || pageSize > 100) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Page size must be between 1 and 100"));
            }
            
            List<Post> posts;
            int totalCount;
            
            // Always use filtering to ensure transaction type is applied
            posts = getFilteredPosts(location, type, price, transactionType);
            totalCount = posts.size();
            
            Map<String, Object> response = buildPaginationResponse(posts, totalCount, page, pageSize);
            logger.debug("Returning {} posts out of {} total", posts.size(), totalCount);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error fetching posts: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch posts"));
        }
    }
    
    private List<Post> getFilteredPosts(String location, String type, Integer price, String transactionType) {
        ApartmentType apartmentType = parseApartmentType(type);
        TransactionType transactionTypeEnum = parseTransactionType(transactionType);
        int maxPrice = (price != null) ? price : Integer.MAX_VALUE;
        String filterLocation = (location != null && !location.isEmpty()) ? location : "";
        
        logger.info("Filtering posts - location: '{}', type: '{}', price: {}, transactionType: '{}', apartmentType: {}", 
                   filterLocation, type, maxPrice, transactionType, apartmentType);
        
        List<PostDAO> filteredPosts;
        if (apartmentType != null) {
            // Use specific filtering when apartment type is selected
            logger.info("Using specific filtering with apartment type: {}", apartmentType);
            filteredPosts = postService.filterBy(filterLocation, maxPrice, apartmentType, transactionTypeEnum);
        } else {
            // Use general filtering when no apartment type is selected
            // This will still filter by location, price, and transaction type
            logger.info("Using general filtering without apartment type - location: '{}', price: {}", filterLocation, maxPrice);
            filteredPosts = postService.filterByGeneral(filterLocation, maxPrice, transactionTypeEnum);
        }
        
        logger.info("Found {} filtered posts", filteredPosts.size());
        
        return filteredPosts.stream()
                .map(Mappers::fromPostDAO)
                .collect(java.util.stream.Collectors.toList());
    }
    
    private ApartmentType parseApartmentType(String type) {
        if (type == null || type.isEmpty()) {
            return null;
        }
        try {
            return ApartmentType.valueOf(type);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid apartment type: {}", type);
            return null;
        }
    }
    
    private TransactionType parseTransactionType(String transactionType) {
        if (transactionType == null || transactionType.isEmpty()) {
            return TransactionType.BUY; // Default to BUY
        }
        try {
            return TransactionType.valueOf(transactionType);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid transaction type: {}, defaulting to BUY", transactionType);
            return TransactionType.BUY;
        }
    }
    
    private Map<String, Object> buildPaginationResponse(List<Post> posts, int totalCount, int page, int pageSize) {
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        
        return Map.of(
            "posts", posts,
            "totalCount", totalCount,
            "totalPages", totalPages,
            "currentPage", page + 1,
            "pageSize", pageSize
        );
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserPosts(@PathVariable int id) {
        try {
            logger.info("Fetching posts for user: {}", id);
            
            if (id <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid user ID"));
            }
            
            List<Post> posts = postService.getUserPosts(id);
            logger.debug("Found {} posts for user {}", posts.size(), id);
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            logger.error("Error fetching posts for user {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch user posts"));
        }
    }

    @GetMapping("/agency/{id}")
    public ResponseEntity<?> getAgencyPosts(@PathVariable int id) {
        try {
            logger.info("Fetching posts for agency: {}", id);
            
            if (id <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid agency ID"));
            }
            
            List<Post> posts = postService.getPostsForAgency(id);
            logger.debug("Found {} posts for agency {}", posts.size(), id);
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            logger.error("Error fetching posts for agency {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch agency posts"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(@PathVariable int id) {
        try {
            logger.info("Fetching post with ID: {}", id);
            
            if (id <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid post ID"));
            }
            
            // Increment view count when someone views the post
            postService.incrementViewCount(id);
            Post post = postService.getPost(id);
            
            logger.debug("Post {} fetched successfully", id);
            return ResponseEntity.ok(post);
            
        } catch (Exception e) {
            logger.error("Error fetching post {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Post not found"));
        }
    }

    @PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
    public ResponseEntity<?> updatePost(@PathVariable int id,
                                       @RequestParam("postInput") String postInputJson,
                                       @CookieValue("Authorization") String token,
                                       @RequestParam(value = "images", required = false) List<MultipartFile> images,
                                       @RequestParam(value = "imagesToDelete", required = false) String imagesToDeleteJson) {
        try {
            logger.info("Updating post with ID: {}", id);
            
            // Validate token
            if (token == null || !jwtUtil.validateToken(token)) {
                logger.warn("Invalid or missing token for post update");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Authentication required"));
            }

            // Validate post ID
            if (id <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid post ID"));
            }

            // Parse post input
            ObjectMapper objectMapper = new ObjectMapper();
            PostInput postInput = objectMapper.readValue(postInputJson, PostInput.class);
            
            // Parse images to delete
            List<String> imagesToDelete = new ArrayList<>();
            if (imagesToDeleteJson != null && !imagesToDeleteJson.isEmpty()) {
                imagesToDelete = objectMapper.readValue(imagesToDeleteJson, 
                    objectMapper.getTypeFactory().constructCollectionType(List.class, String.class));
            }

            // Handle images
            List<MultipartFile> imageFiles = images != null ? images : new ArrayList<>();
            
            // Update the post
            postService.updatePost(id, postInput, imageFiles, imagesToDelete);
            
            logger.info("Post {} updated successfully", id);
            return ResponseEntity.ok(Map.of("message", "Post updated successfully"));
            
        } catch (Exception e) {
            logger.error("Error updating post {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update post"));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePost(@PathVariable int id, 
                                       @CookieValue("Authorization") String token) {
        try {
            logger.info("Deleting post with ID: {}", id);
            
            // Validate token
            if (token == null || !jwtUtil.validateToken(token)) {
                logger.warn("Invalid or missing token for post deletion");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Authentication required"));
            }
            
            // Validate post ID
            if (id <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid post ID"));
            }
            
            // Delete the post
            postService.deletePost(id);
            
            logger.info("Post {} deleted successfully", id);
            return ResponseEntity.ok(Map.of("message", "Post deleted successfully"));
            
        } catch (Exception e) {
            logger.error("Error deleting post {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete post"));
        }
    }

}

