package com.hackathon.KCThack;

import com.hackathon.KCThack.config.JwtCore;
import com.hackathon.KCThack.enums.UserRole;
import com.hackathon.KCThack.service.UserDetailsImpl;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtCoreTest {

    private JwtCore jwtCore;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        jwtCore = new JwtCore();
        // Устанавливаем тестовые значения через рефлексию, т.к. поля приватные
        ReflectionTestUtils.setField(jwtCore, "secretJwtKey", 
            "c2VjcmV0SldUa2V5MTIzNDU2Nzg5MGFiY2RlZmdoaWprbG1ub3BxcnN0dXZ3eHl6QUJDREVGRw==");
        ReflectionTestUtils.setField(jwtCore, "lifetime", 3600000); // 1 час
        jwtCore.validateKey(); // инициализация ключа
    }

    @Test
    void generateToken_ShouldReturnValidJwt() {
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn("user-123");
        when(userDetails.getRole()).thenReturn(UserRole.USER);
        when(userDetails.getTokenVersion()).thenReturn(1);

        String token = jwtCore.generateToken(authentication);

        assertNotNull(token);
        String userId = jwtCore.getIdFromJwt(token);
        assertEquals("user-123", userId);
    }

    @Test
    void getIdFromJwt_WithInvalidToken_ThrowsException() {
        assertThrows(JwtException.class, () -> jwtCore.getIdFromJwt("invalid.token.value"));
    }

    @Test
    void validateKey_WithShortKey_ThrowsException() {
        JwtCore invalidJwtCore = new JwtCore();
        ReflectionTestUtils.setField(invalidJwtCore, "secretJwtKey", "short");
        assertThrows(IllegalStateException.class, invalidJwtCore::validateKey);
    }
}