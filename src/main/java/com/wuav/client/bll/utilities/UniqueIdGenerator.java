package com.wuav.client.bll.utilities;

import java.math.BigInteger;
import java.util.UUID;


/**
 * The unique id generator
 */
public class UniqueIdGenerator {

    /**
     * Generates a unique id
     *
     * @return the unique id
     */
    public static int generateUniqueId() {
        // Generate a random UUID
        UUID uuid = UUID.randomUUID();
        // Convert the UUID into a string, remove hyphen characters, then convert the resulting hexadecimal number to a BigInteger
        BigInteger bigInteger = new BigInteger(uuid.toString().replace("-", ""), 16);
        // Return the BigInteger modulus the maximum integer value as an integer.
        // This ensures that the returned value is within the range of valid integer values.
        return bigInteger.mod(BigInteger.valueOf(Integer.MAX_VALUE)).intValue();
    }
}