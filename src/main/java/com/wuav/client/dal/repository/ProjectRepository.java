package com.wuav.client.dal.repository;

import com.wuav.client.be.Project;
import com.wuav.client.be.device.Device;
import com.wuav.client.dal.interfaces.IProjectRepository;
import com.wuav.client.dal.mappers.IDeviceMapper;
import com.wuav.client.dal.mappers.IProjectMapper;
import com.wuav.client.dal.myBatis.MyBatisConnectionFactory;
import com.wuav.client.gui.dto.CreateProjectDTO;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ProjectRepository implements IProjectRepository {
    static Logger logger = LoggerFactory.getLogger(ProjectRepository.class);

    @Override
    public List<Project> getAllProjectsByUserId(int userId) {
        List<Project> fetchedProjects = new ArrayList<>();
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            IProjectMapper mapper = session.getMapper(IProjectMapper.class);

            fetchedProjects = mapper.getAllProjectsByUserId(userId);

        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }
        return fetchedProjects;
    }

    @Override
    public List<Project> getAllProjects() {
        List<Project> fetchedProjects = new ArrayList<>();
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            IProjectMapper mapper = session.getMapper(IProjectMapper.class);
            fetchedProjects = mapper.getAllProjects();
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }
        return fetchedProjects;
    }

    @Override
    public Project getProjectById(int projectId) {
        Project fetchedProject = new Project();
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            IProjectMapper mapper = session.getMapper(IProjectMapper.class);
            fetchedProject = mapper.getProjectById(projectId);
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }
        return fetchedProject;
    }


    @Override
    public Project updateProject(int projectId, String description) {
        Project updatedProject = null;

        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            IProjectMapper mapper = session.getMapper(IProjectMapper.class);
            mapper.updateProjectForUserById(projectId, description);
            updatedProject = mapper.getProjectById(projectId);
            session.commit();
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }
        return updatedProject;
    }

    @Override
    public boolean createProject(CreateProjectDTO projectDTO) {
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            IProjectMapper mapper = session.getMapper(IProjectMapper.class);
            var affectedRows = mapper.createProject(
                    projectDTO.id(),
                    projectDTO.name(),
                    projectDTO.description(),
                    projectDTO.customer().id()
            );

            session.commit();
            return affectedRows > 0;
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
            return false;
        }
    }

    @Override
    public int addProjectToUser(int userId, int projectId) {
        int finalAffectedRows = 0;
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            IProjectMapper mapper = session.getMapper(IProjectMapper.class);
            int affectedRows = mapper.addUserToProject(userId, projectId);

            session.commit();
            finalAffectedRows = affectedRows > 0 ? 1 : 0;
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }
        return finalAffectedRows;
    }

    @Override
    public int addDeviceToProject(int projectId, int deviceId) {
        int finalAffectedRows = 0;
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            IProjectMapper mapper = session.getMapper(IProjectMapper.class);
            int affectedRows = mapper.addDeviceToProject(projectId, deviceId);

            session.commit();
            finalAffectedRows = affectedRows > 0 ? 1 : 0;
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }
        return finalAffectedRows;
    }


    @Override
    public boolean updateNotes(int projectId, String content) {
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            IProjectMapper mapper = session.getMapper(IProjectMapper.class);
            var affectedRows = mapper.updateNotes(
                    projectId,
                    content
            );
            session.commit();
            return affectedRows > 0;
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }
        return false;
    }

    @Override
    public boolean deleteProjectById(int id) {
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            IProjectMapper mapper = session.getMapper(IProjectMapper.class);
            var affectedRows = mapper.deleteProjectById(
                  id
            );
            session.commit();
            return affectedRows > 0;
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }
        return false;
    }
}


