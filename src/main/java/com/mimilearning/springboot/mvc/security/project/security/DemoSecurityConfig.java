package com.mimilearning.springboot.mvc.security.project.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class DemoSecurityConfig {

    // add support for JDBC ... no more hard-coded users
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource){
        // to tell Spring Security to use JDBC authentication with our data source
        return new JdbcUserDetailsManager(dataSource);
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Restrict access based on the HTTP request
        http.authorizeRequests(configurer ->
                configurer
                        .requestMatchers("/").hasRole("EMPLOYEE")
                        .requestMatchers("/leaders/**").hasRole("MANAGER")
                        .requestMatchers("/systems/**").hasRole("ADMIN")
                        .anyRequest()
                .authenticated() // any request to the app must be authenticated
        ).formLogin(form ->
                form.loginPage("/showMyLoginPage") // used to show the custom form, we create a controller for this request mapping
                        .loginProcessingUrl("/authenticatetheUser") // where we submit our form data, no controller request mapping required for this URL
                        //this URL is internal to Spring Security and is not directly exposed to the user or visible in the browser's address bar.
                       //it serves as an internal endpoint for handling form-based authentication.
                        .permitAll()
        ).logout(logout -> logout.permitAll() // enable log out for the entire application
        ).exceptionHandling(configurer -> configurer
                .accessDeniedPage("/access-denied"))
        ;


        return http.build(); // Return the SecurityFilterChain object
    }


}
//Here's how the aut hentication flow works:

//1-When the user accesses the home page (/ or any other page requiring authentication), Spring Security detects that the user is not authenticated and redirects them to the configured login page (/showMyLoginPage).

//2-The user fills out the login form and submits it. The form action is set to /authenticatetheUser, as specified by .loginProcessingUrl("/authenticatetheUser").

//3-Spring Security intercepts the form submission to /authenticatetheUser, extracts the form data (username and password), and attempts to authenticate the user based on this data.

//4-If authentication is successful, Spring Security will handle the authentication flow internally. Typically, this involves setting the authentication token and redirecting the user to the original requested page or a default success URL.
    /*
    @Bean
    public InMemoryUserDetailsManager userDetailsManager(){
        UserDetails jhon = User.builder()
                .username("jhon")
                .password("{noop}test123")
                .roles("EMPLOYEE")
                .build();
        UserDetails mary = User.builder()
                .username("mary")
                .password("{noop}test123")
                .roles("EMPLOYEE","MANAGER")
                .build();
        UserDetails susan = User.builder()
                .username("susan")
                .password("{noop}test123")
                .roles("EMPLOYEE","MANAGER","ADMIN")
                .build();
        return new InMemoryUserDetailsManager(jhon,mary,susan);
    }
    */