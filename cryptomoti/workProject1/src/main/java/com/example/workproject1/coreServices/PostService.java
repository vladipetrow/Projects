package com.example.workproject1.coreServices;

import com.example.workproject1.coreServices.ServiceExeptions.*;
import com.example.workproject1.coreServices.models.ApartmentType;
import com.example.workproject1.coreServices.models.Post;
import com.example.workproject1.repositories.PostRepository;
import com.example.workproject1.repositories.models.PostDAO;
import com.example.workproject1.web.WebExeptions.SubscriptionExpired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class PostService {
    private final PostRepository repository;
    private final SubscriptionService subscriptionService;
    private final CloudinaryService cloudinaryService;

    public PostService(PostRepository repository, SubscriptionService subscriptionService, CloudinaryService cloudinaryService) {
        this.repository = repository;
        this.subscriptionService = subscriptionService;
        this.cloudinaryService = cloudinaryService;
    }

    public Post createPost(String location, int price, int area, String description,
                           int userId, int agencyId, ApartmentType type, List<MultipartFile> images) {
        if (price < 0) throw new InvalidPrice();
        if (area < 0) throw new InvalidArea();

        try {
            // Upload images to Cloudinary and get URLs
            List<String> imageUrls = getImageUrls(images);

            PostDAO postDAO = repository.createPost(location, price, area, description, userId, agencyId, type, imageUrls);

            return Mappers.fromPostDAO(postDAO);
        } catch (DataAccessException e) {
            throw new InvalidParametersForAgency();
        }
    }

    public List<PostDAO> filterBy(String location, int price, ApartmentType type) {
        return repository.filterBy(location, price, type);
    }

    public List<String> getImageUrls(List<MultipartFile> images) {
        return images.stream()
                .map(cloudinaryService::uploadImage)
                .collect(Collectors.toList());
    }

    public Post getPost(int id) {
        try {
            return Mappers.fromPostDAO(repository.getPost(id));
        } catch (DataAccessException e) {
            throw new InvalidPostId();
        }
    }

    public List<Post> getPostsForUser(int id) {
        return repository.getPostsForUser(id)
                .stream()
                .map(Mappers::fromPostDAO)
                .collect(Collectors.toList());
    }

    public List<Post> getPostsForAgency(int id) {
        return repository.getPostsForAgency(id)
                .stream()
                .map(Mappers::fromPostDAO)
                .collect(Collectors.toList());
    }

    public List<Post> listPosts(int page, int pageSize) {
        return repository.listPosts(page, pageSize)
                .stream()
                .map(Mappers::fromPostDAO)
                .collect(Collectors.toList());
    }

    public void validateSubscriptionAndPostLimit(int userId, int agencyId) {
        Timestamp currDate = new Timestamp(System.currentTimeMillis());

        int userPostCount = getNumberOfPostsForUser(userId);
        int agencyPostCount = getNumberOfPostsForAgency(agencyId);

        if ((userPostCount >= 5 && subscriptionService.listSubscribedUsersById(userId).isEmpty()) ||
                (agencyPostCount >= 10 && subscriptionService.listSubscribedAgenciesById(agencyId).isEmpty())) {
            throw new MaxNumberOfPosts();
        }

        if (userPostCount >= 3 || agencyPostCount >= 5) {
            throw new YouNeedSubscription();
        }

        if (!subscriptionService.listSubscribedUsersById(userId).isEmpty()) {
            if (currDate.after(subscriptionService.getUserExpirationDate(userId))) {
                throw new SubscriptionExpired();
            }
        }

        if (!subscriptionService.listSubscribedAgenciesById(agencyId).isEmpty()) {
            if (currDate.after(subscriptionService.getAgencyExpirationDate(agencyId))) {
                throw new SubscriptionExpired();
            }
        }
    }

    public int getNumberOfPostsForUser(int user_id) {
        return repository.getNumberOfPostsForUser(user_id);
    }

    public int getNumberOfPostsForAgency(int user_id) {
        return repository.getNumberOfPostsForAgency(user_id);
    }

//    public void deletePost(int id) {
//        try {
//            // ✅ Delete images from Cloudinary
//            List<String> imageUrls = repository.get(id);
//            imageUrls.forEach(cloudinaryService::deleteImage);
//
//            // ✅ Delete post from database
//            repository.deletePost(id);
//        } catch (DataAccessException e) {
//            throw new InvalidPostId();
//        }
//    }
}
