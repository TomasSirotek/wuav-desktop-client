package com.wuav.client.be.device;

import java.util.ArrayList;
import java.util.List;

public class MockDevices {
    public static List<Device> generateDevices() {
        List<Device> devices = new ArrayList<>();
        devices.add(new Projector(0,"Projector 1"));
        devices.add(new Speaker(0,"Speaker 2"));
        return devices;
    }
}
