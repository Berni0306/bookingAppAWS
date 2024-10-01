package com.api.bookingapp.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);
    @Value("${web.allowed-origin}")
    private String ALLOWED_ORIGIN;
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        logger.info("Allowed Origin: {}", ALLOWED_ORIGIN); // Verificar si se inyecta correctamente
        registry.addMapping("/**") // Permitir CORS para todos los endpoints
                .allowedOrigins(ALLOWED_ORIGIN) // Permitir solicitudes desde tu frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Métodos permitidos
                .allowedHeaders("*") // Cabeceras permitidas
                .allowCredentials(true); // Permitir credenciales, si es necesario
    }
}