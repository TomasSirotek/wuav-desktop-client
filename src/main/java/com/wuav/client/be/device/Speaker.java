package com.wuav.client.be.device;

import java.util.HashMap;
import java.util.Map;

public class Speaker extends Device {

    private String volume;
    private String power;

    public Speaker(int id, String name,String deviceType) {
        super(id, name,deviceType);
    }


    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    @Override
    public String toString() {
        return getName();
    }
}

