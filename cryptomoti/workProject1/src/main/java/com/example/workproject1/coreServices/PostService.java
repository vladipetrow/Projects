package com.example.workproject1.coreServices;

import com.example.workproject1.coreServices.ServiceExeptions.*;
import com.example.workproject1.coreServices.models.ApartmentType;
import com.example.workproject1.coreServices.models.Post;
import com.example.workproject1.repositories.PostRepository;
import com.example.workproject1.repositories.models.PostDAO;
import com.example.workproject1.web.WebExeptions.SubscriptionExpired;
import org.springframework.dao.DataAccessException;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class PostService {
    private final PostRepository repository;
    private final SubscriptionService subscriptionService;

    public PostService(PostRepository repository, SubscriptionService subscriptionService) {
        this.repository = repository;
        this.subscriptionService = subscriptionService;
    }

    public Post createPost(String location, int price, int area,
                           String description, int user_id, int agency_id, ApartmentType type) {
        if (price < 0) {
            throw new InvalidPrice();
        }
        if (area < 0) {
            throw new InvalidArea();
        }

        try {
            return Mappers.fromPostDAO(
                    repository.createPost(location, price, area, description, user_id, agency_id, type));
        } catch (DataAccessException e) {
            throw new InvalidParametersForAgency();
        }
    }

    public List<PostDAO> filterBy(String location, int price, ApartmentType type) {
        return repository.filterBy(location, price, type);
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

    public void deletePost(int id) {
        try {
            repository.deletePost(id);
        } catch (DataAccessException e) {
            throw new InvalidPostId();
        }
    }
}
