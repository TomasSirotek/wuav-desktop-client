package com.wuav.client.bll.services.interfaces;

import com.wuav.client.be.device.Device;

import java.util.List;

/**
 * The interface for the device service
 */
public interface IDeviceService {

    /**
     * Gets all devices
     *
     * @return a list of devices
     */
    List<Device> getAllDevices();

    /**
     * Creates a device
     *
     * @param device the device to create
     * @return boolean if the device was created
     */
    boolean createDevice(Device device);

    /**
     * Updates a device
     *
     * @param device the device to update
     * @return boolean if the device was updated
     */
    boolean updateDevice(Device device);

    /**
     * Deletes a device
     *
     * @param deviceId the id of the device to delete
     * @param type     the type of the device to delete
     * @return boolean if the device was deleted
     */
    boolean deleteDevice(int deviceId, Class<? extends Device> type);
}
