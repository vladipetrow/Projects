package com.example.workproject1.web.api;

import com.example.workproject1.coreServices.Mappers;
import com.example.workproject1.coreServices.PostService;
import com.example.workproject1.coreServices.ServiceExeptions.MaxNumberOfPosts;
import com.example.workproject1.coreServices.ServiceExeptions.YouNeedSubscription;
import com.example.workproject1.coreServices.SubscriptionService;
import com.example.workproject1.coreServices.UserService;
import com.example.workproject1.coreServices.models.ApartmentType;
import com.example.workproject1.coreServices.models.Post;
import com.example.workproject1.repositories.models.PostDAO;
import com.example.workproject1.web.WebExeptions.SubscriptionExpired;
import com.example.workproject1.web.api.models.PostInput;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping
public class PostController {
    private final PostService postService;

    private final UserService userService;

    private final SubscriptionService subscriptionService;

    public PostController(PostService postService, UserService userService, SubscriptionService subscriptionService) {
        this.postService = postService;
        this.userService = userService;
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/post/add")
    public Post createPost(@RequestBody PostInput postInput, @RequestHeader("Authorization" ) String token) throws IOException {
        int userId = 0;
        int agencyId = 0;

        String userRole  = userService.getRoleFromToken(token);
            if (userRole.equals("[ROLE_USER]")) {
                userId = userService.getIdFromToken(token);
            } else {
                agencyId = userService.getIdFromToken(token);
            }

            Timestamp curr_date = new Timestamp(System.currentTimeMillis());

        int numberOfPostsForUser = postService.getNumberOfPostsForUser(userId);
        int numberOfPostsForAgency = postService.getNumberOfPostsForAgency(agencyId);

        if ((numberOfPostsForUser >= 5 && !subscriptionService.listSubscribedUserByID(userId).isEmpty())
                || (numberOfPostsForAgency >= 10  && !subscriptionService.listSubscribedAgencyByID(agencyId).isEmpty())){
            throw new MaxNumberOfPosts();
        }
        if (numberOfPostsForUser >= 3 || numberOfPostsForAgency >= 5)
            throw new YouNeedSubscription();


        if(!subscriptionService.listSubscribedUserByID(userId).isEmpty())
            if (curr_date.before(subscriptionService.getUserExpirationDate(userId)))
                throw new SubscriptionExpired();

        if(!subscriptionService.listSubscribedAgencyByID(agencyId).isEmpty())
            if(curr_date.before(subscriptionService.getAgencyExpirationDate(agencyId)))
                throw new SubscriptionExpired();


        return postService.createPost(postInput.location,postInput.price,
    postInput.area,postInput.description,userId,agencyId,postInput.type);
}

    @GetMapping("/filter/posts")
    public List<Post> filterByLocationPriceType(@RequestParam String location, @RequestParam int price, @RequestParam ApartmentType type){
        List<Post> listPost = new ArrayList<>();
        List<PostDAO> listPostDAO = postService.filterBy(location, price, type);
        for (PostDAO ps: listPostDAO) {
            listPost.add(Mappers.fromPostDAO(ps));
        }
        return listPost;
    }

    @GetMapping("/list/posts")
    public List<Post> listPosts(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        return postService.listPosts(page, pageSize);
    }

    @GetMapping(value = "get/user/posts/{id}")
    public List<Post> getUserPost(@PathVariable Integer id) {
        return postService.getPostsForUser(id);
    }

    //Only to see if its working(its working)

//    @GetMapping(value = "get/user/expiration_date")
//    public Timestamp getUserExpirationDate(@RequestHeader("Authorization" ) String token){
//        int userId = userService.getIdFromToken(token);
//        return subscriptionService.getUserExpirationDate(userId);
//    }
//    @GetMapping(value = "get/agency/expiration_date")
//    public Timestamp getAgencyExpirationDate(@RequestHeader("Authorization" ) String token){
//        int agencyId = userService.getIdFromToken(token);
//        return subscriptionService.getAgencyExpirationDate(agencyId);
//    }
    @GetMapping(value = "get/user/number/posts/{user_id}")
    public int getNumberOfPostsForUser(@PathVariable int user_id){
        return postService.getNumberOfPostsForUser(user_id);
    }
    @GetMapping(value = "get/agency/number/posts/{agency_id}")
    public int getNumberOfPostsForAgency(@PathVariable int agency_id){
        return postService.getNumberOfPostsForAgency(agency_id);
    }
    @GetMapping(value = "get/agency/posts/{id}")
    public List<Post> getAgencyPost(@PathVariable Integer id) {
        return postService.getPostsForAgency(id);
    }

    @GetMapping(value = "get/post/{id}")
    public Post getPost(@PathVariable Integer id) {
        return postService.getPost(id);
    }

    @DeleteMapping(value = "/delete/post/{id}")
    public void deletePost(@PathVariable Integer id) {
        postService.deletePost(id);
    }
}
