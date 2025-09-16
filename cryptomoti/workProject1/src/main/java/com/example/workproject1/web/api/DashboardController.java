package com.example.workproject1.web.api;

import com.example.workproject1.coreServices.PostService.PostService;
import com.example.workproject1.coreServices.models.Post;
import com.example.workproject1.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
    private final PostService postService;
    private final JwtUtil jwtUtil;

    public DashboardController(PostService postService, JwtUtil jwtUtil) {
        this.postService = postService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/my-posts")
    public ResponseEntity<?> getMyPosts(@CookieValue(value = "Authorization", required = false) String token) {
        try {
            logger.info("Fetching user's posts");
            
            if (token == null || !jwtUtil.validateToken(token)) {
                logger.warn("Unauthorized access attempt to get user posts");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Authentication required"));
            }

            int userId = jwtUtil.getIdFromToken(token);
            logger.debug("Fetching posts for user: {}", userId);
            
            List<Post> posts = postService.getUserPosts(userId);
            logger.debug("Found {} posts for user {}", posts.size(), userId);

            return ResponseEntity.ok(Map.of(
                "posts", posts,
                "totalCount", posts.size()
            ));
        } catch (Exception e) {
            logger.error("Error fetching user posts: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch posts"));
        }
    }

    @PostMapping("/increment-view/{postId}")
    public ResponseEntity<?> incrementView(@PathVariable int postId) {
        try {
            logger.info("Incrementing view count for post: {}", postId);
            
            if (postId <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid post ID"));
            }
            
            postService.incrementViewCount(postId);
            logger.debug("View count incremented for post {}", postId);
            
            return ResponseEntity.ok(Map.of("message", "View count incremented"));
        } catch (Exception e) {
            logger.error("Error incrementing view count for post {}: {}", postId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to increment view count"));
        }
    }
}
