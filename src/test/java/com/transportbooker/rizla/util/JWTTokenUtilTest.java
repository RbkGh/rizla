package com.transportbooker.rizla.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class JWTTokenUtilTest {

    private JWTTokenUtil jwtTokenUtil;

    @BeforeEach
    void setUp() {
        jwtTokenUtil = new JWTTokenUtil("testSecretMUSTGO89*23~testSecretMUSTGO89testSecretMUSTGO89testSecretMUSTGO89testSecretMUSTGO89testSecretMUSTGO89testSecretMUSTGO89",Long.valueOf("3600"));
        ReflectionTestUtils.setField(jwtTokenUtil, "secret", "testSecretMUSTGO89*23~testSecretMUSTGO89testSecretMUSTGO89testSecretMUSTGO89testSecretMUSTGO89testSecretMUSTGO89testSecretMUSTGO89");
        ReflectionTestUtils.setField(jwtTokenUtil, "expiration", Long.valueOf("3600"));
    }

    @Test
    void generateTokenAndValidateToken() {
        UserDetails userDetails = new User("testuser", "password", Collections.emptyList());

        String token = jwtTokenUtil.generateToken(userDetails);
        boolean isValid = jwtTokenUtil.validateToken(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    void getUsernameFromToken() {
        UserDetails userDetails = new User("testuser", "password", Collections.emptyList());
        String token = jwtTokenUtil.generateToken(userDetails);

        String username = jwtTokenUtil.getUsernameFromToken(token);

        assertEquals("testuser", username);
    }
}