package com.wuav.client.gui.models;

import com.wuav.client.be.Project;
import com.wuav.client.gui.dto.CreateProjectDTO;

import java.util.List;

public interface IProjectModel {

 //   Project createProjectByName(int userId,String name);

    List<Project> getProjectsByUserId(int userId);

    void updateProjectsCache(int userId, List<Project> updatedProjects);

    boolean createProject(int userId, CreateProjectDTO projectToCreate);

    List<Project> getCachedProjectsByUserId(int userId);

    void updateCacheForUser(int userId, Project newProject);

    List<Project> getAllProjects();

    Project getProjectById(int projectId);
}
