package com.example.workproject1.coreServices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Service for tracking Redis cache keys to avoid expensive keys() operations
 */
@Service
public class RedisKeyTracker {
    private static final Logger log = LoggerFactory.getLogger(RedisKeyTracker.class);
    
    // Key tracking sets
    private static final String POST_KEYS_SET = "cryptomoti:tracking:posts";
    private static final String POST_LIST_KEYS_SET = "cryptomoti:tracking:post_lists";
    private static final String USER_POSTS_KEYS_SET = "cryptomoti:tracking:user_posts";
    private static final String FILTERED_POSTS_KEYS_SET = "cryptomoti:tracking:filtered_posts";
    
    private final RedisTemplate<String, Object> objectRedisTemplate;
    
    public RedisKeyTracker(RedisTemplate<String, Object> objectRedisTemplate) {
        this.objectRedisTemplate = objectRedisTemplate;
    }
    
    /**
     * Track a post key
     */
    public void trackPostKey(String key) {
        try {
            boolean isNewSet = objectRedisTemplate.opsForSet().add(POST_KEYS_SET, key) == 1;
            // Only set TTL if this is a new set (first key added)
            if (isNewSet) {
                objectRedisTemplate.expire(POST_KEYS_SET, 1, TimeUnit.DAYS);
            }
        } catch (Exception e) {
            log.error("Failed to track post key {}: {}", key, e.getMessage());
        }
    }
    
    /**
     * Atomically track a post key and cache the post
     */
    public void trackPostKeyAtomically(String key, Object post, long ttl, TimeUnit timeUnit) {
        try {
            objectRedisTemplate.execute(new SessionCallback<Object>() {
                @Override
                @SuppressWarnings("unchecked")
                public Object execute(RedisOperations operations) {
                    operations.multi();
                    
                    // Add to tracking set
                    operations.opsForSet().add(POST_KEYS_SET, key);
                    
                    // Set TTL for tracking set (unconditional - minimal cost)
                    operations.expire(POST_KEYS_SET, 1, TimeUnit.DAYS);
                    
                    // Cache the post
                    operations.opsForValue().set(key, post, ttl, timeUnit);
                    
                    return operations.exec();
                }
            });
        } catch (Exception e) {
            log.error("Failed to atomically track and cache post key {}: {}", key, e.getMessage());
        }
    }
    
    /**
     * Atomically track a post list key and cache the list
     */
    public void trackPostListKeyAtomically(String key, Object postList, long ttl, TimeUnit timeUnit) {
        try {
            objectRedisTemplate.execute(new SessionCallback<Object>() {
                @Override
                @SuppressWarnings("unchecked")
                public Object execute(RedisOperations operations) {
                    operations.multi();
                    
                    // Add to tracking set
                    operations.opsForSet().add(POST_LIST_KEYS_SET, key);
                    
                    // Set TTL for tracking set (unconditional - minimal cost)
                    operations.expire(POST_LIST_KEYS_SET, 1, TimeUnit.DAYS);
                    
                    // Cache the post list
                    operations.opsForValue().set(key, postList, ttl, timeUnit);
                    
                    return operations.exec();
                }
            });
        } catch (Exception e) {
            log.error("Failed to atomically track and cache post list key {}: {}", key, e.getMessage());
        }
    }
    
    /**
     * Track a post list key
     */
    public void trackPostListKey(String key) {
        try {
            boolean isNewSet = objectRedisTemplate.opsForSet().add(POST_LIST_KEYS_SET, key) == 1;
            if (isNewSet) {
                objectRedisTemplate.expire(POST_LIST_KEYS_SET, 1, TimeUnit.DAYS);
            }
        } catch (Exception e) {
            log.error("Failed to track post list key {}: {}", key, e.getMessage());
        }
    }
    
    /**
     * Track a user posts key
     */
    public void trackUserPostsKey(String key) {
        try {
            boolean isNewSet = objectRedisTemplate.opsForSet().add(USER_POSTS_KEYS_SET, key) == 1;
            if (isNewSet) {
                objectRedisTemplate.expire(USER_POSTS_KEYS_SET, 1, TimeUnit.DAYS);
            }
        } catch (Exception e) {
            log.error("Failed to track user posts key {}: {}", key, e.getMessage());
        }
    }
    
