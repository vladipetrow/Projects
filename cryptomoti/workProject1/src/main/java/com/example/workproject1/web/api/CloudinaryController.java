package com.example.workproject1.web.api;

import com.example.workproject1.coreServices.CloudinaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/upload")
public class CloudinaryController {
    
    private static final Logger logger = LoggerFactory.getLogger(CloudinaryController.class);
    private final CloudinaryService cloudinaryService;

    public CloudinaryController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            logger.info("Uploading image file: {}", file.getOriginalFilename());
            
            // Validate file
            if (file == null || file.isEmpty()) {
                logger.warn("Empty or null file provided for upload");
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "No file provided"));
            }
            
            // Check file size (e.g., max 10MB)
            if (file.getSize() > 10 * 1024 * 1024) {
                logger.warn("File too large: {} bytes", file.getSize());
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "File size too large. Maximum size is 10MB"));
            }
            
            // Check file type
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                logger.warn("Invalid file type: {}", contentType);
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "File must be an image"));
            }
            
            String imageUrl = cloudinaryService.uploadImage(file);
            logger.info("Image uploaded successfully: {}", imageUrl);
            
            return ResponseEntity.ok(Map.of("imageUrl", imageUrl));
            
        } catch (Exception e) {
            logger.error("Error uploading image: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to upload image: " + e.getMessage()));
        }
    }
}
