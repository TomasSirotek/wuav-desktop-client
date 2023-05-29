package com.wuav.client.be.device;


/**
 * Class for Projector
 */
public class Projector extends Device {

    private String resolution;
    private String connectionType;
    private String devicePort;


    /**
     * Constructor
     *
     * @param id         device id
     * @param name       device name
     * @param deviceType device type
     */
    public Projector(int id, String name, String deviceType) {
        super(id, name, deviceType);
    }

    /**
     * Method to get the resolution
     *
     * @return resolution
     */

    public String getResolution() {
        return resolution;
    }

    /**
     * Method to set the resolution
     *
     * @param resolution resolution
     */

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    /**
     * Method to get the connection type
     *
     * @return connection type
     */

    public String getConnectionType() {
        return connectionType;
    }

    /**
     * Method to set the connection type
     *
     * @param connectionType connection type
     */

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    /**
     * Method to get the device port
     *
     * @return device port
     */

    public String getDevicePort() {
        return devicePort;
    }

    /**
     * Method to set the device port
     *
     * @param devicePort device port
     */

    public void setDevicePort(String devicePort) {
        this.devicePort = devicePort;
    }
}