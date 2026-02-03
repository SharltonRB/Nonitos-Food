package com.nonitos.food.service;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", "test-secret-key-that-is-long-enough-for-hs256-algorithm");
        ReflectionTestUtils.setField(jwtService, "expiration", 1800000L); // 30 minutes
        ReflectionTestUtils.setField(jwtService, "refreshExpiration", 604800000L); // 7 days
    }

    @Test
    void generateAccessToken_Success() {
        String token = jwtService.generateAccessToken("test@example.com", 1L, "CLIENT");

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void generateRefreshToken_Success() {
        String token = jwtService.generateRefreshToken("test@example.com");

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void extractEmail_Success() {
        String token = jwtService.generateAccessToken("test@example.com", 1L, "CLIENT");

        String email = jwtService.extractEmail(token);

        assertEquals("test@example.com", email);
    }

    @Test
    void extractUserId_Success() {
        String token = jwtService.generateAccessToken("test@example.com", 1L, "CLIENT");

        Long userId = jwtService.extractUserId(token);

        assertEquals(1L, userId);
    }

    @Test
    void extractRole_Success() {
        String token = jwtService.generateAccessToken("test@example.com", 1L, "ADMIN");

        String role = jwtService.extractRole(token);

        assertEquals("ADMIN", role);
    }

    @Test
    void isTokenExpired_NotExpired() {
        String token = jwtService.generateAccessToken("test@example.com", 1L, "CLIENT");

        boolean expired = jwtService.isTokenExpired(token);

        assertFalse(expired);
    }

    @Test
    void validateToken_ValidToken() {
        String email = "test@example.com";
        String token = jwtService.generateAccessToken(email, 1L, "CLIENT");

        boolean valid = jwtService.validateToken(token, email);

        assertTrue(valid);
    }

    @Test
    void validateToken_WrongEmail() {
        String token = jwtService.generateAccessToken("test@example.com", 1L, "CLIENT");

        boolean valid = jwtService.validateToken(token, "wrong@example.com");

        assertFalse(valid);
    }
}
