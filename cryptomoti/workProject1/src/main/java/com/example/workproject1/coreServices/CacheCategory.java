package com.example.workproject1.coreServices;

/**
 * Enum for cache categories with their Redis tracking keys.
 */
public enum CacheCategory {
    POST("post", "cryptomoti:tracking:posts"),
    POST_LIST("post_list", "cryptomoti:tracking:post_lists"),
    USER_POSTS("user_posts", "cryptomoti:tracking:user_posts"),
    FILTERED_POSTS("filtered_posts", "cryptomoti:tracking:filtered_posts");

    private final String value;
    private final String redisKey;

    CacheCategory(String value, String redisKey) {
        this.value = value;
        this.redisKey = redisKey;
    }

    public String getValue() { return value; }
    public String getRedisKey() { return redisKey; }

    @Override
    public String toString() { return value; }
}
