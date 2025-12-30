package com.chatop.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration pour servir les fichiers statiques (images uploadées).
 * Permet d'accéder aux images via http://localhost:3001/uploads/nom_image.jpg
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Mappe les URLs /uploads/** vers le dossier uploads/ sur le disque
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}
