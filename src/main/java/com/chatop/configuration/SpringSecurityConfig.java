package com.chatop.configuration;

import com.chatop.filter.JwtFilter;
import com.chatop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * Sets up security rules for a web application to ensure only authorized users can access specific areas.
 */
@Configuration
@EnableWebSecurity
@CrossOrigin
public class SpringSecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    /**
     * Defines a UserDetailsService bean for user authentication
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserService();
    }

    /**
     * Configuration of the securityFilterChain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                //Disable CSRF protection
                .csrf(csrf -> csrf.disable())
                //Set the public and authentication routes
                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/register", "/api/auth/login").permitAll().anyRequest().authenticated())
                //Set session management to stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //Register the authentication provider
                .authenticationProvider(authenticationProvider())
                //Add JWT filter before processing the request
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Creates a DaoAuthenticationProvider to handle user authentication
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    /**
     * Defines a PasswordEncoder bean that uses Bcrypt hashing by default for password encoding
     *
     * @return
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Defines an AuthenticationManager bean to manage authentication processes
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
