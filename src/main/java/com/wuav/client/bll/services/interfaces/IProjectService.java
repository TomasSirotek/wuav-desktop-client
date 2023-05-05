package com.wuav.client.bll.services.interfaces;

import com.wuav.client.be.Project;
import com.wuav.client.gui.dto.CreateProjectDTO;

import java.util.List;

public interface IProjectService {

    List<Project> getProjectsByUserId(int userId);

    List<Project> getAllProjects();

    boolean createProject(int userId, CreateProjectDTO projectToCreate);

}
