package com.example.workproject1.repositories;

import com.example.workproject1.coreServices.models.ApartmentType;
import com.example.workproject1.repositories.models.PostDAO;

import java.util.List;

public interface PostRepository {
    PostDAO createPost(String location, int price, int area, String description, int user_id, int agency_id,
                       ApartmentType type, List<String> imageUrls);
    List<PostDAO> filterBy(String location, int price, ApartmentType type);
    List<PostDAO> getPostsForUser(int user_id);
    List<PostDAO> getPostsForAgency(int agency_id);
    int getNumberOfPostsForUser(int user_id);
    int getNumberOfPostsForAgency(int agency_id);
    PostDAO getPost(int id);
    List<PostDAO> listPosts(int page, int pageSize);
    void deletePost(int id);
    void addImagesToPost(int postId, List<String> imagesURL);
}
