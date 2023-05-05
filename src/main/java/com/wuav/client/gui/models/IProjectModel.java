package com.wuav.client.gui.models;

import com.wuav.client.be.Project;
import com.wuav.client.gui.dto.CreateProjectDTO;

import java.util.List;

public interface IProjectModel {

 //   Project createProjectByName(int userId,String name);

    List<Project> getProjectsByUserId(int userId);

    boolean createProject(int userId,CreateProjectDTO projectToCreate);

    List<Project> getAllProjects();
}
