package com.example.workproject1.web.api;

import com.example.workproject1.coreServices.PostService;
import com.example.workproject1.coreServices.models.ApartmentType;
import com.example.workproject1.coreServices.models.Post;
import com.example.workproject1.repositories.models.PostDAO;
import com.example.workproject1.security.JwtUtil;

import com.example.workproject1.web.api.models.PostInput;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final JwtUtil jwtUtil;

    public PostController(PostService postService, JwtUtil jwtUtil) {
        this.postService = postService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/add")
    public Post createPost(@RequestBody PostInput postInput,  @CookieValue("Authorization") String token,
                           @RequestParam("images") List<MultipartFile> images) {
        int userId = 0;
        int agencyId = 0;

        String role = jwtUtil.getRoleFromToken(token);
        if ("[ROLE_USER]".equals(role)) {
            userId = jwtUtil.getIdFromToken(token);
        } else {
            agencyId = jwtUtil.getIdFromToken(token);
        }

        postService.validateSubscriptionAndPostLimit(userId, agencyId);

        return postService.createPost(
                postInput.getLocation(),
                postInput.getPrice(),
                postInput.getArea(),
                postInput.getDescription(),
                userId,
                agencyId,
                postInput.getType(),
                images
        );
    }

    @GetMapping("/filter")
    public List<PostDAO> filterByLocationPriceType(@RequestParam String location, @RequestParam int price, @RequestParam ApartmentType type) {
        return postService.filterBy(location, price, type);
    }

    @GetMapping("list/posts")
    public List<Post> listPosts(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "20") int pageSize) {
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

//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<?> deletePost(@PathVariable int id) {
//        postService.deletePost(id);
//        return ResponseEntity.ok(Map.of("message", "Post deleted successfully"));
//    }

}

