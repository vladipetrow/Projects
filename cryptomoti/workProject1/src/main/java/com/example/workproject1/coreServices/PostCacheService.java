package com.example.workproject1.coreServices;

import com.example.workproject1.coreServices.models.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Service for caching post-related data in Redis
 */
@Service
public class PostCacheService {
    private static final Logger log = LoggerFactory.getLogger(PostCacheService.class);
    
    // Cache keys
    private static final String POST_KEY = "cryptomoti:posts:";
    private static final String POST_LIST_KEY = "cryptomoti:posts:list:";
    private static final String USER_POSTS_KEY = "cryptomoti:posts:user:";
    private static final String FILTERED_POSTS_KEY = "cryptomoti:posts:filtered:";
    
    // Cache TTLs - optimized for performance vs freshness
    private static final long POST_TTL = 6; // 6 hours - posts are invalidated on writes
    private static final long POST_LIST_TTL = 15; // 15 minutes - shorter for lists
    private static final long USER_POSTS_TTL = 10; // 10 minutes - shorter for user lists
    private static final long FILTERED_POSTS_TTL = 5; // 5 minutes - shortest for search results
    
    private final RedisTemplate<String, Object> objectRedisTemplate;
    private final RedisKeyTracker keyTracker;
    
    public PostCacheService(RedisTemplate<String, Object> objectRedisTemplate, RedisKeyTracker keyTracker) {
        this.objectRedisTemplate = objectRedisTemplate;
        this.keyTracker = keyTracker;
    }
    
    /**
     * Cache a single post
     */
    public void cachePost(Post post) {
        if (post == null) return;
        
        String key = POST_KEY + post.getPostId();
        try {
            // Use atomic operation for consistency
            keyTracker.trackPostKeyAtomically(key, post, POST_TTL, TimeUnit.HOURS);
            log.debug("Cached post: {}", post.getPostId());
        } catch (Exception e) {
            log.error("Failed to cache post {}: {}", post.getPostId(), e.getMessage());
        }
    }
    
    /**
     * Get cached post
     */
    public Post getCachedPost(int postId) {
        String key = POST_KEY + postId;
        try {
            Post post = (Post) objectRedisTemplate.opsForValue().get(key);
            if (post != null) {
                log.debug("Post {} found in cache", postId);
            }
            return post;
        } catch (Exception e) {
            log.error("Failed to get cached post {}: {}", postId, e.getMessage());
            return null;
        }
    }
    
    /**
     * Cache post list
     */
    public void cachePostList(List<Post> posts, String cacheKey) {
        if (posts == null || posts.isEmpty()) return;
        
        try {
            objectRedisTemplate.opsForValue().set(cacheKey, posts, POST_LIST_TTL, TimeUnit.MINUTES);
            log.debug("Cached post list: {} posts", posts.size());
        } catch (Exception e) {
            log.error("Failed to cache post list: {}", e.getMessage());
        }
    }
    
