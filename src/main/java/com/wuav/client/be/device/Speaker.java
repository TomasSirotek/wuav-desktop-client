package com.wuav.client.be.device;

public class Speaker extends Device {

    public String volume;
    public String power;

    public Speaker(int id,String name) {
        super(id, name);
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
        return "Speaker{" +
                "volume='" + volume + '\'' +
                ", power='" + power + '\'' +
                '}';
    }
}

