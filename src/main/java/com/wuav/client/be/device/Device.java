package com.wuav.client.be.device;

import java.util.Map;

public abstract class Device {

    public int id;
    public String name;
    public String deviceType;

    public Device(int id, String name, String deviceType) {
        this.id = id;
        this.name = name;
        this.deviceType = deviceType;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }
}

