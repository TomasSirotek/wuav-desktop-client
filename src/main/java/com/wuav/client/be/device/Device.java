package com.wuav.client.be.device;

/**
 * Abstract class for all devices
 */
public abstract class Device {
    private int id;
    private String name;
    private String deviceType;

    /**
     * Constructor
     *
     * @param id         device id
     * @param name       device name
     * @param deviceType device type
     */
    public Device(int id, String name, String deviceType) {
        this.id = id;
        this.name = name;
        this.deviceType = deviceType;
    }

    /**
     * Method to get the device type
     *
     * @return device type
     */
    public String getDeviceType() {
        return deviceType;
    }

    /**
     * Method to set the device type
     *
     * @param deviceType device type
     */
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    /**
     * Method to get the device name
     *
     * @return device name
     */
    public String getName() {
        return name;
    }

    /**
     * Method to set the device name
     *
     * @param name device name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method to get the device id
     *
     * @return device id
     */
    public int getId() {
        return id;
    }

    /**
     * Method to set the device id
     *
     * @param id device id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Method get device as String
     *
     * @return device name
     */
    @Override
    public String toString() {
        return name;
    }
}