    /**
     * Atomically track a user posts key and cache the list
     */
    public void trackUserPostsKeyAtomically(String key, Object userPosts, long ttl, TimeUnit timeUnit) {
        try {
            objectRedisTemplate.execute(new SessionCallback<Object>() {
                @Override
                @SuppressWarnings("unchecked")
                public Object execute(RedisOperations operations) {
                    operations.multi();
                    
                    // Add to tracking set
                    operations.opsForSet().add(USER_POSTS_KEYS_SET, key);
                    
                    // Set TTL for tracking set (unconditional - minimal cost)
                    operations.expire(USER_POSTS_KEYS_SET, 1, TimeUnit.DAYS);
                    
                    // Cache the user posts
                    operations.opsForValue().set(key, userPosts, ttl, timeUnit);
                    
                    return operations.exec();
                }
            });
        } catch (Exception e) {
            log.error("Failed to atomically track and cache user posts key {}: {}", key, e.getMessage());
        }
    }
    
    /**
     * Track a filtered posts key
     */
    public void trackFilteredPostsKey(String key) {
        try {
            boolean isNewSet = objectRedisTemplate.opsForSet().add(FILTERED_POSTS_KEYS_SET, key) == 1;
            if (isNewSet) {
                objectRedisTemplate.expire(FILTERED_POSTS_KEYS_SET, 1, TimeUnit.DAYS);
            }
        } catch (Exception e) {
            log.error("Failed to track filtered posts key {}: {}", key, e.getMessage());
        }
    }
    
    /**
     * Atomically track a filtered posts key and cache the list
     */
    public void trackFilteredPostsKeyAtomically(String key, Object filteredPosts, long ttl, TimeUnit timeUnit) {
        try {
            objectRedisTemplate.execute(new SessionCallback<Object>() {
                @Override
                @SuppressWarnings("unchecked")
                public Object execute(RedisOperations operations) {
                    operations.multi();
                    
                    // Add to tracking set
                    operations.opsForSet().add(FILTERED_POSTS_KEYS_SET, key);
                    
                    // Set TTL for tracking set (unconditional - minimal cost)
                    operations.expire(FILTERED_POSTS_KEYS_SET, 1, TimeUnit.DAYS);
                    
                    // Cache the filtered posts
                    operations.opsForValue().set(key, filteredPosts, ttl, timeUnit);
                    
                    return operations.exec();
                }
            });
        } catch (Exception e) {
            log.error("Failed to atomically track and cache filtered posts key {}: {}", key, e.getMessage());
        }
    }
    
    /**
     * Helper method to get keys from a Redis set with proper type conversion
     */
    private Set<String> getKeysFromSet(String redisSetKey) {
        try {
            Set<Object> rawKeys = objectRedisTemplate.opsForSet().members(redisSetKey);
            if (rawKeys == null) return Set.of();
            return rawKeys.stream()
                          .filter(String.class::isInstance)
                          .map(String.class::cast)
                          .collect(Collectors.toSet());
        } catch (Exception e) {
            log.error("Failed to get tracked keys for {}: {}", redisSetKey, e.getMessage());
            return Set.of();
        }
    }
    
    /**
     * Get all tracked post keys
     */
    public Set<String> getTrackedPostKeys() {
        return getKeysFromSet(POST_KEYS_SET);
    }
    
    /**
     * Get all tracked post list keys
     */
    public Set<String> getTrackedPostListKeys() {
        return getKeysFromSet(POST_LIST_KEYS_SET);
    }
    
    /**
     * Get all tracked user posts keys
     */
    public Set<String> getTrackedUserPostsKeys() {
        return getKeysFromSet(USER_POSTS_KEYS_SET);
    }
    
    /**
     * Get all tracked filtered posts keys
     */
    public Set<String> getTrackedFilteredPostsKeys() {
        return getKeysFromSet(FILTERED_POSTS_KEYS_SET);
    }
    
    /**
     * Remove a key from tracking
     */
    public void untrackKey(String key) {
        try {
            objectRedisTemplate.opsForSet().remove(POST_KEYS_SET, key);
            objectRedisTemplate.opsForSet().remove(POST_LIST_KEYS_SET, key);
            objectRedisTemplate.opsForSet().remove(USER_POSTS_KEYS_SET, key);
            objectRedisTemplate.opsForSet().remove(FILTERED_POSTS_KEYS_SET, key);
        } catch (Exception e) {
            log.error("Failed to untrack key {}: {}", key, e.getMessage());
        }
    }
    
    /**
     * Clear all tracking sets
     */
    public void clearAllTracking() {
        try {
            objectRedisTemplate.delete(POST_KEYS_SET);
            objectRedisTemplate.delete(POST_LIST_KEYS_SET);
            objectRedisTemplate.delete(USER_POSTS_KEYS_SET);
            objectRedisTemplate.delete(FILTERED_POSTS_KEYS_SET);
            log.info("Cleared all Redis key tracking sets");
        } catch (Exception e) {
            log.error("Failed to clear tracking sets: {}", e.getMessage());
        }
    }
}
