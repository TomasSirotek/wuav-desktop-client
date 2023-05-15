package com.wuav.client.dal.mappers;

import com.wuav.client.be.device.Device;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IDeviceMapper {

    List<Device> getAllDevices();

    int addDeviceToProject(@Param("projectId") int projectId,@Param("deviceId") int deviceId);

    Device getDeviceById(@Param("deviceId") int deviceId);

    int createDevice(Device device);
}
