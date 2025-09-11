package com.example.workproject1.coreServices;

import com.example.workproject1.coreServices.ServiceExeptions.InvalidPostIdException;
import com.example.workproject1.coreServices.ServiceExeptions.PostUpdateException;
import com.example.workproject1.coreServices.ServiceExeptions.PostDeleteException;
import com.example.workproject1.coreServices.models.ApartmentType;
import com.example.workproject1.coreServices.models.Post;
import com.example.workproject1.coreServices.models.TransactionType;
import com.example.workproject1.repositories.PostRepository;
import com.example.workproject1.repositories.models.PostDAO;
import com.example.workproject1.web.api.models.PostInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;


public class PostService {
    private static final Logger log = LoggerFactory.getLogger(PostService.class);
    private final PostRepository repository;
    private final PostCacheService postCacheService;
    private final PostImageService postImageService;
    private final PostValidationService postValidationService;

    public PostService(PostRepository repository, PostCacheService postCacheService,
                      PostImageService postImageService, PostValidationService postValidationService) {
        this.repository = repository;
        this.postCacheService = postCacheService;
        this.postImageService = postImageService;
        this.postValidationService = postValidationService;
    }

    public Post createPost(PostInput postInput, int userId, int agencyId, List<MultipartFile> images) {
        log.info("Creating post for user: {}, agency: {}, location: {}", userId, agencyId, postInput.getLocation());
        
        // Validate input and subscription limits
        postValidationService.validatePostCreation(postInput, userId, agencyId);

        try {
            // Upload images to Cloudinary and get URLs
            List<String> imageUrls = postImageService.uploadImages(images);

            // Convert ApartmentType enum to database ID (enum ordinal + 1)
            int typeOfApartId = postInput.getApartmentType().ordinal() + 1;
            PostDAO postDAO = repository.createPost(
                postInput.getLocation(), 
                postInput.getPrice(), 
                postInput.getArea(), 
                postInput.getDescription(), 
                userId, 
                agencyId, 
                typeOfApartId, 
                postInput.getTransactionType(), 
                imageUrls
            );
            Post post = Mappers.fromPostDAO(postDAO);

            // Cache the new post
            postCacheService.cachePost(post);
            
            // Smart invalidation - only invalidate what's affected
            postCacheService.invalidatePostAndRelatedCaches(postDAO.getPostId(), userId);

            log.info("Post created successfully with ID: {}", postDAO.getPostId());
            return post;
        } catch (DataAccessException e) {
            log.error("Database error creating post: {}", e.getMessage(), e);
            throw new PostUpdateException("Failed to create post: " + e.getMessage(), e);
        }
    }

    public List<PostDAO> filterBy(String location, int price, ApartmentType apartmentType, TransactionType transactionType) {
        return repository.filterBy(location, price, apartmentType, transactionType);
    }
    
    public List<PostDAO> filterByGeneral(String location, int price, TransactionType transactionType) {
        return repository.filterByGeneral(location, price, transactionType);
    }
    
    public List<Post> filterByPosts(ApartmentType apartmentType, TransactionType transactionType) {
        log.info("Filtering posts by apartment type: {}, transaction type: {}", apartmentType, transactionType);
        
        // Try cache first
        List<Post> cachedPosts = postCacheService.getCachedFilteredPosts(
            apartmentType.toString(), transactionType.toString());
        if (cachedPosts != null) {
            return cachedPosts;
        }
        
        // Not in cache, fetch from database
        List<PostDAO> postDAOs = repository.filterBy("", 0, apartmentType, transactionType);
        List<Post> posts = mapPostDAOsToPosts(postDAOs);
        
        // Cache the results
        postCacheService.cacheFilteredPosts(apartmentType.toString(), transactionType.toString(), posts);
        
        return posts;
    }
    
    /**
     * Helper method to convert PostDAO list to Post list
     */
    private List<Post> mapPostDAOsToPosts(List<PostDAO> postDAOs) {
        return postDAOs.stream()
                .map(Mappers::fromPostDAO)
                .collect(Collectors.toList());
    }




    public List<Post> getPostsForAgency(int id) {
        return mapPostDAOsToPosts(repository.getPostsForAgency(id));
    }

    public List<Post> listPosts(int page, int pageSize) {
        log.debug("Fetching posts - page: {}, pageSize: {}", page, pageSize);
        
        // Try cache first
        List<Post> cachedPosts = postCacheService.getCachedPostList(page, pageSize);
        if (cachedPosts != null) {
            return cachedPosts;
        }
        
        // Not in cache, fetch from database
        List<Post> posts = mapPostDAOsToPosts(repository.listPosts(page, pageSize));
        
        // Cache the results
        postCacheService.cachePostList(page, pageSize, posts);
        
        return posts;
    }

    /**
     * Optimized method to list posts with images.
     * Uses efficient querying to prevent N+1 query problem.
     */
    public List<Post> listPostsWithImages(int page, int pageSize) {
        log.debug("Fetching posts with images - page: {}, pageSize: {}", page, pageSize);
        return mapPostDAOsToPosts(repository.listPostsWithImages(page, pageSize));
    }

