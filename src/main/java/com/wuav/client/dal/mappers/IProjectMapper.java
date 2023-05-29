package com.wuav.client.dal.mappers;

import com.wuav.client.be.Project;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Interface for ProjectMapper
 */
public interface IProjectMapper {

    /**
     * Gets all projects by user id.
     *
     * @param userId the user id
     * @return the all projects by user id
     */
    List<Project> getAllProjectsByUserId(@Param("userId") int userId);

    /**
     * Gets all projects.
     *
     * @return the all projects
     */
    List<Project> getAllProjects();

    /**
     * Gets project by id.
     *
     * @param id the id
     * @return the project by id
     */
    Project getProjectById(@Param("id") int id);

    /**
     * Creates project.
     *
     * @param id          the id
     * @param name        the name
     * @param description the description
     * @param customerId  the customer id
     * @return the int if > 0 then success else fail
     */
    int createProject(@Param("id") int id, @Param("name") String name, @Param("description") String description, @Param("customerId") int customerId);

    /**
     * Adds user to project.
     *
     * @param userId    the user id
     * @param projectId the project id
     * @return the int if > 0 then success else fail
     */
    int addUserToProject(@Param("userId") int userId, @Param("projectId") int projectId);

    /**
     * Updates project for user by id.
     *
     * @param projectId   the project id
     * @param description the description
     * @return the int if > 0 then success else fail
     */
    int updateProjectForUserById(@Param("projectId") int projectId, @Param("description") String description);

    /**
     * Updates notes.
     *
     * @param projectId the project id
     * @param content   the content
     * @return the int if > 0 then success else fail
     */
    int updateNotes(@Param("projectId") int projectId, @Param("content") String content);

    /**
     * Deletes project by id.
     *
     * @param projectId the project id
     * @return the int if > 0 then success else fail
     */
    int deleteProjectById(@Param("projectId") int projectId);

    /**
     * Adds device to project.
     *
     * @param projectId the project id
     * @param deviceId  the device id
     * @return the int if > 0 then success else fail
     */
    int addDeviceToProject(@Param("projectId") int projectId, @Param("deviceId") int deviceId);

    /**
     * Updates project name
     *
     * @param projectId project id
     * @param newName   new name
     * @return the int if > 0 then success else fail
     */
    int updateProjectName(@Param("projectId") int projectId, @Param("name") String newName);
}
