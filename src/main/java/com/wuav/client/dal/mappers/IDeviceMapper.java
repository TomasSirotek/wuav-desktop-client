package com.wuav.client.dal.mappers;

import com.wuav.client.be.device.Device;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * Interface for DeviceMapper
 */
public interface IDeviceMapper {

    /**
     * Gets all devices.
     *
     * @return the all devices
     */

    List<Device> getAllDevices();

    /**
     * Adds Device to project
     *
     * @param projectId the project id
     * @param deviceId  the device id
     * @return the int if > 0 then success else fail
     */

    int addDeviceToProject(@Param("projectId") int projectId, @Param("deviceId") int deviceId);

    /**
     * Gets Device by id
     *
     * @param deviceId the device id
     * @return the device by id object
     */
    Device getDeviceById(@Param("deviceId") int deviceId);

    /**
     * Creates Device
     *
     * @param device the device object
     * @return the int if > 0 then success else fail
     */
    int createDevice(Device device);

    /**
     * Updates Device.
     *
     * @param id   the id
     * @param name the name
     * @return the int if > 0 then success else fail
     */
    int updateDevice(@Param("deviceId") int id, @Param("name") String name);


    /**
     * Deletes Device.
     *
     * @param deviceId the id
     * @return the int if > 0 then success else fail
     */
    int deleteDeviceById(@Param("deviceId") int deviceId);
}
