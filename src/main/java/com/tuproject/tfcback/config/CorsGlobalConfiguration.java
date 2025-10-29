package com.tuproject.tfcback.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsGlobalConfiguration {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Ajusta los orígenes según tu entorno (añade producción cuando toque)
        config.setAllowedOrigins(List.of(
            "http://localhost:5173",   // Vite (React)
            "http://localhost:3000"    // opcional (otros dev servers)
        ));
        config.setAllowCredentials(true);
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        // Si necesitas exponer headers (p. ej. Authorization, Location)
        config.setExposedHeaders(List.of("Authorization","Location"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}