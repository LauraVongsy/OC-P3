package com.chatop.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Appliquer CORS à tous les endpoints
                        .allowedOrigins("http://localhost:4200") // Autoriser les requêtes provenant de localhost:4200
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Méthodes HTTP permises
                        .allowedHeaders("*") // Autoriser tous les en-têtes
                        .allowCredentials(true); // Autoriser les cookies et l'authentification
            }

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/uploads/**")
                        .addResourceLocations("file:uploads/");
            }


        };
    }
}
