package com.example.workproject1.coreServices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Service for tracking Redis cache keys to avoid expensive keys() operations
 */
@Service
public class RedisKeyTracker {
    private static final Logger log = LoggerFactory.getLogger(RedisKeyTracker.class);
    
    private final RedisTemplate<String, Object> objectRedisTemplate;
    
    @Value("${cache.tracking.ttl:1d}")
    private Duration trackingTtl;
    
    public RedisKeyTracker(RedisTemplate<String, Object> objectRedisTemplate) {
        this.objectRedisTemplate = objectRedisTemplate;
    }

    /**
     * Atomically track a post key and cache the post
     */
    public void trackPostKeyAtomically(String key, Object post, long ttl, TimeUnit timeUnit) {
        trackKeyAndCache(key, post, ttl, timeUnit, CacheCategory.POST);
    }
    
    /**
     * Atomically track a post list key and cache the list
     */
    public void trackPostListKeyAtomically(String key, Object postList, long ttl, TimeUnit timeUnit) {
        trackKeyAndCache(key, postList, ttl, timeUnit, CacheCategory.POST_LIST);
    }
    
    /**
     * Atomically track a user posts key and cache the list
     */
    public void trackUserPostsKeyAtomically(String key, Object userPosts, long ttl, TimeUnit timeUnit) {
        trackKeyAndCache(key, userPosts, ttl, timeUnit, CacheCategory.USER_POSTS);
    }
    
    /**
     * Atomically track a filtered posts key and cache the list
     */
    public void trackFilteredPostsKeyAtomically(String key, Object filteredPosts, long ttl, TimeUnit timeUnit) {
        trackKeyAndCache(key, filteredPosts, ttl, timeUnit, CacheCategory.FILTERED_POSTS);
    }
    
    /**
     * Generalized helper method to track a key and cache a value atomically
     */
    private void trackKeyAndCache(String key, Object value, long ttl, TimeUnit timeUnit, CacheCategory category) {
        try {
            // Check if tracking set exists before starting transaction
            boolean trackingSetExists = objectRedisTemplate.hasKey(category.getRedisKey());
            
            objectRedisTemplate.execute(new SessionCallback<Object>() {
                @Override
                @SuppressWarnings({"unchecked"})
                public Object execute(RedisOperations operations) {
                    operations.multi();
                    
                    // Add to tracking set using enum's Redis key
                    operations.opsForSet().add(category.getRedisKey(), key);
                    
                    // Set TTL only if tracking set is new (prevents sliding expiration)
                    if (!trackingSetExists) {
                        operations.expire(category.getRedisKey(), trackingTtl.toSeconds(), TimeUnit.SECONDS);
                    }
                    
                    // Cache the value
                    operations.opsForValue().set(key, value, ttl, timeUnit);
                    
                    return operations.exec();
                }
            });
            
            log.debug("Successfully tracked and cached {} key: {}", category.getValue(), key);
        } catch (Exception e) {
            log.error("Failed to atomically track and cache {} key {}: {}", category.getValue(), key, e.getMessage());
            // For critical caching operations, consider re-throwing the exception
            // throw new RuntimeException("Critical cache operation failed", e);
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
        return getKeysFromSet(CacheCategory.POST.getRedisKey());
    }
    
    /**
     * Get all tracked post list keys
     */
    public Set<String> getTrackedPostListKeys() {
        return getKeysFromSet(CacheCategory.POST_LIST.getRedisKey());
    }
    
    /**
     * Get all tracked user posts keys
     */
    public Set<String> getTrackedUserPostsKeys() {
        return getKeysFromSet(CacheCategory.USER_POSTS.getRedisKey());
    }
    
    /**
     * Get all tracked filtered posts keys
     */
    public Set<String> getTrackedFilteredPostsKeys() {
        return getKeysFromSet(CacheCategory.FILTERED_POSTS.getRedisKey());
    }
    
    /**
     * Remove a key from tracking (category-aware)
     */
    public void untrackKey(String key, CacheCategory category) {
        try {
            objectRedisTemplate.opsForSet().remove(category.getRedisKey(), key);
            log.debug("Untracked {} key: {}", category.getValue(), key);
        } catch (Exception e) {
            log.error("Failed to untrack {} key {}: {}", category.getValue(), key, e.getMessage());
        }
    }
    
    /**
     * Batch untrack multiple keys from a specific category (more efficient)
     */
    public void untrackKeys(Set<String> keys, CacheCategory category) {
        if (keys == null || keys.isEmpty()) return;
        
        try {
            objectRedisTemplate.executePipelined(new SessionCallback<Object>() {
                @Override
                @SuppressWarnings({"unchecked"})
                public Object execute(RedisOperations operations) {
                    operations.opsForSet().remove(category.getRedisKey(), keys.toArray());
                    return null;
                }
            });
            log.debug("Batch untracked {} {} keys", keys.size(), category.getValue());
        } catch (Exception e) {
            log.error("Failed to batch untrack {} keys for category {}: {}", keys.size(), category.getValue(), e.getMessage());
        }
    }
    
    /**
     * Remove a key from all tracking sets (use with caution)
     */
    public void untrackKeyFromAll(String key) {
        try {
            objectRedisTemplate.executePipelined(new SessionCallback<Object>() {
                @Override
                @SuppressWarnings({"unchecked"})
                public Object execute(RedisOperations operations) {
                    operations.opsForSet().remove(CacheCategory.POST.getRedisKey(), key);
                    operations.opsForSet().remove(CacheCategory.POST_LIST.getRedisKey(), key);
                    operations.opsForSet().remove(CacheCategory.USER_POSTS.getRedisKey(), key);
                    operations.opsForSet().remove(CacheCategory.FILTERED_POSTS.getRedisKey(), key);
                    return null;
                }
            });
            log.debug("Untracked key from all sets: {}", key);
        } catch (Exception e) {
            log.error("Failed to untrack key from all sets {}: {}", key, e.getMessage());
        }
    }

    /**
     * Clear all tracking sets and their cached values
     */
    public void clearAllTracking() {
        try {
            // Get all tracked keys before clearing tracking sets
            Set<String> allTrackedKeys = new HashSet<>();
            allTrackedKeys.addAll(getTrackedPostKeys());
            allTrackedKeys.addAll(getTrackedPostListKeys());
            allTrackedKeys.addAll(getTrackedUserPostsKeys());
            allTrackedKeys.addAll(getTrackedFilteredPostsKeys());
            
            // Delete all cached values
            if (!allTrackedKeys.isEmpty()) {
                objectRedisTemplate.delete(allTrackedKeys);
                log.info("Deleted {} cached values", allTrackedKeys.size());
            }
            
            // Clear all tracking sets using enum keys
            objectRedisTemplate.delete(CacheCategory.POST.getRedisKey());
            objectRedisTemplate.delete(CacheCategory.POST_LIST.getRedisKey());
            objectRedisTemplate.delete(CacheCategory.USER_POSTS.getRedisKey());
            objectRedisTemplate.delete(CacheCategory.FILTERED_POSTS.getRedisKey());
            
            log.info("Cleared all Redis key tracking sets and cached values");
        } catch (Exception e) {
            log.error("Failed to clear tracking sets and cached values: {}", e.getMessage());
        }
    }
}
