package com.wuav.client.dal.repository;

import com.wuav.client.be.Project;
import com.wuav.client.dal.interfaces.IProjectRepository;
import com.wuav.client.dal.mappers.IProjectMapper;
import com.wuav.client.dal.myBatis.MyBatisConnectionFactory;
import com.wuav.client.gui.dto.CreateProjectDTO;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * ProjectRepository class.
 */
public class ProjectRepository implements IProjectRepository {
    private Logger logger = LoggerFactory.getLogger(ProjectRepository.class);

    /**
     * Get all projects.
     *
     * @return List<Project>
     * @throws Exception Exception
     */
    @Override
    public List<Project> getAllProjects() throws Exception {
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            IProjectMapper mapper = session.getMapper(IProjectMapper.class);
            return mapper.getAllProjects();
        } catch (PersistenceException ex) {
            logger.error("An error occurred mapping tables", ex);
            throw new Exception(ex);
        }
    }


    /**
     * Get all projects by user id.
     *
     * @param userId int
     * @return List<Project>
     * @throws Exception Exception
     */
    @Override
    public List<Project> getAllProjectsByUserId(int userId) throws Exception {
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            IProjectMapper mapper = session.getMapper(IProjectMapper.class);
            return mapper.getAllProjectsByUserId(userId);
        } catch (PersistenceException ex) {
            logger.error("An error occurred mapping tables", ex);
            throw new Exception(ex);
        }
    }

    /**
     * Get project by id.
     *
     * @param projectId int
     * @return Project
     * @throws Exception Exception
     */
    @Override
    public Project getProjectById(int projectId) throws Exception {
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            IProjectMapper mapper = session.getMapper(IProjectMapper.class);
            return mapper.getProjectById(projectId);
        } catch (PersistenceException ex) {
            logger.error("An error occurred mapping tables", ex);
            throw new Exception(ex);
        }
    }

    /**
     * Create a new project.
     *
     * @param projectId   the project id
     * @param description the description
     * @return Project
     * @throws Exception Exception
     */
    @Override
    public Project updateProject(int projectId, String description) throws Exception {
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            IProjectMapper mapper = session.getMapper(IProjectMapper.class);
            mapper.updateProjectForUserById(projectId, description);
            session.commit();
            return mapper.getProjectById(projectId);
        } catch (PersistenceException ex) {
            logger.error("An error occurred mapping tables", ex);
            throw new Exception(ex);
        }
    }

    /**
     * Create a new project.
     *
     * @param session    SqlSession
     * @param projectDTO CreateProjectDTO
     * @return boolean if the project was created
     * @throws Exception Exception if the project was not created
     */
    @Override
    public boolean createProject(SqlSession session, CreateProjectDTO projectDTO) throws Exception {
        try {
            IProjectMapper mapper = session.getMapper(IProjectMapper.class);
            var affectedRows = mapper.createProject(
                    projectDTO.id(),
                    projectDTO.name(),
                    projectDTO.description(),
                    projectDTO.customer().id()
            );
            return affectedRows > 0;
        } catch (PersistenceException ex) {
            logger.error("An error occurred mapping tables", ex);
            throw new Exception(ex);
        }
    }

    /**
     * Add a user to a project.
     *
     * @param session   SqlSession
     * @param userId    int
     * @param projectId int
     * @return int if the user was added to the project (affected rows)
     * @throws Exception Exception if the user was not added to the project
     */
    @Override
    public int addProjectToUser(SqlSession session, int userId, int projectId) throws Exception {
        try {
            IProjectMapper mapper = session.getMapper(IProjectMapper.class);
            return mapper.addUserToProject(userId, projectId);
        } catch (PersistenceException ex) {
            logger.error("An error occurred mapping tables", ex);
            throw new Exception(ex);
        }
    }

    /**
     * Add a device to a project.
     *
     * @param session   SqlSession
     * @param projectId int project id
     * @param deviceId  int device id
     * @return int if the device was added to the project (affected rows)
     * @throws Exception Exception if the device was not added to the project
     */
    @Override
    public int addDeviceToProject(SqlSession session, int projectId, int deviceId) throws Exception {
        try {
            IProjectMapper mapper = session.getMapper(IProjectMapper.class);
            return mapper.addDeviceToProject(projectId, deviceId);
        } catch (PersistenceException ex) {
            logger.error("An error occurred mapping tables", ex);
            throw new Exception(ex);
        }
    }

    /**
     * Update project name.
     *
     * @param projectId the project id
     * @param newName   the new name
     * @return boolean if the project name was updated
     * @throws Exception Exception
     */
    @Override
    public boolean updateProjectName(int projectId, String newName) throws Exception {
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            IProjectMapper mapper = session.getMapper(IProjectMapper.class);
            var affectedRows = mapper.updateProjectName(
                    projectId,
                    newName
            );
            session.commit();
            return affectedRows > 0;
        } catch (PersistenceException ex) {
            throw new Exception(ex);
        }
    }

    /**
     * Update project notes
     *
     * @param projectId the project id
     * @param content   the content
     * @return boolean if the project notes were updated
     * @throws Exception Exception
     */
    @Override
    public boolean updateNotes(int projectId, String content) throws Exception {
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            IProjectMapper mapper = session.getMapper(IProjectMapper.class);
            var affectedRows = mapper.updateNotes(
                    projectId,
                    content
            );
            session.commit();
            return affectedRows > 0;
        } catch (PersistenceException ex) {
            throw new Exception(ex);
        }
    }

    /**
     * Delete project by id.
     *
     * @param session the session
     * @param id      the id
     * @return boolean if the project was deleted
     * @throws Exception Exception if the project was not deleted
     */
    @Override
    public boolean deleteProjectById(SqlSession session, int id) throws Exception {
        try {
            IProjectMapper mapper = session.getMapper(IProjectMapper.class);
            var affectedRows = mapper.deleteProjectById(id);
            return affectedRows > 0;
        } catch (PersistenceException ex) {
            logger.error("An error occurred mapping tables", ex);
            throw new Exception(ex);
        }
    }
}


