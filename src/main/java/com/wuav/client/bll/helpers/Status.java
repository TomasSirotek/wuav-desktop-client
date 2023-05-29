package com.wuav.client.bll.helpers;

/**
 * Enum for Status
 */
public enum Status {
    ACTIVE(1),
    COMPLETED(2),
    DELETED(3);

    private int value;

    private Status(int value) {
        this.value = value;
    }

    /**
     * Method to get the value
     *
     * @return value
     */
    public int getValue() {
        return value;
    }
}
