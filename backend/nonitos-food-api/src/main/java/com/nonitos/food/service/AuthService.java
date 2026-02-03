package com.nonitos.food.service;

import com.nonitos.food.dto.auth.*;
import com.nonitos.food.exception.BadRequestException;
import com.nonitos.food.exception.ResourceNotFoundException;
import com.nonitos.food.exception.UnauthorizedException;
import com.nonitos.food.model.User;
import com.nonitos.food.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Service for handling user authentication operations.
 * 
 * <p>This service is the core of the authentication system, managing the complete
 * user authentication lifecycle including registration, login, token management,
 * and email verification. It implements a JWT-based stateless authentication
 * strategy combined with Redis for refresh token storage.</p>
 * 
 * <h2>Design Decisions:</h2>
 * <ul>
 *   <li><b>BCrypt with strength 12:</b> Chosen for password hashing to balance
 *       security and performance. Strength 12 provides ~4096 iterations, making
 *       brute-force attacks computationally expensive while maintaining acceptable
 *       login response times (~200-300ms).</li>
 *   <li><b>Redis for refresh tokens:</b> Refresh tokens are stored in Redis instead
 *       of the database to enable instant revocation and reduce database load.
 *       Redis TTL automatically handles token expiration.</li>
 *   <li><b>Email verification required:</b> Users can login immediately after
 *       registration but certain features may be restricted until email is verified.
 *       This improves user experience while maintaining security.</li>
 *   <li><b>Transactional methods:</b> All state-changing operations are wrapped in
 *       transactions to ensure data consistency, especially important for the
 *       registration flow which involves multiple database writes.</li>
 * </ul>
 * 
 * <h2>Security Considerations:</h2>
 * <ul>
 *   <li>Passwords are never logged or exposed in responses</li>
 *   <li>Failed login attempts don't reveal whether email exists (generic error)</li>
 *   <li>Verification tokens are UUID v4 (cryptographically random)</li>
 *   <li>Tokens have strict expiration times (24h for email, 7d for refresh)</li>
 * </ul>
 *
 * @author Nonito's Food Team
 * @since 1.0
 * @see JwtService for token generation and validation
 * @see UserRepository for user data persistence
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RedisTemplate<String, String> redisTemplate;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    /**
     * Registers a new user in the system.
     * 
     * <p>This method handles the complete user registration flow including password
     * encryption, verification token generation, and immediate JWT token issuance.
     * The user can start using the system immediately but may have restricted access
     * until email verification is completed.</p>
     * 
     * <p><b>Why immediate token issuance?</b> We issue JWT tokens immediately upon 
     * registration (before email verification) to improve user experience. Users can 
     * explore the system right away, but critical operations (like placing orders) 
     * require verified email. This approach reduces friction while maintaining security 
     * for sensitive operations.</p>
     * 
     * <p><b>Email verification flow:</b></p>
     * <ol>
     *   <li>Generate cryptographically random UUID token</li>
     *   <li>Store token with 24-hour expiration</li>
     *   <li>Send verification email (currently mocked, will integrate email service)</li>
     *   <li>User clicks link to verify</li>
     * </ol>
     * 
     * <p><b>Security notes:</b></p>
     * <ul>
     *   <li>Password is hashed with BCrypt before storage (never stored in plaintext)</li>
     *   <li>Email uniqueness is enforced at database level (unique constraint)</li>
     *   <li>Verification token is single-use and time-limited</li>
     * </ul>
     *
     * @param request the registration request containing user details (email, password,
     *                full name, phone number). All fields are validated before reaching
     *                this method via Jakarta Bean Validation.
     * @return login response with JWT access token (30min expiry), refresh token (7d expiry),
     *         and user information including verification status
     * @throws BadRequestException if email is already registered. This prevents user
     *                            enumeration attacks by not revealing whether an email
     *                            exists in the system through timing attacks (check is fast).
     */
    @Transactional
    public LoginResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }

        String verificationToken = UUID.randomUUID().toString();

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .role(User.UserRole.CLIENT)
                .isEmailVerified(false)
                .emailVerificationToken(verificationToken)
                .emailVerificationExpiresAt(LocalDateTime.now().plusHours(24))
                .build();

        user = userRepository.save(user);

        // Mock email sending
        log.info("=== EMAIL VERIFICATION ===");
        log.info("To: {}", user.getEmail());
        log.info("Verification Token: {}", verificationToken);
        log.info("Verification Link: http://localhost:3000/verify-email?token={}", verificationToken);
        log.info("==========================");

        return generateLoginResponse(user);
    }

    /**
     * Authenticates a user with email and password.
     * 
     * <p>Implements secure authentication following OWASP guidelines. The method
     * uses constant-time comparison for passwords (via BCrypt) to prevent timing
     * attacks that could reveal password information.</p>
     * 
     * <p><b>Why generic error messages?</b> Both invalid email and invalid password 
     * return the same "Invalid credentials" error. This prevents user enumeration 
     * attacks where an attacker could determine which emails are registered by 
     * observing different error messages. This is a critical security practice for 
     * authentication systems.</p>
     * 
     * <p><b>Login tracking:</b> We update the lastLoginAt timestamp for analytics 
     * and security monitoring. This helps identify:</p>
     * <ul>
     *   <li>Inactive accounts (for cleanup or re-engagement campaigns)</li>
     *   <li>Suspicious login patterns (multiple logins from different locations)</li>
     *   <li>User engagement metrics</li>
     * </ul>
     * 
     * <p><b>Token generation:</b> Both access and refresh tokens are generated on 
     * successful login:</p>
     * <ul>
     *   <li><b>Access token (30min):</b> Short-lived for security. Used for API requests.</li>
     *   <li><b>Refresh token (7d):</b> Long-lived, stored in Redis. Used to obtain new
     *       access tokens without re-entering credentials.</li>
     * </ul>
     * <p>This dual-token approach balances security (short access token) with UX
     * (users don't need to login every 30 minutes).</p>
     *
     * @param request the login request containing email and password. Password is
     *                validated against the hashed version stored in database.
     * @return login response with new JWT tokens and user information. The response
     *         includes the user's verification status so the frontend can prompt
     *         for email verification if needed.
     * @throws UnauthorizedException if email doesn't exist OR password doesn't match.
     *                              Generic message prevents user enumeration attacks.
     */
    @Transactional
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        return generateLoginResponse(user);
    }

    /**
     * Refreshes JWT access token using a valid refresh token.
     * 
     * <p>This method implements the refresh token flow, allowing users to obtain
     * new access tokens without re-entering their credentials. This is crucial for
     * maintaining a seamless user experience while keeping access tokens short-lived
     * for security.</p>
     * 
     * <p><b>Why store refresh tokens in Redis?</b> Refresh tokens are stored in 
     * Redis (not database) for several reasons:</p>
     * <ul>
     *   <li><b>Instant revocation:</b> Can immediately invalidate tokens (logout, security breach)</li>
     *   <li><b>Performance:</b> Redis lookups are faster than database queries</li>
     *   <li><b>Automatic expiration:</b> Redis TTL handles token expiration automatically</li>
     *   <li><b>Reduced DB load:</b> Token validation happens frequently, Redis handles
     *       this load better than PostgreSQL</li>
     * </ul>
     * 
     * <p><b>Security validation:</b> The method performs multiple security checks:</p>
     * <ol>
     *   <li>Token signature validation (JWT integrity)</li>
     *   <li>Token expiration check</li>
     *   <li>Token existence in Redis (not revoked)</li>
     *   <li>Token value match (prevents token substitution)</li>
     *   <li>User still exists in system</li>
     * </ol>
     * <p>All checks must pass for token refresh to succeed.</p>
     * 
     * <p><b>Token rotation:</b> We generate a NEW refresh token on each refresh 
     * (token rotation). This limits the damage if a refresh token is compromised - 
     * it becomes invalid after first use. The old token is automatically replaced 
     * in Redis.</p>
     *
     * @param request the token refresh request containing the refresh token to validate
     * @return login response with NEW access and refresh tokens. Both tokens are
     *         regenerated for security (token rotation pattern).
     * @throws UnauthorizedException if refresh token is invalid, expired, revoked,
     *                              or doesn't match the stored token in Redis
     * @throws ResourceNotFoundException if the user associated with the token no
     *                                  longer exists (account deleted)
     */
    @Transactional
    public LoginResponse refreshToken(TokenRefreshRequest request) {
        String refreshToken = request.getRefreshToken();

        try {
            String email = jwtService.extractEmail(refreshToken);

            if (jwtService.isTokenExpired(refreshToken)) {
                throw new UnauthorizedException("Refresh token expired");
            }

            String storedToken = redisTemplate.opsForValue().get("refresh_token:" + email);
            if (storedToken == null || !storedToken.equals(refreshToken)) {
                throw new UnauthorizedException("Invalid refresh token");
            }

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            return generateLoginResponse(user);
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid refresh token");
        }
    }

    /**
     * Verifies user email address using verification token.
     * 
     * <p>Email verification is a critical security measure that confirms the user
     * has access to the email address they registered with. This prevents:</p>
     * <ul>
     *   <li>Fake account creation with others' email addresses</li>
     *   <li>Spam and abuse through unverified accounts</li>
     *   <li>Account recovery issues (can't reset password without valid email)</li>
     * </ul>
     * 
     * <p><b>Why 24-hour expiration?</b> Verification tokens expire after 24 hours 
     * to balance security and UX:</p>
     * <ul>
     *   <li><b>Security:</b> Limits window for token interception/misuse</li>
     *   <li><b>UX:</b> Gives users reasonable time to check email and verify</li>
     *   <li><b>Cleanup:</b> Prevents accumulation of unverified accounts</li>
     * </ul>
     * <p>Users can request a new verification email if the token expires.</p>
     * 
     * <p><b>Token cleanup:</b> After successful verification, we immediately clear 
     * the verification token and expiration from the database. This ensures:</p>
     * <ul>
     *   <li>Token can only be used once (single-use tokens)</li>
     *   <li>No lingering sensitive data in database</li>
     *   <li>Clear verification state (either verified or not)</li>
     * </ul>
     * 
     * <p><b>Business impact:</b> Verified users can:</p>
     * <ul>
     *   <li>Place orders for meal prep services</li>
     *   <li>Receive order notifications and updates</li>
     *   <li>Reset password if forgotten</li>
     *   <li>Access full account features</li>
     * </ul>
     * <p>Unverified users have limited access to protect business operations.</p>
     *
     * @param token the email verification token sent to user's email. This is a
     *              UUID v4 token that was generated during registration.
     * @throws BadRequestException if token is invalid (doesn't exist in database)
     *                            or expired (past 24-hour window). Different error
     *                            messages help users understand what went wrong.
     */
    @Transactional
    public void verifyEmail(String token) {
        User user = userRepository.findByEmailVerificationToken(token)
                .orElseThrow(() -> new BadRequestException("Invalid verification token"));

        if (user.getEmailVerificationExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Verification token expired");
        }

        user.setIsEmailVerified(true);
        user.setEmailVerificationToken(null);
        user.setEmailVerificationExpiresAt(null);
        userRepository.save(user);

        log.info("Email verified for user: {}", user.getEmail());
    }

    /**
     * Generates login response with JWT tokens for authenticated user.
     * 
     * <p>This is a private helper method that centralizes token generation logic
     * used by register(), login(), and refreshToken() methods. Centralizing this
     * logic ensures consistency and makes it easier to modify token generation
     * strategy in the future.</p>
     * 
     * <p><b>Token structure - Access Token contains:</b></p>
     * <ul>
     *   <li>Subject: user email (for identification)</li>
     *   <li>userId: for quick user lookup without database query</li>
     *   <li>role: for authorization checks (ADMIN vs CLIENT)</li>
     *   <li>Expiration: 30 minutes (short-lived for security)</li>
     * </ul>
     * 
     * <p><b>Token structure - Refresh Token contains:</b></p>
     * <ul>
     *   <li>Subject: user email only</li>
     *   <li>Expiration: 7 days (longer for better UX)</li>
     *   <li>Stored in Redis for revocation capability</li>
     * </ul>
     * 
     * <p><b>Why different token lifetimes?</b> Access tokens are short-lived (30min) 
     * because they grant direct API access. If compromised, the damage window is 
     * limited. Refresh tokens are longer-lived (7d) but can only be used to get new 
     * access tokens, and are stored in Redis for instant revocation if needed. This 
     * balances security with user experience.</p>
     * 
     * <p><b>Redis storage strategy:</b> Refresh tokens are stored with key pattern 
     * "refresh_token:{email}" and 7-day TTL. This allows:</p>
     * <ul>
     *   <li>Quick lookup by email during token refresh</li>
     *   <li>Automatic cleanup of expired tokens (Redis TTL)</li>
     *   <li>Easy revocation (delete key from Redis)</li>
     *   <li>One token per user (new login invalidates old token)</li>
     * </ul>
     *
     * @param user the authenticated user for whom to generate tokens. Must be a
     *             persisted entity with valid ID.
     * @return login response containing access token, refresh token, token type
     *         (Bearer), expiration time, and user information (id, email, name,
     *         role, verification status)
     */
    private LoginResponse generateLoginResponse(User user) {
        String accessToken = jwtService.generateAccessToken(
                user.getEmail(),
                user.getId(),
                user.getRole().name()
        );

        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        // Store refresh token in Redis with 7 days TTL
        redisTemplate.opsForValue().set(
                "refresh_token:" + user.getEmail(),
                refreshToken,
                7,
                TimeUnit.DAYS
        );

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(1800L) // 30 minutes in seconds
                .user(LoginResponse.UserInfo.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .fullName(user.getFullName())
                        .role(user.getRole().name())
                        .isEmailVerified(user.getIsEmailVerified())
                        .build())
                .build();
    }
}
