package com.wuav.client.dal.interfaces;

import com.wuav.client.be.Address;
import com.wuav.client.be.device.Device;
import com.wuav.client.gui.dto.AddressDTO;
import com.wuav.client.gui.dto.PutAddressDTO;

import java.util.List;

public interface IDeviceRepository {

    List<Device> getAllDevices();

    Device getDeviceById(int deviceId, Class<? extends Device> type);

    boolean createDevice(Device device);

    boolean updateDevice(Device device);

    boolean deleteDevice(int deviceId, Class<? extends Device> type);

    int addDeviceToProject(int projectId, int deviceId);
}

