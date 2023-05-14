package com.wuav.client.bll.services.interfaces;

import com.wuav.client.be.device.Device;

import java.util.List;

public interface IDeviceService {
    List<Device> getAllDevices();

    Device getDeviceById(int deviceId, Class<? extends Device> type);

    boolean createDevice(Device device);

    boolean updateDevice(Device device);

    boolean deleteDevice(int deviceId, Class<? extends Device> type);
}
