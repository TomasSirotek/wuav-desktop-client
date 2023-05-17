package com.wuav.client.bll.services.interfaces;

import com.wuav.client.be.CustomImage;
import com.wuav.client.be.Customer;
import com.wuav.client.be.Project;
import com.wuav.client.bll.exeption.ProjectException;
import com.wuav.client.gui.dto.CreateProjectDTO;
import com.wuav.client.gui.dto.PutCustomerDTO;
import com.wuav.client.gui.entities.DashboardData;

import java.io.File;
import java.util.List;

public interface IProjectService {

    List<Project> getProjectsByUserId(int userId);

    List<Project> getAllProjects() throws Exception;

    boolean createProject(int userId, CreateProjectDTO projectToCreate);

    Project getProjectById(int projectId);

    boolean deleteProject(Project id);

    CustomImage reuploadImage(int projectId, int id, File selectedImageFile);

    String updateNotes(int id, String content);

    Customer updateCustomer(PutCustomerDTO customerDTO);

    DashboardData getDashboardData(int userId);
}
