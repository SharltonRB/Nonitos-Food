package com.nonitos.food.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * CORS (Cross-Origin Resource Sharing) configuration.
 * 
 * <p>Configures which frontend origins are allowed to make requests to this API.
 * This is necessary because browsers block cross-origin requests by default for
 * security reasons.</p>
 * 
 * <h2>Why CORS configuration is needed:</h2>
 * <p>When frontend (e.g., React on localhost:3000) tries to call backend API
 * (localhost:8080), browsers block the request unless the backend explicitly
 * allows it via CORS headers. Without this configuration, all API calls from
 * the frontend would fail with CORS errors.</p>
 * 
 * <h2>Security considerations:</h2>
 * <ul>
 *   <li><b>Allowed origins:</b> Only specific domains can access the API</li>
 *   <li><b>Credentials:</b> Allows cookies/auth headers (needed for JWT)</li>
 *   <li><b>Methods:</b> Only specified HTTP methods are allowed</li>
 *   <li><b>Max age:</b> Browsers cache CORS preflight for 1 hour</li>
 * </ul>
 * 
 * <h2>Current allowed origins:</h2>
 * <ul>
 *   <li>localhost:3000 - React development server (Create React App)</li>
 *   <li>localhost:5173 - Vite development server</li>
 *   <li>*.vercel.app - Production deployments on Vercel</li>
 * </ul>
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Configuration
public class CorsConfig {
    
    /**
     * Configures CORS settings for the application.
     * 
     * <p>Defines which origins, methods, and headers are allowed for cross-origin
     * requests. Applied to all endpoints (/**).</p>
     *
     * @return configured CORS configuration source
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Allowed frontend origins
        configuration.setAllowedOrigins(List.of(
                "http://localhost:3000",  // React dev server
                "http://localhost:5173",  // Vite dev server
                "https://*.vercel.app"    // Production deployments
        ));
        
        // Allowed HTTP methods
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        
        // Allow all headers (Authorization, Content-Type, etc.)
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        // Allow credentials (cookies, authorization headers)
        configuration.setAllowCredentials(true);
        
        // Cache preflight response for 1 hour
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
