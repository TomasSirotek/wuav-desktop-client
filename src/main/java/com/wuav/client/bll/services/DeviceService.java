package com.wuav.client.bll.services;

import com.google.inject.Inject;
import com.wuav.client.be.device.Device;
import com.wuav.client.bll.services.interfaces.IDeviceService;
import com.wuav.client.dal.interfaces.IDeviceRepository;

import java.util.List;

/**
 * The implementation of the device service
 */

public class DeviceService implements IDeviceService {

    private final IDeviceRepository deviceRepository;

    /**
     * Constructor
     *
     * @param deviceRepository the device repository
     */
    @Inject
    public DeviceService(IDeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    /**
     * Gets all devices
     *
     * @return a list of devices
     */
    @Override
    public List<Device> getAllDevices() {
        return deviceRepository.getAllDevices();
    }

    /**
     * Gets all devices of the given type
     *
     * @param type the type of the devices
     * @return a list of devices
     */
    @Override
    public Device getDeviceById(int deviceId, Class<? extends Device> type) {
        return deviceRepository.getDeviceById(deviceId, type);
    }

    /**
     * Creates a device
     *
     * @param device the device to create
     * @return boolean if the device was created
     */
    @Override
    public boolean createDevice(Device device) {
        return deviceRepository.createDevice(device);
    }

    /**
     * Updates a device
     *
     * @param device the device to update
     * @return boolean if the device was updated
     */
    @Override
    public boolean updateDevice(Device device) {
        return deviceRepository.updateDevice(device);
    }

    /**
     * Deletes a device
     *
     * @param deviceId the id of the device to delete
     * @param type     the type of the device to delete
     * @return boolean if the device was deleted
     */
    @Override
    public boolean deleteDevice(int deviceId, Class<? extends Device> type) {
        return deviceRepository.deleteDevice(deviceId, type);
    }
}
