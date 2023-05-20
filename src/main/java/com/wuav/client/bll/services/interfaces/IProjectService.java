package com.wuav.client.bll.services.interfaces;

import com.wuav.client.be.CustomImage;
import com.wuav.client.be.Customer;
import com.wuav.client.be.Project;
import com.wuav.client.gui.dto.CreateProjectDTO;
import com.wuav.client.gui.dto.PutCustomerDTO;
import com.wuav.client.gui.entities.DashboardData;

import java.io.File;
import java.util.List;
import java.util.Optional;

public interface IProjectService {

    List<Project> getProjectsByUserId(int userId) throws Exception;

    List<Project> getAllProjects() throws Exception;

    boolean createProject(int userId, CreateProjectDTO projectToCreate) throws Exception;

    Project getProjectById(int projectId) throws Exception;

    boolean deleteProject(Project id) throws Exception;

    Optional<CustomImage> reuploadImage(int projectId, int id, File selectedImageFile) throws Exception;

    String updateNotes(int id, String content) throws Exception;

    Customer updateCustomer(PutCustomerDTO customerDTO);

    String updateProjectName(int projectId, String newName) throws Exception;
}
