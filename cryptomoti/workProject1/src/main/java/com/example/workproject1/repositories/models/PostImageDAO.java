package com.example.workproject1.repositories.models;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * Data Access Object for PostImage entities.
 * Immutable data transfer object with Builder pattern.
 */
public final class PostImageDAO {
    private final Integer id;
    private final Integer postId;
    private final String imageUrl;
    private final Timestamp createdAt;

    private PostImageDAO(Builder builder) {
        this.id = builder.id;
        this.postId = builder.postId;
        this.imageUrl = builder.imageUrl;
        this.createdAt = builder.createdAt;
    }

    // Getters only - no setters for immutability
    public Integer getId() {
        return id;
    }

    public Integer getPostId() {
        return postId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    /**
     * Checks if the image URL is valid.
     * 
     * @return true if imageUrl is not null and not empty
     */
    public boolean hasValidImageUrl() {
        return imageUrl != null && !imageUrl.trim().isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostImageDAO)) return false;
        PostImageDAO that = (PostImageDAO) o;
        return Objects.equals(id, that.id) && 
               Objects.equals(postId, that.postId) && 
               Objects.equals(imageUrl, that.imageUrl) &&
               Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, postId, imageUrl, createdAt);
    }

    @Override
    public String toString() {
        return "PostImageDAO{" +
                "id=" + id +
                ", postId=" + postId +
                ", imageUrl='" + imageUrl + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    /**
     * Builder pattern for creating PostImageDAO instances.
     * Follows Effective Java Item 2 - Builder pattern for many parameters.
     */
    public static class Builder {
        private Integer id;
        private Integer postId;
        private String imageUrl;
        private Timestamp createdAt;

        public Builder() {}

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder postId(Integer postId) {
            this.postId = postId;
            return this;
        }

        public Builder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder createdAt(Timestamp createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public PostImageDAO build() {
            return new PostImageDAO(this);
        }
    }
}


