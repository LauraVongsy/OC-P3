package com.chatop.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**") // Applying cors to all endpoints
                        .allowedOrigins("http://localhost:4200") // Authorizing requests from localhost:4200
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // allowed http methods
                        .allowedHeaders("*") // allowed headers
                        .allowCredentials(true); // allow credentials
            }

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/uploads/**")
                        .addResourceLocations("file:uploads/");
            }


        };
    }
}