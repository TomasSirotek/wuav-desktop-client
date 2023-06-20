package com.wuav.client.bll.utilities.engines.cryptoEngine;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

public class CryptoEngine implements ICryptoEngine {

    /**
     * static = initiliazed once, associated with the class itself, not instance, final = cannot be changed
     */
    private final static Argon2PasswordEncoder encoder =
            new Argon2PasswordEncoder(32, 64, 1, 15 * 1024, 2);

    /**
     * Hashes the given text
     *
     * @param text the text to hash
     * @return the hashed text
     */
    @Override
    public String Hash(String text) {
        return encoder.encode(text);
    }

    /**
     * Checks if the given text matches the given hash
     *
     * @param hash the hash to check
     * @param text the text to check
     * @return boolean if the text matches the hash
     */
    @Override
    public Boolean HashCheck(String hash, String text) {
        return encoder.matches(text, hash);
    }
}