package com.example.workproject1.web.beans;

import com.example.workproject1.repositories.*;
import com.example.workproject1.repositories.mysql.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RepositoryBeans {

    @Bean
    public UserRepository userRepository(
            TransactionTemplate txTemplate, JdbcTemplate jdbcTemplate) {
        return new MySQLUserRepository(txTemplate, jdbcTemplate);
    }
    @Bean
    public PostRepository postRepository(
            TransactionTemplate txTemplate, JdbcTemplate jdbcTemplate) {
        return new MySQLPostRepository(txTemplate, jdbcTemplate);
    }
    @Bean
    public AgencyRepository agencyRepository(
            TransactionTemplate txTemplate, JdbcTemplate jdbcTemplate) {
        return new MySQLAgencyRepository(txTemplate, jdbcTemplate);
    }
    @Bean
    public SubscriptionRepository subscriptionRepository(
            TransactionTemplate txTemplate, JdbcTemplate jdbcTemplate) {
        return new MySQLSubscriptionRepository(txTemplate, jdbcTemplate);
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}