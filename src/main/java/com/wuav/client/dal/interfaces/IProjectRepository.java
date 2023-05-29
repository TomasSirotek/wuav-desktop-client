package com.wuav.client.dal.interfaces;


import com.wuav.client.be.Project;
import com.wuav.client.gui.dto.CreateProjectDTO;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * Interface for ProjectRepository
 */
public interface IProjectRepository {

    /**
     * Gets all projects by user id.
     *
     * @param userId the user id
     * @return the all projects by user id
     * @throws Exception the exception
     */
    List<Project> getAllProjectsByUserId(int userId) throws Exception;

    /**
     * Gets all projects.
     *
     * @return the all projects
     * @throws Exception the exception
     */
    List<Project> getAllProjects() throws Exception;

    /**
     * Gets project by id.
     *
     * @param projectId the project id
     * @return the project by id
     * @throws Exception the exception
     */

    Project getProjectById(int projectId) throws Exception;

    /**
     * Updates project.
     *
     * @param projectId   the project id
     * @param description the description
     * @return the project
     * @throws Exception the exception
     */

    Project updateProject(int projectId, String description) throws Exception;

    /**
     * Creates project.
     *
     * @param session    the session
     * @param projectDTO the project dto
     * @return the boolean
     * @throws Exception the exception
     */
    boolean createProject(SqlSession session, CreateProjectDTO projectDTO) throws Exception;

    /**
     * Adds project to user.
     *
     * @param session   the session
     * @param userId    the user id
     * @param projectId the project id
     * @return the int
     * @throws Exception the exception
     */
    int addProjectToUser(SqlSession session, int userId, int projectId) throws Exception;

    /**
     * Updates notes
     *
     * @param projectId the project id
     * @param content   the content
     * @return the boolean if the update was successful
     * @throws Exception the exception
     */
    boolean updateNotes(int projectId, String content) throws Exception;

    /**
     * Deletes project by id.
     *
     * @param session the session
     * @param id      the id
     * @return the boolean
     * @throws Exception the exception
     */
    boolean deleteProjectById(SqlSession session, int id) throws Exception;

    /**
     * Adds device to project.
     *
     * @param session   the session
     * @param projectId the project id
     * @param deviceId  the device id
     * @return the int
     * @throws Exception the exception
     */
    int addDeviceToProject(SqlSession session, int projectId, int deviceId) throws Exception;

    /**
     * Update project name
     *
     * @param projectId the project id
     * @param newName   the new name
     * @return the boolean if the update was successful
     * @throws Exception the exception
     */
    boolean updateProjectName(int projectId, String newName) throws Exception;
}