    public int getTotalPostsCount() {
        return repository.getTotalPostsCount();
    }

    public List<Post> getUserPosts(int userId) {
        log.debug("Getting posts for user: {}", userId);
        
        // Try cache first
        List<Post> cachedPosts = postCacheService.getCachedUserPosts(userId);
        if (cachedPosts != null) {
            return cachedPosts;
        }
        
        // Not in cache, fetch from database
        List<Post> posts = mapPostDAOsToPosts(repository.getUserPosts(userId));
        
        // Cache the results
        postCacheService.cacheUserPosts(userId, posts);
        
        return posts;
    }

    public Post getPost(int id) {
        log.debug("Fetching post with ID: {}", id);
        
        if (id <= 0) {
            throw new InvalidPostIdException("Post ID must be positive");
        }
        
        // Try cache first
        Post cachedPost = postCacheService.getCachedPost(id);
        if (cachedPost != null) {
            return cachedPost;
        }
        
        try {
            PostDAO postDAO = repository.getPost(id);
            if (postDAO == null) {
                log.warn("Post not found with ID: {}", id);
                throw new InvalidPostIdException("Post not found with ID: " + id);
            }
            
            Post post = Mappers.fromPostDAO(postDAO);
            
            // Cache the post
            postCacheService.cachePost(post);
            
            log.debug("Post {} fetched successfully", id);
            return post;
        } catch (InvalidPostIdException e) {
            // Re-throw our custom exceptions
            throw e;
        } catch (Exception e) {
            log.error("Error fetching post {}: {}", id, e.getMessage(), e);
            throw new InvalidPostIdException("Failed to fetch post with ID: " + id);
        }
    }

    public void incrementViewCount(int postId) {
        repository.incrementViewCount(postId);
    }


    public int getNumberOfPostsForUser(int user_id) {
        return repository.getNumberOfPostsForUser(user_id);
    }

    public int getNumberOfPostsForAgency(int user_id) {
        return repository.getNumberOfPostsForAgency(user_id);
    }

    public void updatePost(int id, PostInput postInput, List<MultipartFile> newImages, List<String> imagesToDelete) {
        log.info("Updating post with ID: {}", id);
        
        if (id <= 0) {
            throw new InvalidPostIdException("Post ID must be positive");
        }
        
        try {
            // Validate input and check if post exists
            postValidationService.validatePostUpdate(postInput, id);
            
            // Get existing post for user ID
            PostDAO existingPost = repository.getPost(id);
            
            // Delete specified images from Cloudinary
            if (imagesToDelete != null && !imagesToDelete.isEmpty()) {
                log.info("Deleting {} images from Cloudinary for post {}", imagesToDelete.size(), id);
                postImageService.deleteImages(imagesToDelete);
            }
            
            // Upload new images to Cloudinary
            List<String> newImageUrls = postImageService.uploadImages(newImages);
            
            // Update post in database
            repository.updatePost(id, postInput, newImageUrls, imagesToDelete);
            
            // Smart invalidation - only invalidate what's affected
            postCacheService.invalidatePostAndRelatedCaches(id, existingPost.getUserId());
            
            log.info("Post {} updated successfully", id);
            
        } catch (InvalidPostIdException e) {
            throw e;
        } catch (DataAccessException e) {
            log.error("Database error updating post {}: {}", id, e.getMessage(), e);
            throw new PostUpdateException("Failed to update post with ID: " + id, e);
        } catch (Exception e) {
            log.error("Error updating post {}: {}", id, e.getMessage(), e);
            throw new PostUpdateException("Failed to update post with ID: " + id, e);
        }
    }

    public void deletePost(int id) {
        log.info("Deleting post with ID: {}", id);
        
        if (id <= 0) {
            throw new InvalidPostIdException("Post ID must be positive");
        }
        
        try {
            // Get post and validate deletion
            PostDAO postDAO = repository.getPost(id);
            postValidationService.validatePostDeletion(id, postDAO.getUserId());
            
            // Delete images from Cloudinary
            if (postDAO.getImageUrls() != null && !postDAO.getImageUrls().isEmpty()) {
                log.info("Deleting {} images from Cloudinary for post {}", postDAO.getImageUrls().size(), id);
                postImageService.deleteImages(postDAO.getImageUrls());
            }
            
            // Delete post from database
            repository.deletePost(id);
            
            // Smart invalidation - only invalidate what's affected
            postCacheService.invalidatePostAndRelatedCaches(id, postDAO.getUserId());
            
            log.info("Post {} deleted successfully", id);
            
        } catch (InvalidPostIdException e) {
            throw e;
        } catch (DataAccessException e) {
            log.error("Database error deleting post {}: {}", id, e.getMessage(), e);
            throw new PostDeleteException("Failed to delete post with ID: " + id, e);
        } catch (Exception e) {
            log.error("Error deleting post {}: {}", id, e.getMessage(), e);
            throw new PostDeleteException("Failed to delete post with ID: " + id, e);
        }
    }
}
