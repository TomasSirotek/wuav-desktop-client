package com.wuav.client.bll.services.interfaces;

import com.wuav.client.be.CustomImage;
import com.wuav.client.be.Customer;
import com.wuav.client.be.Project;
import com.wuav.client.gui.dto.CreateProjectDTO;
import com.wuav.client.gui.dto.PutCustomerDTO;

import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * The interface for the project service
 */
public interface IProjectService {

    /**
     * Gets all projects of the given user
     *
     * @param userId
     * @return
     * @throws Exception
     */
    List<Project> getProjectsByUserId(int userId) throws Exception;

    /**
     * Gets all projects
     *
     * @return a list of projects
     * @throws Exception
     */
    List<Project> getAllProjects() throws Exception;

    /**
     * Creates a project
     *
     * @param userId          the id of the user to create the project for
     * @param projectToCreate the project to create
     * @return boolean if the project was created
     * @throws Exception
     */
    boolean createProject(int userId, CreateProjectDTO projectToCreate) throws Exception;

    /**
     * Gets the project with the given id
     *
     * @param projectId the id of the project
     * @return the project
     * @throws Exception
     */
    Project getProjectById(int projectId) throws Exception;

    /**
     * Deletes the project with the given id
     *
     * @param id the id of the project
     * @return boolean if the project was deleted
     * @throws Exception
     */
    boolean deleteProject(Project id) throws Exception;

    /**
     * Reuploads the image of the project with the given id
     *
     * @param projectId         the id of the project
     * @param id                the id of the image
     * @param selectedImageFile the image to upload
     * @return the image
     * @throws Exception
     */
    Optional<CustomImage> reuploadImage(int projectId, int id, File selectedImageFile) throws Exception;

    /**
     * Updates the notes of the project with the given id
     *
     * @param id      the id of the project
     * @param content the new notes
     * @return the notes as String or empty String if the notes are null
     * @throws Exception
     */
    String updateNotes(int id, String content) throws Exception;

    /**
     * Updates the customer of the project with the given PutCustomerDTO
     *
     * @param customerDTO the customer to update
     * @return the customer
     */
    Customer updateCustomer(PutCustomerDTO customerDTO);

    /**
     * Updates the name of the project with the given id
     *
     * @param projectId the id of the project
     * @param newName   the new name
     * @return the name
     * @throws Exception
     */
    String updateProjectName(int projectId, String newName) throws Exception;
}
