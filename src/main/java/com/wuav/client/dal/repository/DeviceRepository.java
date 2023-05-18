package com.wuav.client.dal.repository;

import com.wuav.client.be.device.Device;
import com.wuav.client.be.device.Projector;
import com.wuav.client.be.device.Speaker;
import com.wuav.client.dal.interfaces.IDeviceRepository;
import com.wuav.client.dal.mappers.IDeviceMapper;
import com.wuav.client.dal.mappers.IProjectMapper;
import com.wuav.client.dal.mappers.IProjectorMapper;
import com.wuav.client.dal.mappers.ISpeakerMapper;
import com.wuav.client.dal.myBatis.MyBatisConnectionFactory;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DeviceRepository implements IDeviceRepository {

    Logger logger = LoggerFactory.getLogger(DeviceRepository.class);


    // FINISHED AND WORKING TESTED
    @Override
    public List<Device> getAllDevices() {
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            IDeviceMapper deviceMapper = session.getMapper(IDeviceMapper.class);
            return deviceMapper.getAllDevices();
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
            return new ArrayList<>();
        }
    }

    // FINISHED AND WORKING TESTED
    @Override
    public Device getDeviceById(int deviceId, Class<? extends Device> type) {
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            IDeviceMapper deviceMapper = session.getMapper(IDeviceMapper.class);
            return deviceMapper.getDeviceById(deviceId);
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }
        return null;
    }

    // FINISHED AND WORKING TESTED
    @Override
    public boolean createDevice(Device device) {
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            IDeviceMapper deviceMapper = session.getMapper(IDeviceMapper.class);
            int affectedRows = deviceMapper.createDevice(device);
            if (device instanceof Projector) {
                IProjectorMapper projectorMapper = session.getMapper(IProjectorMapper.class);
                affectedRows += projectorMapper.createProjector((Projector) device);
            } else if (device instanceof Speaker) {
                ISpeakerMapper speakerMapper = session.getMapper(ISpeakerMapper.class);
                affectedRows += speakerMapper.createSpeaker((Speaker)device);
            }
            session.commit();
            return affectedRows > 0;
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
            return false;
        }
    }

    @Override
    public int addDeviceToProject(SqlSession session,int projectId, int deviceId) throws Exception {
        try {
            IDeviceMapper mapper = session.getMapper(IDeviceMapper.class);
            int affectedRows = mapper.addDeviceToProject(projectId, deviceId);

            return affectedRows;
        } catch (PersistenceException ex) {
            logger.error("An error occurred mapping tables", ex);
            throw new Exception(ex);
        }
    }

    // FINISHED AND WORKING TESTED
    @Override
    public boolean updateDevice(Device device) {
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            IDeviceMapper deviceMapper = session.getMapper(IDeviceMapper.class);

            int affectedRows = 0;
            // If the device type hasn't changed, just update the device
            affectedRows += deviceMapper.updateDevice(device.getId(),device.getName());
                if (device instanceof Projector) {
                    IProjectorMapper mapper = session.getMapper(IProjectorMapper.class);
                    affectedRows += mapper.updateProjectorById((Projector) device);
                } else if (device instanceof Speaker) {
                    ISpeakerMapper mapper = session.getMapper(ISpeakerMapper.class);
                    affectedRows += mapper.updateSpeakerById((Speaker) device);
                }

            session.commit();
            return affectedRows > 0;
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
            return false;
        }
    }

    @Override
    public boolean deleteDevice(int deviceId, Class<? extends Device> type) {
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            IDeviceMapper deviceMapper = session.getMapper(IDeviceMapper.class);
            int affectedRows = 0;

            if (type == Projector.class) {
                IProjectorMapper mapper = session.getMapper(IProjectorMapper.class);
                affectedRows += mapper.deleteProjectorById(deviceId);
            } else if (type == Speaker.class) {
                ISpeakerMapper mapper = session.getMapper(ISpeakerMapper.class);
                affectedRows += mapper.deleteSpeakerById(deviceId);
            }
            affectedRows += deviceMapper.deleteDeviceById(deviceId);
            session.commit();
            return affectedRows > 0;
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
            return false;
        }
    }


}
