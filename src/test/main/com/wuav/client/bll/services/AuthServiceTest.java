package com.wuav.client.bll.services;

import com.wuav.client.be.user.AppUser;
import com.wuav.client.bll.utilities.engines.cryptoEngine.CryptoEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.naming.AuthenticationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private CryptoEngine cryptoEngine;

    @InjectMocks
    private AuthService authService;

    private static final String USER_EMAIL = "tech@hotmail.com";
    private static final String USER_PASSWORD = "123456";
    private static final String USER_HASHED_PASSWORD = "$argon2id$v=19$m=15360,t=2,p=1$TujujZu6svAVJhEP7pqJCRgjxY5Smsl4T+vtLLbAvHI$xNooVUBnPCHzH47o2UQbqdKD6LF9oKl44nN0qxVuWmsEenKixQhsHv8+nD8XI98iUYznINEMxnvl3sOMmrig6w";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void authenticateSuccess() {
        AppUser user = new AppUser();
        user.setEmail(USER_EMAIL);
        user.setPasswordHash(USER_HASHED_PASSWORD);

        when(userService.getUserByEmail(USER_EMAIL)).thenReturn(user);
        when(cryptoEngine.HashCheck(USER_HASHED_PASSWORD, USER_PASSWORD)).thenReturn(true);

        assertDoesNotThrow(() -> authService.authenticate(USER_EMAIL, USER_PASSWORD));
    }

    @Test
    void authenticateUserNotFound() {
        when(userService.getUserByEmail(USER_EMAIL)).thenReturn(null);

        assertThrows(AuthenticationException.class, () -> authService.authenticate(USER_EMAIL, USER_PASSWORD));
    }

    @Test
    void authenticateInvalidPassword() {
        AppUser user = new AppUser();
        user.setEmail(USER_EMAIL);
        user.setPasswordHash(USER_HASHED_PASSWORD);

        when(userService.getUserByEmail(USER_EMAIL)).thenReturn(user);
        when(cryptoEngine.HashCheck(USER_HASHED_PASSWORD, USER_PASSWORD)).thenReturn(false);

        assertThrows(AuthenticationException.class, () -> authService.authenticate(USER_EMAIL, USER_PASSWORD));
    }
}
