package com.wuav.client.gui.models;

import com.google.inject.Inject;
import com.wuav.client.be.device.Device;
import com.wuav.client.bll.services.interfaces.IDeviceService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DeviceModel {

    private final IDeviceService deviceService;

    private ObservableList<Device> devices = FXCollections.observableArrayList();

    @Inject
    public DeviceModel(IDeviceService deviceService) {
        this.deviceService = deviceService;
    }

    public ObservableList<Device> getAllDevices() {
       return FXCollections.observableArrayList(deviceService.getAllDevices());
    }

    public Device getDeviceById(int deviceId, Class<? extends Device> type) {
        return deviceService.getDeviceById(deviceId, type);
    }

    public boolean createDevice(Device device) {
        return deviceService.createDevice(device);
    }

    public boolean updateDevice(Device device) {
        return deviceService.updateDevice(device);
    }

    public boolean deleteDevice(int deviceId, Class<? extends Device> type) {
        return deviceService.deleteDevice(deviceId, type);
    }
}
