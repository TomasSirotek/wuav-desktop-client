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
    public int addDeviceToProject(int projectId, int deviceId) {
        int finalAffectedRows = 0;
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            IDeviceMapper mapper = session.getMapper(IDeviceMapper.class);
            int affectedRows = mapper.addDeviceToProject(projectId, deviceId);

            session.commit();
            finalAffectedRows = affectedRows > 0 ? 1 : 0;
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }
        return finalAffectedRows;
    }

    @Override
    public boolean updateDevice(Device device) {
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            if (device instanceof Projector) {
                IProjectorMapper mapper = session.getMapper(IProjectorMapper.class);
                mapper.updateProjectorById(
                        device.getId(),
                        device.getName(),
                        ((Projector) device).getResolution(),
                        ((Projector) device).getConnectionType(),
                        ((Projector) device).getDevicePort()
                );
            } else if (device instanceof Speaker) {
                ISpeakerMapper mapper = session.getMapper(ISpeakerMapper.class);
                mapper.updateSpeakerById(
                        device.getId(),
                        device.getName(),
                        ((Speaker) device).getPower(),
                        ((Speaker) device).getVolume()
                );
            }
            session.commit();
            return true;
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
            return false;
        }
    }

    @Override
    public boolean deleteDevice(int deviceId, Class<? extends Device> type) {
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            if (type == Projector.class) {
                IProjectorMapper mapper = session.getMapper(IProjectorMapper.class);
                mapper.deleteProjectorById(deviceId);
            } else if (type == Speaker.class) {
                ISpeakerMapper mapper = session.getMapper(ISpeakerMapper.class);
                mapper.deleteSpeakerById(deviceId);
            }
            session.commit();
            return true;
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
            return false;
        }
    }


}
