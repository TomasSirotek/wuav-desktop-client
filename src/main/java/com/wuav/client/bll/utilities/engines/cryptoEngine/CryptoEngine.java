package com.wuav.client.bll.utilities.engines.cryptoEngine;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
public class CryptoEngine implements ICryptoEngine {

    private final static Argon2PasswordEncoder encoder =
            new Argon2PasswordEncoder(32, 64, 1, 15 * 1024, 2);
    @Override
    public String Hash(String text) {
        return encoder.encode(text);
    }
    @Override
    public Boolean HashCheck(String hash, String text) {
        return encoder.matches(text,hash);
    }
}