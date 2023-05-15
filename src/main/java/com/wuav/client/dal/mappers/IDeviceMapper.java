package com.wuav.client.dal.mappers;

import com.wuav.client.be.device.Device;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IDeviceMapper {

    List<Device> getAllDevices();

    int addDeviceToProject(@Param("projectId") int projectId,@Param("deviceId") int deviceId);

    Device getDeviceById(@Param("deviceId") int deviceId);

    int createDevice(Device device);

    int updateDeviceById(@Param("deviceId") int id, @Param("name") String name,@Param("deviceType") String deviceType);

    int updateDevice(@Param("deviceId") int id,@Param("name") String name);

    String getDeviceTypeById(@Param("deviceId") int id);

    int deleteDeviceById(@Param("deviceId") int deviceId);
}
