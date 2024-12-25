package com.example.workproject1.web.api;

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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final SubscriptionService subscriptionService;

    public PostController(PostService postService, UserService userService, SubscriptionService subscriptionService) {
        this.postService = postService;
        this.userService = userService;
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/add")
    public Post createPost(@RequestBody PostInput postInput, @RequestHeader("Authorization") String token) {
        int userId = 0;
        int agencyId = 0;

        String userRole = userService.getRoleFromToken(token);
        if (userRole.equals("[ROLE_USER]")) {
            userId = userService.getIdFromToken(token);
        } else {
            agencyId = userService.getIdFromToken(token);
        }

        validateSubscriptionAndPostLimit(userId, agencyId);

        return postService.createPost(
                postInput.getLocation(),
                postInput.getPrice(),
                postInput.getArea(),
                postInput.getDescription(),
                userId,
                agencyId,
                postInput.getType()
        );
    }

    @GetMapping("/filter")
    public List<PostDAO> filterByLocationPriceType(@RequestParam String location, @RequestParam int price, @RequestParam ApartmentType type) {
        return postService.filterBy(location, price, type);
    }

    @GetMapping
    public List<Post> listPosts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int pageSize) {
        return postService.listPosts(page, pageSize);
    }

    @GetMapping("/user/{id}")
    public List<Post> getUserPosts(@PathVariable int id) {
        return postService.getPostsForUser(id);
    }

    @GetMapping("/agency/{id}")
    public List<Post> getAgencyPosts(@PathVariable int id) {
        return postService.getPostsForAgency(id);
    }

    @GetMapping("/{id}")
    public Post getPost(@PathVariable int id) {
        return postService.getPost(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable int id) {
        postService.deletePost(id);
        return ResponseEntity.ok(Map.of("message", "Post deleted successfully"));
    }

    private void validateSubscriptionAndPostLimit(int userId, int agencyId) {
        Timestamp currDate = new Timestamp(System.currentTimeMillis());

        int userPostCount = postService.getNumberOfPostsForUser(userId);
        int agencyPostCount = postService.getNumberOfPostsForAgency(agencyId);

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
}

