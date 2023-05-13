package com.wuav.client.bll.utilities.engines.cryptoEngine;

public interface ICryptoEngine {
    String Hash(String text);
    Boolean HashCheck(String hash, String text);
}
