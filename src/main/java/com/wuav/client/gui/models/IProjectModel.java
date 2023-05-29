package com.wuav.client.gui.models;

import com.wuav.client.be.Customer;
import com.wuav.client.be.Project;
import com.wuav.client.gui.dto.CreateProjectDTO;
import com.wuav.client.gui.dto.PutCustomerDTO;
import com.wuav.client.gui.entities.DashboardData;
import javafx.scene.image.Image;

import java.io.File;
import java.util.List;

/**
 * The interface Project model.
 */
public interface IProjectModel {


    /**
     * Gets projects by user id.
     *
     * @param userId the user id
     * @return the projects list by user id
     * @throws Exception
     */
    List<Project> getProjectsByUserId(int userId) throws Exception;


    /**
     * Create project.
     *
     * @param userId          the user id
     * @param projectToCreate the project to create
     * @return the boolean
     * @throws Exception
     */
    boolean createProject(int userId, CreateProjectDTO projectToCreate) throws Exception;


    /**
     * Gets all projects
     *
     * @return list of all projects
     * @throws Exception
     */
    List<Project> getAllProjects() throws Exception;

    /**
     * Gets project by id.
     *
     * @param projectId projects id
     * @return the project by id
     * @throws Exception
     */

    Project getProjectById(int projectId) throws Exception;


    /**
     * Delete project.
     *
     * @param project the project
     * @return the boolean
     * @throws Exception
     */
    boolean deleteProject(Project project) throws Exception;

    /**
     * Delete project.
     *
     * @param projectId
     * @param id
     * @param selectedImageFile
     * @return the boolean if the project was deleted
     * @throws Exception
     */
    Image reuploadImage(int projectId, int id, File selectedImageFile) throws Exception;


    /**
     * Updates projects nore
     *
     * @param id project id
     * @param s  new note
     * @return the string of updated content (note)
     * @throws Exception
     */
    String updateNotes(int id, String s) throws Exception;

    /**
     * Update customer customer.
     *
     * @param customerDTO the customer dto
     * @return the customer
     */
    Customer updateCustomer(PutCustomerDTO customerDTO);

    /**
     * Gets technician dashboard data.
     *
     * @param id the id
     * @return the technician dashboard data
     */
    DashboardData getTechnicianDashboardData(int id);

    /**
     * Gets admin dashboard data.
     *
     * @param id the id
     * @return the admin dashboard data
     */
    DashboardData getAdminDashboardData(int id);

    /**
     * Search project list.
     *
     * @param query the query
     * @return the list
     */
    List<Project> searchProject(String query);

    /**
     * Update project name string.
     *
     * @param id      the id
     * @param newName the new name
     * @return the string
     * @throws Exception the exception
     */
    String updateProjectName(int id, String newName) throws Exception;
}
