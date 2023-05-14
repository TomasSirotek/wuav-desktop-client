package com.wuav.client.dal.repository;

import com.wuav.client.be.device.Device;
import com.wuav.client.be.device.Projector;
import com.wuav.client.be.device.Speaker;
import com.wuav.client.dal.interfaces.IDeviceRepository;
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

    @Override
    public List<Device> getAllDevices() {
        List<Device> allDevices = new ArrayList<>();
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            IProjectorMapper projectorMapper = session.getMapper(IProjectorMapper.class);
            ISpeakerMapper speakerMapper = session.getMapper(ISpeakerMapper.class);
            allDevices.addAll(projectorMapper.getAllProjectors());
            allDevices.addAll(speakerMapper.getAllSpeakers());
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }
        return allDevices;
    }

    @Override
    public Device getDeviceById(int deviceId, Class<? extends Device> type) {
        Device device = null;
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            if (type == Projector.class) {
                IProjectorMapper mapper = session.getMapper(IProjectorMapper.class);
                device = mapper.getProjectorById(deviceId);
            } else if (type == Speaker.class) {
                ISpeakerMapper mapper = session.getMapper(ISpeakerMapper.class);
                device = mapper.getSpeakerById(deviceId);
            }
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }
        return device;
    }

    @Override
    public boolean createDevice(Device device) {
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            if (device instanceof Projector) {
                IProjectorMapper mapper = session.getMapper(IProjectorMapper.class);
                mapper.createProjector((Projector) device);
            } else if (device instanceof Speaker) {
                ISpeakerMapper mapper = session.getMapper(ISpeakerMapper.class);
                mapper.createSpeaker((Speaker) device);
            }
            session.commit();
            return true;
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
            return false;
        }
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