    /**
     * Get cached post list
     */
    @SuppressWarnings("unchecked")
    public List<Post> getCachedPostList(String cacheKey) {
        try {
            List<Post> posts = (List<Post>) objectRedisTemplate.opsForValue().get(cacheKey);
            if (posts != null) {
                log.debug("Post list found in cache: {} posts", posts.size());
            }
            return posts;
        } catch (Exception e) {
            log.error("Failed to get cached post list: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * Cache user posts
     */
    public void cacheUserPosts(int userId, List<Post> posts) {
        String key = USER_POSTS_KEY + userId;
        try {
            objectRedisTemplate.opsForValue().set(key, posts, USER_POSTS_TTL, TimeUnit.MINUTES);
            keyTracker.trackUserPostsKey(key);
            log.debug("Cached {} posts for user {}", posts.size(), userId);
        } catch (Exception e) {
            log.error("Failed to cache user posts: {}", e.getMessage());
        }
    }
    
    /**
     * Get cached user posts
     */
    public List<Post> getCachedUserPosts(int userId) {
        String key = USER_POSTS_KEY + userId;
        return getCachedPostList(key);
    }
    
    /**
     * Cache filtered posts
     */
    public void cacheFilteredPosts(String apartmentType, String transactionType, List<Post> posts) {
        String key = FILTERED_POSTS_KEY + apartmentType + ":" + transactionType;
        try {
            objectRedisTemplate.opsForValue().set(key, posts, FILTERED_POSTS_TTL, TimeUnit.MINUTES);
            keyTracker.trackFilteredPostsKey(key);
            log.debug("Cached {} filtered posts for {}:{}", posts.size(), apartmentType, transactionType);
        } catch (Exception e) {
            log.error("Failed to cache filtered posts: {}", e.getMessage());
        }
    }
    
    /**
     * Get cached filtered posts
     */
    public List<Post> getCachedFilteredPosts(String apartmentType, String transactionType) {
        String key = FILTERED_POSTS_KEY + apartmentType + ":" + transactionType;
        return getCachedPostList(key);
    }
    
    /**
     * Invalidate post cache
     */
    public void invalidatePost(int postId) {
        String key = POST_KEY + postId;
        try {
            objectRedisTemplate.delete(key);
            keyTracker.untrackKey(key);
            log.debug("Invalidated post cache: {}", postId);
        } catch (Exception e) {
            log.error("Failed to invalidate post cache {}: {}", postId, e.getMessage());
        }
    }
    
    /**
     * Invalidate user posts cache
     */
    public void invalidateUserPosts(int userId) {
        String key = USER_POSTS_KEY + userId;
        try {
            objectRedisTemplate.delete(key);
            keyTracker.untrackKey(key);
            log.debug("Invalidated user posts cache: {}", userId);
        } catch (Exception e) {
            log.error("Failed to invalidate user posts cache {}: {}", userId, e.getMessage());
        }
    }
    
    /**
     * Invalidate all post caches (use sparingly - prefer targeted invalidation)
     */
    public void invalidateAllPostCaches() {
        try {
            // Get tracked keys instead of using expensive keys() operation
            Set<String> postKeys = keyTracker.getTrackedPostKeys();
            Set<String> listKeys = keyTracker.getTrackedPostListKeys();
            Set<String> userKeys = keyTracker.getTrackedUserPostsKeys();
            Set<String> filterKeys = keyTracker.getTrackedFilteredPostsKeys();
            
            // Combine all keys
            Set<String> allKeys = new HashSet<>();
            if (postKeys != null) allKeys.addAll(postKeys);
            if (listKeys != null) allKeys.addAll(listKeys);
            if (userKeys != null) allKeys.addAll(userKeys);
            if (filterKeys != null) allKeys.addAll(filterKeys);
            
            // Delete all keys at once
            if (!allKeys.isEmpty()) {
                objectRedisTemplate.delete(allKeys);
                keyTracker.clearAllTracking();
                log.info("Invalidated {} post cache keys", allKeys.size());
            }
        } catch (Exception e) {
            log.error("Failed to invalidate all post caches: {}", e.getMessage());
        }
    }
    
    /**
     * Smart invalidation - only invalidate what's actually affected
     */
    public void invalidatePostAndRelatedCaches(int postId, int userId) {
        try {
            // Invalidate specific post
            invalidatePost(postId);
            
            // Invalidate user's posts list
            invalidateUserPosts(userId);
            
            // Invalidate global post lists (they might contain this post)
            Set<String> listKeys = keyTracker.getTrackedPostListKeys();
            if (listKeys != null && !listKeys.isEmpty()) {
                objectRedisTemplate.delete(listKeys);
                // Clear tracking for deleted keys
                listKeys.forEach(keyTracker::untrackKey);
                log.info("Invalidated {} post list cache keys", listKeys.size());
            }
            
            // Note: Filtered caches have short TTL (5-15min) so we let them expire naturally
            // This avoids expensive key scanning operations
            
        } catch (Exception e) {
            log.error("Failed to invalidate post and related caches: {}", e.getMessage());
        }
    }
    
    /**
     * Cache post list with pagination
     */
    public void cachePostList(int page, int pageSize, List<Post> posts) {
        try {
            String key = POST_LIST_KEY + page + ":" + pageSize;
            // Use atomic operation for consistency
            keyTracker.trackPostListKeyAtomically(key, posts, POST_LIST_TTL, TimeUnit.MINUTES);
            log.debug("Cached {} posts for page {}:{}", posts.size(), page, pageSize);
        } catch (Exception e) {
            log.error("Failed to cache post list: {}", e.getMessage());
        }
    }
    
    /**
     * Get cached post list
     */
    @SuppressWarnings("unchecked")
    public List<Post> getCachedPostList(int page, int pageSize) {
        try {
            String key = POST_LIST_KEY + page + ":" + pageSize;
            Object cached = objectRedisTemplate.opsForValue().get(key);
            if (cached instanceof List) {
                log.debug("Cache hit for post list page {}:{}", page, pageSize);
                return (List<Post>) cached;
            }
        } catch (Exception e) {
            log.error("Failed to get cached post list: {}", e.getMessage());
        }
        return null;
    }
}
