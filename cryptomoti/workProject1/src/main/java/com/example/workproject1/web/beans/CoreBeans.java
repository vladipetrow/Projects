package com.example.workproject1.web.beans;

import com.example.workproject1.coreServices.*;
import com.example.workproject1.repositories.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;


@Configuration
public class CoreBeans {
    @Bean
    public UserService userService(UserRepository repository, EmailCacheService emailCacheService, MailgunService mailgunService, ValidationService validationService, PasswordService passwordService) {
        return new UserService(repository, emailCacheService, mailgunService, validationService, passwordService);
    }
    @Bean
    public PostService postService(PostRepository repository, PostCacheService postCacheService, PostImageService postImageService, PostValidationService postValidationService) {
        return new PostService(repository, postCacheService, postImageService, postValidationService);
    }
    
    
    @Bean
    public AgencyService agencyService(AgencyRepository repository, EmailCacheService emailCacheService, MailgunService mailgunService, ValidationService validationService, PasswordService passwordService) {
        return new AgencyService(repository, emailCacheService, mailgunService, validationService, passwordService);
    }

}
