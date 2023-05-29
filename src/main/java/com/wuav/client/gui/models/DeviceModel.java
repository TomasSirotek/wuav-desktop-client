package com.wuav.client.gui.models;

import com.google.inject.Inject;
import com.wuav.client.be.device.Device;
import com.wuav.client.bll.services.interfaces.IDeviceService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The type Device model.
 */
public class DeviceModel {

    private final IDeviceService deviceService;

    private ObservableList<Device> devices = FXCollections.observableArrayList();

    /**
     * Instantiates a new Device model.
     *
     * @param deviceService the device service
     */
    @Inject
    public DeviceModel(IDeviceService deviceService) {
        this.deviceService = deviceService;
    }

    /**
     * Gets all devices.
     *
     * @return the all devices list
     */
    public ObservableList<Device> getAllDevices() {
        return FXCollections.observableArrayList(deviceService.getAllDevices());
    }


    /**
     * Create device
     *
     * @param device the device
     * @return the boolean if the device was created
     */
    public boolean createDevice(Device device) {
        return deviceService.createDevice(device);
    }

    /**
     * Update device
     *
     * @param device the device
     * @return the boolean if the device was updated
     */
    public boolean updateDevice(Device device) {
        return deviceService.updateDevice(device);
    }

    /**
     * Delete device
     *
     * @param deviceId the device id
     * @param type     the type of the device
     * @return the boolean if the device was deleted
     */
    public boolean deleteDevice(int deviceId, Class<? extends Device> type) {
        return deviceService.deleteDevice(deviceId, type);
    }
}
