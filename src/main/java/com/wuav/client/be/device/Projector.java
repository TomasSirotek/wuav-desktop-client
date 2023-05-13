package com.wuav.client.be.device;

public class Projector extends Device {

    private String resolution;
    private String connectionType;
    public Projector(String name) {
        super(name);
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }
}