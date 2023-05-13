package com.wuav.client.be.device;

public abstract class Device {

    private int id;
    private String name;

    public Device(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Device{" +
                "name='" + name + '\'' +
                '}';
    }
}

