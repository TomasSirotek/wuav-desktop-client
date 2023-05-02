package com.wuav.client.bll.utilities;

import java.math.BigInteger;
import java.util.UUID;

public class UniqueIdGenerator {
    public static int generateUniqueId() {
        UUID uuid = UUID.randomUUID();
        BigInteger bigInteger = new BigInteger(uuid.toString().replace("-", ""), 16);
        return bigInteger.mod(BigInteger.valueOf(Integer.MAX_VALUE)).intValue();
    }
}