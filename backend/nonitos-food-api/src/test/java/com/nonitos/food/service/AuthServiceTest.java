package com.nonitos.food.service;

import com.nonitos.food.dto.auth.*;
import com.nonitos.food.exception.BadRequestException;
import com.nonitos.food.exception.UnauthorizedException;
import com.nonitos.food.model.User;
import com.nonitos.food.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ClientProfileService clientProfileService;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_Success() {
        RegisterRequest request = RegisterRequest.builder()
                .email("test@example.com")
                .password("Password123")
                .fullName("Test User")
                .phoneNumber("1234567890")
                .build();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });
        when(jwtService.generateAccessToken(anyString(), anyLong(), anyString())).thenReturn("access-token");
        when(jwtService.generateRefreshToken(anyString())).thenReturn("refresh-token");
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        LoginResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals(1800L, response.getExpiresIn());
        assertNotNull(response.getUser());
        assertEquals("test@example.com", response.getUser().getEmail());
        
        // Verify profile creation was called
        verify(clientProfileService).createProfile(1L);
        assertEquals("Test User", response.getUser().getFullName());
        assertEquals("CLIENT", response.getUser().getRole());
        assertFalse(response.getUser().getIsEmailVerified());

        verify(userRepository).existsByEmail(request.getEmail());
        verify(userRepository).save(any(User.class));
        verify(valueOperations).set(eq("refresh_token:test@example.com"), eq("refresh-token"), eq(7L), eq(TimeUnit.DAYS));
    }

    @Test
    void register_EmailAlreadyExists() {
        RegisterRequest request = RegisterRequest.builder()
                .email("existing@example.com")
                .password("Password123")
                .fullName("Test User")
                .build();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> authService.register(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_Success() {
        LoginRequest request = LoginRequest.builder()
                .email("test@example.com")
                .password("Password123")
                .build();

        // Create a real BCrypt encoder to hash the password
        org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder encoder = 
            new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder(12);
        String hashedPassword = encoder.encode("Password123");

        User user = User.builder()
                .email("test@example.com")
                .password(hashedPassword)
                .fullName("Test User")
                .role(User.UserRole.CLIENT)
                .isEmailVerified(true)
                .build();
        user.setId(1L);

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateAccessToken(anyString(), anyLong(), anyString())).thenReturn("access-token");
        when(jwtService.generateRefreshToken(anyString())).thenReturn("refresh-token");
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        LoginResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("access-token", response.getAccessToken());
        verify(userRepository).save(argThat(u -> u.getLastLoginAt() != null));
    }

    @Test
    void login_InvalidEmail() {
        LoginRequest request = LoginRequest.builder()
                .email("nonexistent@example.com")
                .password("Password123")
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        assertThrows(UnauthorizedException.class, () -> authService.login(request));
    }

    @Test
    void verifyEmail_Success() {
        String token = "verification-token";
        User user = User.builder()
                .email("test@example.com")
                .emailVerificationToken(token)
                .emailVerificationExpiresAt(LocalDateTime.now().plusHours(1))
                .isEmailVerified(false)
                .build();
        user.setId(1L);

        when(userRepository.findByEmailVerificationToken(token)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        authService.verifyEmail(token);

        verify(userRepository).save(argThat(u ->
                u.getIsEmailVerified() &&
                u.getEmailVerificationToken() == null &&
                u.getEmailVerificationExpiresAt() == null
        ));
    }

    @Test
    void verifyEmail_ExpiredToken() {
        String token = "expired-token";
        User user = User.builder()
                .email("test@example.com")
                .emailVerificationToken(token)
                .emailVerificationExpiresAt(LocalDateTime.now().minusHours(1))
                .isEmailVerified(false)
                .build();
        user.setId(1L);

        when(userRepository.findByEmailVerificationToken(token)).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> authService.verifyEmail(token));
        verify(userRepository, never()).save(any(User.class));
    }
}
