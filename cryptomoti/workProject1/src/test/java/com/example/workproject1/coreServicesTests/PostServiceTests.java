package com.example.workproject1.coreServicesTests;

import com.example.workproject1.coreServices.PostService;
import com.example.workproject1.coreServices.ServiceExeptions.InvalidArea;
import com.example.workproject1.coreServices.ServiceExeptions.InvalidParametersForAgency;
import com.example.workproject1.coreServices.ServiceExeptions.InvalidPostId;
import com.example.workproject1.coreServices.ServiceExeptions.InvalidPrice;
import com.example.workproject1.coreServices.models.ApartmentType;
import com.example.workproject1.coreServices.models.Post;
import com.example.workproject1.repositories.PostRepository;
import com.example.workproject1.repositories.models.PostDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.InvalidDataAccessResourceUsageException;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PostServiceTests {

    @Mock
    private PostRepository repository;

    @InjectMocks
    private PostService postService;

    private final String location = "New York";
    private final int price = 1200;
    private final int area = 100;
    private final String description = "Spacious Apartment";
    private final int userId = 1;
    private final int agencyId = 2;
    private final ApartmentType type = ApartmentType.BUY;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePost_ValidInputs() {
        PostDAO mockPostDAO = new PostDAO(1, location, price, area, description, userId, agencyId, type, new Timestamp(System.currentTimeMillis()));
        when(repository.createPost(location, price, area, description, userId, agencyId, type)).thenReturn(mockPostDAO);

        Post result = postService.createPost(location, price, area, description, userId, agencyId, type);

        assertNotNull(result);
        assertEquals(location, result.getLocation());
        assertEquals(price, result.getPrice());
        assertEquals(area, result.getArea());
        assertEquals(description, result.getDescription());
        assertEquals(type, result.getType());
        verify(repository, times(1)).createPost(location, price, area, description, userId, agencyId, type);
    }

    @Test
    void testCreatePost_InvalidPrice() {
        int invalidPrice = -1;
        assertThrows(InvalidPrice.class, () ->
                postService.createPost(location, invalidPrice, area, description, userId, agencyId, type)
        );
        verifyNoInteractions(repository);
    }

    @Test
    void testCreatePost_InvalidArea() {
        int invalidArea = -1;
        assertThrows(InvalidArea.class, () ->
                postService.createPost(location, price, invalidArea, description, userId, agencyId, type)
        );
        verifyNoInteractions(repository);
    }

    @Test
    void testCreatePost_InvalidParametersForAgency() {
        when(repository.createPost(location, price, area, description, userId, agencyId, type))
                .thenThrow(new InvalidDataAccessResourceUsageException("Invalid parameters"));

        assertThrows(InvalidParametersForAgency.class, () ->
                postService.createPost(location, price, area, description, userId, agencyId, type)
        );

        verify(repository, times(1)).createPost(location, price, area, description, userId, agencyId, type);
    }

    @Test
    void testGetPost_ValidId() {
        PostDAO mockPostDAO = new PostDAO(1, location, price, area, description, userId, agencyId, type, new Timestamp(System.currentTimeMillis()));
        when(repository.getPost(1)).thenReturn(mockPostDAO);

        Post result = postService.getPost(1);

        assertNotNull(result);
        assertEquals(1, result.getPostId());
        assertEquals(location, result.getLocation());
        verify(repository, times(1)).getPost(1);
    }

    @Test
    void testGetPost_InvalidId() {
        when(repository.getPost(999)).thenThrow(new InvalidDataAccessResourceUsageException("Invalid post ID"));

        assertThrows(InvalidPostId.class, () ->
                postService.getPost(999)
        );

        verify(repository, times(1)).getPost(999);
    }

    @Test
    void testFilterBy() {
        PostDAO mockPostDAO = new PostDAO(1, location, price, area, description, userId, agencyId, type, new Timestamp(System.currentTimeMillis()));
        when(repository.filterBy(location, price, type)).thenReturn(Collections.singletonList(mockPostDAO));

        List<PostDAO> result = postService.filterBy(location, price, type);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockPostDAO, result.get(0));
        verify(repository, times(1)).filterBy(location, price, type);
    }

    @Test
    void testGetPostsForUser() {
        PostDAO mockPostDAO = new PostDAO(1, location, price, area, description, userId, agencyId, type, new Timestamp(System.currentTimeMillis()));
        when(repository.getPostsForUser(userId)).thenReturn(Collections.singletonList(mockPostDAO));

        List<Post> result = postService.getPostsForUser(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(location, result.get(0).getLocation());
        verify(repository, times(1)).getPostsForUser(userId);
    }

    @Test
    void testGetPostsForAgency() {
        PostDAO mockPostDAO = new PostDAO(1, location, price, area, description, userId, agencyId, type, new Timestamp(System.currentTimeMillis()));
        when(repository.getPostsForAgency(agencyId)).thenReturn(Collections.singletonList(mockPostDAO));

        List<Post> result = postService.getPostsForAgency(agencyId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(location, result.get(0).getLocation());
        verify(repository, times(1)).getPostsForAgency(agencyId);
    }

    @Test
    void testDeletePost_ValidId() {
        doNothing().when(repository).deletePost(1);

        postService.deletePost(1);

        verify(repository, times(1)).deletePost(1);
    }

    @Test
    void testDeletePost_InvalidId() {
        doThrow(new InvalidDataAccessResourceUsageException("Invalid post ID"))
                .when(repository).deletePost(999);

        assertThrows(InvalidPostId.class, () ->
                postService.deletePost(999)
        );

        verify(repository, times(1)).deletePost(999);
    }
}

