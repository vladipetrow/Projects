package com.example.workproject1.coreServices.PostService;

import com.example.workproject1.coreServices.CloudinaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for handling post image operations
 */
@Service
public class PostImageService {
    private static final Logger log = LoggerFactory.getLogger(PostImageService.class);
    
    private final CloudinaryService cloudinaryService;
    
    public PostImageService(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }
    
    /**
     * Upload images and return URLs
     */
    public List<String> uploadImages(List<MultipartFile> images) {
        if (images == null || images.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile image : images) {
            try {
                String imageUrl = cloudinaryService.uploadImage(image);
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    imageUrls.add(imageUrl);
                }
            } catch (Exception e) {
                log.error("Failed to upload image: {}", e.getMessage());
            }
        }
        
        return imageUrls;
    }
    
    /**
     * Delete images from Cloudinary
     */
    public void deleteImages(List<String> imageUrls) {
        if (imageUrls == null || imageUrls.isEmpty()) {
            return;
        }
        
        for (String imageUrl : imageUrls) {
            try {
                cloudinaryService.deleteImage(imageUrl);
            } catch (Exception e) {
                log.error("Failed to delete image {}: {}", imageUrl, e.getMessage());
            }
        }
    }
    
    /**
     * Get image URLs from MultipartFile list
     */
    public List<String> getImageUrls(List<MultipartFile> images) {
        if (images == null || images.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile image : images) {
            if (image != null && !image.isEmpty()) {
                try {
                    String imageUrl = cloudinaryService.uploadImage(image);
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        imageUrls.add(imageUrl);
                    }
                } catch (Exception e) {
                    log.error("Failed to process image: {}", e.getMessage());
                }
            }
        }
        
        return imageUrls;
    }
}
