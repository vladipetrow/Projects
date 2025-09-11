package com.example.workproject1.repositories;

import com.example.workproject1.coreServices.models.ApartmentType;
import com.example.workproject1.coreServices.models.TransactionType;
import com.example.workproject1.repositories.models.PostDAO;
import com.example.workproject1.web.api.models.PostInput;

import java.util.List;

public interface PostRepository {
    PostDAO createPost(String location, int price, int area, String description, int user_id, int agency_id,
                       int typeOfApartId, TransactionType transactionType, List<String> imageUrls);
    List<PostDAO> filterBy(String location, int price, ApartmentType apartmentType, TransactionType transactionType);
    List<PostDAO> filterByGeneral(String location, int price, TransactionType transactionType);
    List<PostDAO> getPostsForUser(int user_id);
    List<PostDAO> getPostsForAgency(int agency_id);
    int getNumberOfPostsForUser(int user_id);
    int getNumberOfPostsForAgency(int agency_id);
    PostDAO getPost(int id);
    List<PostDAO> listPosts(int page, int pageSize);
    List<PostDAO> listPostsWithImages(int page, int pageSize);
    int getTotalPostsCount();
    void deletePost(int id);
    void addImagesToPost(int postId, List<String> imagesURL);
    List<PostDAO> getUserPosts(int userId);
    void incrementViewCount(int postId);
    void updatePost(int id, PostInput postInput, List<String> newImageUrls, List<String> imagesToDelete);
}
