package com.wuav.client.bll.services;

import com.google.inject.Inject;
import com.wuav.client.be.device.Device;
import com.wuav.client.bll.services.interfaces.IDeviceService;
import com.wuav.client.dal.interfaces.IDeviceRepository;

import java.util.List;

public class DeviceService implements IDeviceService {

    private final IDeviceRepository deviceRepository;

    @Inject
    public DeviceService(IDeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public List<Device> getAllDevices() {
        return deviceRepository.getAllDevices();
    }

    @Override
    public Device getDeviceById(int deviceId, Class<? extends Device> type) {
        return deviceRepository.getDeviceById(deviceId, type);
    }

    @Override
    public boolean createDevice(Device device) {
        return deviceRepository.createDevice(device);
    }

    @Override
    public boolean updateDevice(Device device) {
        return deviceRepository.updateDevice(device);
    }

    @Override
    public boolean deleteDevice(int deviceId, Class<? extends Device> type) {
        return deviceRepository.deleteDevice(deviceId, type);
    }
}
