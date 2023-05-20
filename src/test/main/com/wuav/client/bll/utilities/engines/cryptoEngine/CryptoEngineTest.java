package com.wuav.client.bll.utilities.engines.cryptoEngine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CryptoEngineTest {

    private ICryptoEngine cryptoEngine;


    @BeforeEach
    public void setUp() {
        cryptoEngine = new CryptoEngine();
    }

    @Test
    public void testHash() {
        // Given
        String password = "password";

        // When
        String hash = cryptoEngine.Hash(password);

        // Then
        assertNotNull(hash);
        assertFalse(hash.isEmpty());
    }

    @Test
    public void testHashCheck() {
        // Given
        String password = "password";

        // When
        String hash = cryptoEngine.Hash(password);

        // Then
        assertTrue(cryptoEngine.HashCheck(hash, password));
        assertFalse(cryptoEngine.HashCheck(hash, "wrongPassword"));
    }

    @Test
    public void testHashCheckWrongPassword() {
        // Given
        String password = "password";

        // When
        String hash = cryptoEngine.Hash(password);

        // Then
        assertFalse(cryptoEngine.HashCheck(hash, "wrongPassword"));
    }

    @Test
    public void testHashCheckNullHash() {
        // Given
        String password = "password";

        // Then
        assertFalse(cryptoEngine.HashCheck(null, password));
    }


    @Test
    public void testHashCheckEmptyHash() {
        // Given
        String password = "password";

        // Then
        assertFalse(cryptoEngine.HashCheck("", password));
    }


}