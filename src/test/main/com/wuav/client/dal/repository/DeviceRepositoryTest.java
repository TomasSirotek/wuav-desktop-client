package com.wuav.client.dal.repository;

import com.wuav.client.be.device.Device;
import com.wuav.client.be.device.Projector;
import com.wuav.client.be.device.Speaker;
import com.wuav.client.bll.utilities.UniqueIdGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DeviceRepositoryTest {

    private static final int EXISTING_PROJECTOR_ID = 10387399;
    private static final int EXISTING_SPEAKER_ID = 111111;
    private static final int EXISTING_DEVICE_ID = 1207741913;
    private static final int EXISTING_PROJECT_ID = 358550511;

    DeviceRepository deviceRepository = new DeviceRepository();

    @Test
    public void testCreateAndFetchDeviceProjector() {
        // Arrange
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
        // Act
        Device deleteResult = deviceRepository.getDeviceById(EXISTING_DEVICE_ID, Device.class);

        System.out.println(deleteResult.toString());
        assertEquals(EXISTING_DEVICE_ID,deleteResult.getId());
    }

    @Test
    public void testUpdateDeviceProjectorName() {
        // Arrange

        Device device = deviceRepository.getDeviceById(EXISTING_PROJECTOR_ID, Projector.class);
        String newName = "Updated";

        // Act
        device.setName(newName);
        boolean updateResult = deviceRepository.updateDevice(device);

        // Assert
        assertTrue(updateResult, "Failed to update device");
        Device updatedDevice = deviceRepository.getDeviceById(EXISTING_PROJECTOR_ID, Projector.class);
        assertEquals(newName, updatedDevice.getName(), "Device name was not updated");
    }


    @Test
    public void testUpdateCompleteProjector() {
        // Arrange

        Device device = deviceRepository.getDeviceById(EXISTING_PROJECTOR_ID, Projector.class);
        String newName = "Updated";

        // Act
        device.setName(newName);
        boolean updateResult = deviceRepository.updateDevice(device);

        // Assert
        assertTrue(updateResult, "Failed to update device");
        Device updatedDevice = deviceRepository.getDeviceById(EXISTING_PROJECTOR_ID, Projector.class);
        assertEquals(newName, updatedDevice.getName(), "Device name was not updated");
    }
    @Test
    public void testUpdateDeviceSpeakerVolume() {
        // Arrange

        Device device = deviceRepository.getDeviceById(EXISTING_SPEAKER_ID, Speaker.class);
        String newVolume = "666";

        // Act
        ((Speaker) device).setVolume(newVolume);
        boolean updateResult = deviceRepository.updateDevice(device);

        // Assert
        // Assert
        assertTrue(updateResult, "Failed to update device");
        Device updatedDevice = deviceRepository.getDeviceById(EXISTING_SPEAKER_ID, Speaker.class);
        assertEquals(newVolume, ((Speaker) updatedDevice).getVolume(), "Speaker volume was not updated");
    }

    @Test
    public void testUpdateDeviceProjectorResolution() {
        // Arrange

        Device device = deviceRepository.getDeviceById(EXISTING_PROJECTOR_ID, Projector.class);
        String resolution = "ULTRA HD(1890x1920)";

        // Act
        ((Projector) device).setResolution(resolution);
        boolean updateResult = deviceRepository.updateDevice(device);

        // Assert
        // Assert
        assertTrue(updateResult, "Failed to update device");
        Device updatedDevice = deviceRepository.getDeviceById(EXISTING_PROJECTOR_ID, Projector.class);
        assertEquals(resolution, ((Projector) updatedDevice).getResolution(), "Speaker volume was not updated");
    }

//    @Test
//    void testAddDeviceToProject() {
//        // Act
//        int affectedRows = deviceRepository.addDeviceToProject(
//                EXISTING_PROJECT_ID,
//                EXISTING_DEVICE_ID
//        );
//
//        // Assert
//        Assertions.assertEquals(1, affectedRows, "Device was not added to the project");
//    }


    @Test
    public void testDeleteDeviceSpeaker() {
        // Act
        boolean deleteResult = deviceRepository.deleteDevice(EXISTING_SPEAKER_ID, Speaker.class);

        // Assert
        assertTrue(deleteResult, "Failed to delete device");
    }

    @Test
    public void testDeleteDeviceProjector() {
        // Act
        boolean deleteResult = deviceRepository.deleteDevice(EXISTING_PROJECTOR_ID, Projector.class);

        // Assert
        assertTrue(deleteResult, "Failed to delete device");
    }


    @Test
    public void testGetAllDevices() {
        // Act
        List<Device> devices = deviceRepository.getAllDevices();
        // Assert
        assertNotNull(devices, "Device list is null");
        assertFalse(devices.isEmpty(), "Device list is empty");
    }
}
