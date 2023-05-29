package com.wuav.client.dal.interfaces;

import com.wuav.client.be.device.Device;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * Interface for the DeviceRepository class.
 */
public interface IDeviceRepository {

    /**
     * Gets all devices.
     *
     * @return A list of all devices.
     */
    List<Device> getAllDevices();


    /**
     * Gets all devices of a certain type.
     *
     * @param type The type of devices to get.
     * @return A list of all devices of the given type.
     */
    Device getDeviceById(int deviceId, Class<? extends Device> type);


    /**
     * Creates a new device.
     *
     * @param device The device to create.
     * @return True if the device was created successfully, false otherwise.
     */
    boolean createDevice(Device device);


    /**
     * Updates a device.
     *
     * @param device The device to update.
     * @return True if the device was updated successfully, false otherwise.
     */
    boolean updateDevice(Device device);

    /**
     * Deletes a device.
     *
     * @param deviceId The id of the device to delete.
     * @param type     The type of device to delete.
     * @return True if the device was deleted successfully, false otherwise.
     */

    boolean deleteDevice(int deviceId, Class<? extends Device> type);

    /**
     * Adds a device to a project.
     *
     * @param session   The session.
     * @param projectId The id of the project to add the device to.
     * @param deviceId  The id of the device to add to the project.
     * @return True if the device was added successfully, false otherwise.
     * @throws Exception If an error occurs.
     */
    int addDeviceToProject(SqlSession session, int projectId, int deviceId) throws Exception;
}

