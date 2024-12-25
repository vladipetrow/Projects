package com.example.workproject1.web.beans;

import com.example.workproject1.coreServices.*;
import com.example.workproject1.repositories.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CoreBeans {
    @Bean
    public UserService userService(UserRepository repository) {
        return new UserService(repository);
    }
    @Bean
    public PostService postService(PostRepository repository) {
        return new PostService(repository);
    }
    @Bean
    public AgencyService agencyService(AgencyRepository repository) {
        return new AgencyService(repository);
    }

}
