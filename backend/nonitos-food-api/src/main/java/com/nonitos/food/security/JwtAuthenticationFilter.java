package com.nonitos.food.security;

import com.nonitos.food.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * JWT authentication filter for validating and processing JWT tokens.
 * 
 * <p>This filter intercepts every HTTP request, extracts the JWT token from the
 * Authorization header, validates it, and sets up Spring Security authentication
 * context if the token is valid.</p>
 * 
 * <h2>Filter execution flow:</h2>
 * <ol>
 *   <li>Extract Authorization header from request</li>
 *   <li>Check if header exists and starts with "Bearer "</li>
 *   <li>Extract JWT token (remove "Bearer " prefix)</li>
 *   <li>Validate token (signature, expiration, format)</li>
 *   <li>Extract user information (email, role, userId) from token claims</li>
 *   <li>Create Spring Security authentication object</li>
 *   <li>Set authentication in SecurityContext (user is now authenticated)</li>
 *   <li>Continue filter chain</li>
 * </ol>
 * 
 * <h2>Why extend OncePerRequestFilter?</h2>
 * <p>OncePerRequestFilter guarantees the filter executes exactly once per request,
 * even if the request is forwarded or included. This prevents duplicate token
 * validation and authentication setup.</p>
 * 
 * <h2>Error handling strategy:</h2>
 * <p>If token validation fails (expired, invalid signature, malformed), the filter
 * silently continues without setting authentication. This allows the request to
 * proceed to the endpoint, where Spring Security will reject it with 401 if
 * authentication is required. This approach:
 * <ul>
 *   <li>Allows public endpoints to work even with invalid tokens</li>
 *   <li>Provides consistent error responses from Spring Security</li>
 *   <li>Prevents information leakage about token validation failures</li>
 * </ul>
 * </p>
 * 
 * <h2>SecurityContext and thread safety:</h2>
 * <p>SecurityContextHolder stores authentication per thread. Each request runs in
 * its own thread, so authentication is isolated between concurrent requests. The
 * context is automatically cleared after the request completes.</p>
 * 
 * <h2>Role prefix convention:</h2>
 * <p>Spring Security requires role names to be prefixed with "ROLE_". We store
 * roles in JWT as "ADMIN" or "CLIENT", but add "ROLE_" prefix when creating
 * authorities. This allows @PreAuthorize("hasRole('ADMIN')") to work correctly.</p>
 *
 * @author Nonito's Food Team
 * @since 1.0
 * @see JwtService for token validation and claim extraction
 * @see com.nonitos.food.config.SecurityConfig for security configuration
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /** Service for JWT token operations */
    private final JwtService jwtService;

    /**
     * Filters each request to validate JWT token and set up authentication.
     * 
     * <p>Extracts JWT from Authorization header, validates it, and populates
     * Spring Security context with user authentication if token is valid.</p>
     * 
     * <h3>Request attribute:</h3>
     * <p>Sets "userId" request attribute for easy access in controllers without
     * parsing JWT again. Controllers can retrieve it with:
     * {@code (Long) request.getAttribute("userId")}</p>
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain to continue
     * @throws ServletException if servlet error occurs
     * @throws IOException if I/O error occurs
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // Extract Authorization header
        final String authHeader = request.getHeader("Authorization");

        // Skip if no Authorization header or not Bearer token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Extract JWT token (remove "Bearer " prefix)
            final String jwt = authHeader.substring(7);
            
            // Extract email from token
            final String email = jwtService.extractEmail(jwt);

            // Validate token and set authentication if not already authenticated
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (!jwtService.isTokenExpired(jwt)) {
                    // Extract user information from token
                    String role = jwtService.extractRole(jwt);
                    Long userId = jwtService.extractUserId(jwt);

                    // Create authentication token with role authority
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            email,
                            null,  // No credentials needed (already authenticated via JWT)
                            List.of(new SimpleGrantedAuthority("ROLE_" + role))
                    );

                    // Add request details (IP address, session ID, etc.)
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // Set authentication in security context
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    // Store userId in request attribute for easy access in controllers
                    request.setAttribute("userId", userId);
                }
            }
        } catch (Exception e) {
            // Invalid token - continue without authentication
            // Spring Security will reject request if endpoint requires authentication
        }

        // Continue filter chain
        filterChain.doFilter(request, response);
    }
}
