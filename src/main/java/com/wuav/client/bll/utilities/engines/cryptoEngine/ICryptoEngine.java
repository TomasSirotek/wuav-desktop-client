package com.wuav.client.bll.utilities.engines.cryptoEngine;

/**
 * The interface for the crypto engine
 */
public interface ICryptoEngine {
    /**
     * Hashes the given text
     *
     * @param text the text to hash
     * @return the hashed text
     */
    String Hash(String text);

    /**
     * Checks if the given text matches the given hash
     *
     * @param hash the hash to check
     * @param text the text to check
     * @return boolean if the text matches the hash
     */
    Boolean HashCheck(String hash, String text);
}
