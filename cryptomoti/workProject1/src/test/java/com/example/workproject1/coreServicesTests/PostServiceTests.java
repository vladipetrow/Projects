package com.example.workproject1.coreServicesTests;

import com.example.workproject1.coreServices.PostService;
import com.example.workproject1.coreServices.ServiceExeptions.InvalidPostIdException;
import com.example.workproject1.coreServices.models.ApartmentType;
import com.example.workproject1.coreServices.models.TransactionType;
import com.example.workproject1.coreServices.models.Post;
import com.example.workproject1.web.api.models.PostInput;
import com.example.workproject1.repositories.PostRepository;
import com.example.workproject1.repositories.models.PostDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

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
    private final ApartmentType apartmentType = ApartmentType.TWO_BEDROOM;
    private final TransactionType transactionType = TransactionType.BUY;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePost_ValidInputs() {
        // Create PostInput
        PostInput postInput = new PostInput();
        postInput.setLocation(location);
        postInput.setPrice(price);
        postInput.setArea(area);
        postInput.setDescription(description);
        postInput.setApartmentType(apartmentType);
        postInput.setTransactionType(transactionType);
        
        // Create PostDAO using Builder
        PostDAO mockPostDAO = new PostDAO.Builder()
                .postId(1)
                .location(location)
                .price(price)
                .area(area)
                .description(description)
                .userId(userId)
                .agencyId(agencyId)
                .typeOfApartId(1) // Mock apartment type ID
                .transactionType(transactionType)
                .postDate(new Timestamp(System.currentTimeMillis()))
                .imageUrls(Collections.emptyList())
                .promoted(false)
                .viewCount(0)
                .build();

        when(repository.createPost(anyString(), anyInt(), anyInt(), anyString(), anyInt(), anyInt(), anyInt(), any(TransactionType.class), anyList()))
                .thenReturn(mockPostDAO);

        List<MultipartFile> mockImages = new ArrayList<>();
        Post result = postService.createPost(postInput, userId, agencyId, mockImages);

        assertNotNull(result);
        assertEquals(location, result.getLocation());
        assertEquals(price, result.getPrice());
        assertEquals(area, result.getArea());
        assertEquals(description, result.getDescription());
        assertEquals(apartmentType, result.getApartmentType());
    }

    @Test
    void testGetPost_ValidId() {
        PostDAO mockPostDAO = new PostDAO.Builder()
                .postId(1)
                .location(location)
                .price(price)
                .area(area)
                .description(description)
                .userId(userId)
                .agencyId(agencyId)
                .typeOfApartId(1)
                .transactionType(transactionType)
                .postDate(new Timestamp(System.currentTimeMillis()))
                .imageUrls(Collections.emptyList())
                .promoted(false)
                .viewCount(0)
                .build();
        
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

        assertThrows(InvalidPostIdException.class, () ->
                postService.getPost(999)
        );

        verify(repository, times(1)).getPost(999);
    }

    @Test
    void testFilterBy() {
        PostDAO mockPostDAO = new PostDAO.Builder()
                .postId(1)
                .location(location)
                .price(price)
                .area(area)
                .description(description)
                .userId(userId)
                .agencyId(agencyId)
                .typeOfApartId(1)
                .transactionType(transactionType)
                .postDate(new Timestamp(System.currentTimeMillis()))
                .imageUrls(Collections.emptyList())
                .promoted(false)
                .viewCount(0)
                .build();
        
        when(repository.filterBy(location, price, apartmentType, transactionType)).thenReturn(Collections.singletonList(mockPostDAO));

        List<PostDAO> result = postService.filterBy(location, price, apartmentType, transactionType);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(location, result.get(0).getLocation());
        verify(repository, times(1)).filterBy(location, price, apartmentType, transactionType);
    }

    @Test
    void testGetPostsForUser() {
        PostDAO mockPostDAO = new PostDAO.Builder()
                .postId(1)
                .location(location)
                .price(price)
                .area(area)
                .description(description)
                .userId(userId)
                .agencyId(agencyId)
                .typeOfApartId(1)
                .transactionType(transactionType)
                .postDate(new Timestamp(System.currentTimeMillis()))
                .imageUrls(Collections.emptyList())
                .promoted(false)
                .viewCount(0)
                .build();
        
        when(repository.getPostsForUser(userId)).thenReturn(Collections.singletonList(mockPostDAO));

        List<Post> result = postService.getUserPosts(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(location, result.get(0).getLocation());
        verify(repository, times(1)).getPostsForUser(userId);
    }

    @Test
    void testGetPostsForAgency() {
        PostDAO mockPostDAO = new PostDAO.Builder()
                .postId(1)
                .location(location)
                .price(price)
                .area(area)
                .description(description)
                .userId(userId)
                .agencyId(agencyId)
                .typeOfApartId(1)
                .transactionType(transactionType)
                .postDate(new Timestamp(System.currentTimeMillis()))
                .imageUrls(Collections.emptyList())
                .promoted(false)
                .viewCount(0)
                .build();
        
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

        assertThrows(InvalidPostIdException.class, () ->
                postService.deletePost(999)
        );

        verify(repository, times(1)).deletePost(999);
    }
}

