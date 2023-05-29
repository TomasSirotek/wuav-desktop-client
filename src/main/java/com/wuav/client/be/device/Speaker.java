package com.wuav.client.be.device;

/**
 * Class for Speaker
 */
public class Speaker extends Device {

    private String volume;
    private String power;

    /**
     * Constructor
     *
     * @param id         device id
     * @param name       device name
     * @param deviceType device type
     */

    public Speaker(int id, String name, String deviceType) {
        super(id, name, deviceType);
    }

    /**
     * Method to get the volume
     *
     * @return volume
     */

    public String getVolume() {
        return volume;
    }

    /**
     * Method to set the volume
     *
     * @param volume volume
     */

    public void setVolume(String volume) {
        this.volume = volume;
    }

    /**
     * Method to get the power
     *
     * @return power
     */

    public String getPower() {
        return power;
    }

    /**
     * Method to set the power
     *
     * @param power power
     */

    public void setPower(String power) {
        this.power = power;
    }

}

