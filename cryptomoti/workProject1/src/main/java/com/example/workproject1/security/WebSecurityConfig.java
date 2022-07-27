package com.example.workproject1.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
               // .addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/user/login").permitAll()
                .antMatchers(HttpMethod.POST, "/agency/login").permitAll()
                .antMatchers(HttpMethod.POST, "/user/register").permitAll()
                .antMatchers(HttpMethod.POST, "/agency/register").permitAll()
                .antMatchers(HttpMethod.POST, "/post/add").permitAll()
                .antMatchers(HttpMethod.GET, "/get/user/{id}").permitAll()
                .antMatchers(HttpMethod.DELETE, "/delete/user/{id}").permitAll()
                .antMatchers(HttpMethod.POST, "/subscribe").permitAll()
                .antMatchers(HttpMethod.DELETE, "/delete/post/{id}").permitAll()
                .antMatchers(HttpMethod.DELETE, "/delete/subscription/{id}").permitAll()
                .antMatchers(HttpMethod.GET, "/get/subscription/{id}").permitAll()
                .antMatchers(HttpMethod.GET, "/list/agency/subscriptions/{agency_id}").permitAll()
                .antMatchers(HttpMethod.GET, "/filter/posts").permitAll()
                .antMatchers(HttpMethod.GET, "/list/agency").permitAll()
                .antMatchers(HttpMethod.GET, "/get/agency/{id}").permitAll()
                .antMatchers(HttpMethod.DELETE, "/delete/agency/{id}").permitAll()
                .antMatchers(HttpMethod.GET, "/list/posts").permitAll()
                .antMatchers(HttpMethod.GET, "/get/post/{id}").permitAll()
                .antMatchers(HttpMethod.DELETE, "/delete/post/{id}").permitAll()
                .antMatchers(HttpMethod.GET, "/list/users").permitAll()
                .antMatchers(HttpMethod.GET, "/list/user/subscriptions/{user_id}").permitAll()
                .antMatchers(HttpMethod.GET, "/get/user/posts/{id}").permitAll()
                .antMatchers(HttpMethod.GET, "/get/agency/posts/{id}").permitAll()
                .antMatchers(HttpMethod.GET, "/get/post/{id}").permitAll()
                .antMatchers(HttpMethod.GET, "/get/user/expiration_date").permitAll()
                .antMatchers(HttpMethod.GET, "/get/agency/expiration_date").permitAll()
                .antMatchers(HttpMethod.GET, "/get/user/number/posts/{user_id}").permitAll()
                .antMatchers(HttpMethod.GET, "/get/agency/number/posts/{agency_id}").permitAll()
                .anyRequest().authenticated();

       http.cors();
    }
}