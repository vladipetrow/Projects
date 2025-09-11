package com.example.workproject1.repositories.models;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * Data Access Object for PostView entities.
 */
public final class PostViewDAO {
    private final Integer id;
    private final Integer postId;
    private final Integer userId;
    private final Timestamp viewedAt;
    private final String ipAddress;

    private PostViewDAO(Builder builder) {
        this.id = builder.id;
        this.postId = builder.postId;
        this.userId = builder.userId;
        this.viewedAt = builder.viewedAt;
        this.ipAddress = builder.ipAddress;
    }

    // Getters only - no setters for immutability
    public Integer getId() {
        return id;
    }

    public Integer getPostId() {
        return postId;
    }

    public Integer getUserId() {
        return userId;
    }

    public Timestamp getViewedAt() {
        return viewedAt;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * Checks if the view has a valid post ID.
     * 
     * @return true if postId is not null and greater than 0
     */
    public boolean hasValidPostId() {
        return postId != null && postId > 0;
    }

    /**
     * Checks if the view is from a logged-in user.
     * 
     * @return true if userId is not null and greater than 0
     */
    public boolean isFromLoggedInUser() {
        return userId != null && userId > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostViewDAO)) return false;
        PostViewDAO that = (PostViewDAO) o;
        return Objects.equals(id, that.id) && 
               Objects.equals(postId, that.postId) && 
               Objects.equals(userId, that.userId) &&
               Objects.equals(viewedAt, that.viewedAt) &&
               Objects.equals(ipAddress, that.ipAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, postId, userId, viewedAt, ipAddress);
    }

    @Override
    public String toString() {
        return "PostViewDAO{" +
                "id=" + id +
                ", postId=" + postId +
                ", userId=" + userId +
                ", viewedAt=" + viewedAt +
                ", ipAddress='" + ipAddress + '\'' +
                '}';
    }

    /**
     * Builder pattern for creating PostViewDAO instances.
     */
    public static class Builder {
        private Integer id;
        private Integer postId;
        private Integer userId;
        private Timestamp viewedAt;
        private String ipAddress;

        public Builder() {}

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder postId(Integer postId) {
            this.postId = postId;
            return this;
        }

        public Builder userId(Integer userId) {
            this.userId = userId;
            return this;
        }

        public Builder viewedAt(Timestamp viewedAt) {
            this.viewedAt = viewedAt;
            return this;
        }

        public Builder ipAddress(String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        public PostViewDAO build() {
            return new PostViewDAO(this);
        }
    }
}


