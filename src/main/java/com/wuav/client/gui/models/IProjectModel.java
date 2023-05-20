package com.wuav.client.gui.models;

import com.wuav.client.be.Customer;
import com.wuav.client.be.Project;
import com.wuav.client.gui.dto.CreateProjectDTO;
import com.wuav.client.gui.dto.PutCustomerDTO;
import com.wuav.client.gui.entities.DashboardData;
import javafx.scene.image.Image;

import java.io.File;
import java.util.List;

public interface IProjectModel {

 //   Project createProjectByName(int userId,String name);

    List<Project> getProjectsByUserId(int userId) throws Exception;

    void updateProjectsCache(int userId, List<Project> updatedProjects);

    boolean createProject(int userId, CreateProjectDTO projectToCreate) throws Exception;

    List<Project> getCachedProjectsByUserId(int userId);

    void updateCacheForUser(int userId, Project newProject) throws Exception;

    List<Project> getAllProjects() throws Exception;

    Project getProjectById(int projectId) throws Exception;

    boolean deleteProject(Project project) throws Exception;

    Image reuploadImage(int projectId, int id, File selectedImageFile) throws Exception;


    String updateNotes(int id, String s) throws Exception;

    Customer updateCustomer(PutCustomerDTO customerDTO);

    DashboardData getTechnicianDashboardData(int id);

    DashboardData getAdminDashboardData(int id);

    List<Project> searchProject(String query);

    String updateProjectName(int id, String newName) throws Exception;
}
