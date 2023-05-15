package com.wuav.client.dal.repository;

import com.wuav.client.be.device.Device;
import com.wuav.client.be.device.Projector;
import com.wuav.client.be.device.Speaker;
import com.wuav.client.bll.utilities.UniqueIdGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DeviceRepositoryTest {

    // Assuming these values exist in your test database
    private static final int EXISTING_PROJECTOR_ID = 3132;
    private static final int EXISTING_SPEAKER_ID = 2324234;

    private static final int EXISTING_DEVICE_ID = 1207741913;


    @Test
    public void testCreateAndFetchDeviceProjector() {
        // Arrange
        DeviceRepository deviceRepository = new DeviceRepository();
        var uniqueIdGenerator = UniqueIdGenerator.generateUniqueId();

        Projector projector = new Projector(uniqueIdGenerator, "SWIFT PROJECTOR", Projector.class.getSimpleName().toUpperCase());
        projector.setResolution("1080p");
        projector.setConnectionType("HDMI");
        projector.setDevicePort("USB");

        // Act
        boolean createResult = deviceRepository.createDevice(projector);

        // Assert
        assertTrue(createResult, "Failed to create device");
        Device fetchedDevice = deviceRepository.getDeviceById(uniqueIdGenerator, Device.class);
        assertNotNull(fetchedDevice, "Fetched device is null");
        assertEquals(projector.getId(), fetchedDevice.getId(), "Fetched device ID does not match original device ID");
    }

    @Test
    public void testCreateAndFetchDeviceSpeaker() {
        // Arrange
        DeviceRepository deviceRepository = new DeviceRepository();
        var uniqueIdGenerator = UniqueIdGenerator.generateUniqueId();

        Speaker speaker = new Speaker(uniqueIdGenerator, "FOO SPEAKER",Speaker.class.getSimpleName().toUpperCase());
        speaker.setPower("1000W");
        speaker.setVolume("30");

        // Act
        boolean createResult = deviceRepository.createDevice(speaker);

        // Assert
        assertTrue(createResult, "Failed to create device");
        Device fetchedDevice = deviceRepository.getDeviceById(uniqueIdGenerator, Device.class);
        assertNotNull(fetchedDevice, "Fetched device is null");
        assertEquals(speaker.getId(), fetchedDevice.getId(), "Fetched device ID does not match original device ID");
    }

    @Test
    public void getDeviceById() {
        // Arrange
        DeviceRepository deviceRepository = new DeviceRepository();

        // Act
        Device deleteResult = deviceRepository.getDeviceById(EXISTING_DEVICE_ID, Device.class);

        System.out.println(deleteResult.toString());
        assertEquals(EXISTING_DEVICE_ID,deleteResult.id);
    }

    @Test
    public void testUpdateDevice() {
        // Arrange
        DeviceRepository deviceRepository = new DeviceRepository();
        Device device = deviceRepository.getDeviceById(EXISTING_PROJECTOR_ID, Projector.class);
        String newName = "Updated Projector";

        // Act
        device.setName(newName);
        boolean updateResult = deviceRepository.updateDevice(device);

        // Assert
        assertTrue(updateResult, "Failed to update device");
        Device updatedDevice = deviceRepository.getDeviceById(EXISTING_PROJECTOR_ID, Projector.class);
        assertEquals(newName, updatedDevice.getName(), "Device name was not updated");
    }

    @Test
    public void testDeleteDevice() {
        // Arrange
        DeviceRepository deviceRepository = new DeviceRepository();

        // Act
        boolean deleteResult = deviceRepository.deleteDevice(EXISTING_SPEAKER_ID, Speaker.class);

        // Assert
        assertTrue(deleteResult, "Failed to delete device");
        Device deletedDevice = deviceRepository.getDeviceById(EXISTING_SPEAKER_ID, Speaker.class);
        assertNull(deletedDevice, "Device was not deleted");
    }

    @Test
    public void testGetAllDevices() {
        // Arrange
        DeviceRepository deviceRepository = new DeviceRepository();

        // Act
        List<Device> devices = deviceRepository.getAllDevices();

        // Assert
        assertNotNull(devices, "Device list is null");
        assertFalse(devices.isEmpty(), "Device list is empty");
    }
}
