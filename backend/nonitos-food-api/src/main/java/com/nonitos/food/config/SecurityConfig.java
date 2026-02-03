package com.nonitos.food.config;

import com.nonitos.food.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration for the application.
 * 
 * <p>This configuration class sets up Spring Security with JWT-based authentication,
 * defining which endpoints are public, which require authentication, and how
 * authentication is performed.</p>
 * 
 * <h2>Authentication Strategy:</h2>
 * <p>We use JWT token-based authentication with a stateless session policy:</p>
 * <ul>
 *   <li><b>Stateless sessions:</b> No server-side session storage (JSESSIONID cookies).
 *       This enables horizontal scaling and works better with mobile/SPA clients.</li>
 *   <li><b>JWT filter:</b> JwtAuthenticationFilter runs before every request to
 *       extract and validate JWT tokens from Authorization header.</li>
 *   <li><b>Bearer tokens:</b> Standard format: "Authorization: Bearer {token}"</li>
 * </ul>
 * 
 * <h2>Why CSRF is disabled:</h2>
 * <p>CSRF protection is disabled because:</p>
 * <ul>
 *   <li>We use JWT tokens (not cookies) for authentication</li>
 *   <li>JWT tokens in Authorization header are not automatically sent by browser</li>
 *   <li>Stateless API doesn't maintain session state</li>
 *   <li>CSRF attacks target cookie-based authentication (not applicable here)</li>
 * </ul>
 * <p>If we add cookie-based authentication in the future, CSRF must be re-enabled.</p>
 * 
 * <h2>Endpoint Security Rules:</h2>
 * 
 * <h3>Public endpoints (no authentication required):</h3>
 * <ul>
 *   <li><b>/api/auth/**</b> - Registration, login, token refresh, email verification.
 *       Must be public so users can create accounts and login.</li>
 *   <li><b>/actuator/health</b> - Health check for monitoring/load balancers.
 *       No sensitive data exposed.</li>
 *   <li><b>/swagger-ui/**, /v3/api-docs/**</b> - API documentation.
 *       Public for developer convenience. In production, consider restricting.</li>
 *   <li><b>GET /api/dishes/**</b> - Browse dish catalog.
 *       Public to allow potential customers to see offerings before registering.</li>
 *   <li><b>GET /api/menus/**</b> - View weekly menus.
 *       Public for same reason as dishes - marketing/discovery.</li>
 * </ul>
 * 
 * <h3>Protected endpoints (authentication required):</h3>
 * <ul>
 *   <li>POST/PUT/DELETE /api/dishes/** - Only authenticated admins can modify dishes</li>
 *   <li>POST/PUT/DELETE /api/menus/** - Only authenticated admins can modify menus</li>
 *   <li>/api/orders/** - Clients need authentication to place/view orders</li>
 *   <li>/api/users/** - Users need authentication to manage their profile</li>
 *   <li>All other endpoints - Default to requiring authentication (secure by default)</li>
 * </ul>
 * 
 * <h3>Role-based authorization:</h3>
 * <p>Endpoint-level security (public vs authenticated) is configured here.
 * Fine-grained role-based authorization (ADMIN vs CLIENT) is handled at the
 * method level using @PreAuthorize annotations. This separation keeps this
 * configuration clean and allows flexible per-method authorization rules.</p>
 * 
 * <h2>CORS Configuration:</h2>
 * <p>CORS is configured separately in CorsConfig. We enable CORS to allow
 * frontend (React app) running on different origin (localhost:3000) to call
 * our API (localhost:8080). In production, CORS should be restricted to
 * specific allowed origins.</p>
 * 
 * <h2>Filter Chain Order:</h2>
 * <p>JwtAuthenticationFilter is added BEFORE UsernamePasswordAuthenticationFilter.
 * This ensures JWT validation happens early in the filter chain, before Spring
 * Security's default authentication mechanisms. If JWT is valid, user is
 * authenticated and subsequent filters are skipped.</p>
 *
 * @author Nonito's Food Team
 * @since 1.0
 * @see JwtAuthenticationFilter for JWT token validation logic
 * @see CorsConfig for CORS configuration
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Configures the security filter chain.
     * Sets up endpoint authorization, JWT filter, and session management.
     *
     * @param http the HttpSecurity to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configure(http))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/actuator/health").permitAll()
                        // Swagger UI endpoints
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/v3/api-docs.yaml").permitAll()
                        .requestMatchers("/swagger-resources/**").permitAll()
                        .requestMatchers("/webjars/**").permitAll()
                        // Public API endpoints
                        .requestMatchers(HttpMethod.GET, "/api/dishes/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/menus/**").permitAll()
                        // All other endpoints require authentication
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
