package com.nonitos.food.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service for JWT token generation and validation.
 * 
 * <p>This service encapsulates all JWT operations, providing a clean interface
 * for token creation, parsing, and validation. It uses the JJWT library (io.jsonwebtoken)
 * which is the most mature and widely-used JWT library for Java.</p>
 * 
 * <h2>Why JWT for Authentication?</h2>
 * <p>JWT (JSON Web Tokens) was chosen over session-based authentication for several reasons:
 * <ul>
 *   <li><b>Stateless:</b> No server-side session storage needed, enabling horizontal scaling</li>
 *   <li><b>Mobile-friendly:</b> Works seamlessly with mobile apps and SPAs</li>
 *   <li><b>Microservices-ready:</b> Tokens can be validated by any service without
 *       centralized session store</li>
 *   <li><b>Performance:</b> No database lookup needed for every request (claims in token)</li>
 *   <li><b>Cross-domain:</b> Works across different domains/subdomains</li>
 * </ul>
 * 
 * <h2>Algorithm Choice: HS256</h2>
 * <p>We use HMAC-SHA256 (HS256) symmetric signing:
 * <ul>
 *   <li><b>Simplicity:</b> Single secret key, easier to manage than RSA key pairs</li>
 *   <li><b>Performance:</b> Faster than RSA (important for high-traffic APIs)</li>
 *   <li><b>Sufficient security:</b> For our use case (single backend), symmetric
 *       signing is secure and appropriate</li>
 *   <li><b>Key size:</b> 256-bit key provides strong security (2^256 possible keys)</li>
 * </ul>
 * Note: If we move to microservices, we might switch to RS256 (asymmetric) to allow
 * services to verify tokens without sharing the signing key.
 * 
 * <h2>Token Expiration Strategy:</h2>
 * <ul>
 *   <li><b>Access tokens: 30 minutes</b> - Short-lived to limit damage if compromised.
 *       Users won't notice because refresh tokens handle renewal automatically.</li>
 *   <li><b>Refresh tokens: 7 days</b> - Long enough for good UX (users stay logged in
 *       for a week) but short enough to limit security risk. Stored in Redis for
 *       instant revocation capability.</li>
 * </ul>
 * 
 * <h2>Security Considerations:</h2>
 * <ul>
 *   <li>Secret key must be at least 256 bits (32 bytes) for HS256</li>
 *   <li>Secret key should be randomly generated and stored securely (env variable)</li>
 *   <li>Tokens are signed but not encrypted - don't put sensitive data in claims</li>
 *   <li>Always validate token signature and expiration before trusting claims</li>
 *   <li>Use HTTPS in production to prevent token interception</li>
 * </ul>
 *
 * @author Nonito's Food Team
 * @since 1.0
 * @see <a href="https://jwt.io">JWT.io - JWT Debugger and Documentation</a>
 * @see <a href="https://github.com/jwtk/jjwt">JJWT Library Documentation</a>
 */
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;

    /**
     * Gets the signing key for JWT token operations.
     *
     * @return the signing key derived from secret
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Generates JWT access token with user claims.
     *
     * @param email the user email (token subject)
     * @param userId the user ID
     * @param role the user role
     * @return the generated access token
     */
    public String generateAccessToken(String email, Long userId, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", role);
        return createToken(claims, email, expiration);
    }

    /**
     * Generates JWT refresh token.
     *
     * @param email the user email (token subject)
     * @return the generated refresh token
     */
    public String generateRefreshToken(String email) {
        return createToken(new HashMap<>(), email, refreshExpiration);
    }

    /**
     * Creates a JWT token with specified claims and expiration.
     *
     * @param claims the token claims
     * @param subject the token subject (typically user email)
     * @param expiration the token expiration time in milliseconds
     * @return the generated JWT token
     */
    private String createToken(Map<String, Object> claims, String subject, Long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts email from JWT token.
     *
     * @param token the JWT token
     * @return the email (subject) from token
     */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts user ID from JWT token.
     *
     * @param token the JWT token
     * @return the user ID from token claims
     */
    public Long extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", Long.class));
    }

    /**
     * Extracts user role from JWT token.
     *
     * @param token the JWT token
     * @return the user role from token claims
     */
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    /**
     * Extracts expiration date from JWT token.
     *
     * @param token the JWT token
     * @return the expiration date
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from JWT token.
     *
     * @param token the JWT token
     * @param claimsResolver function to extract specific claim
     * @param <T> the type of the claim
     * @return the extracted claim value
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from JWT token.
     *
     * @param token the JWT token
     * @return all claims from the token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Checks if JWT token is expired.
     *
     * @param token the JWT token
     * @return true if token is expired, false otherwise
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Validates JWT token against user email.
     *
     * @param token the JWT token
     * @param email the user email to validate against
     * @return true if token is valid and not expired, false otherwise
     */
    public boolean validateToken(String token, String email) {
        final String tokenEmail = extractEmail(token);
        return (tokenEmail.equals(email) && !isTokenExpired(token));
    }
}
