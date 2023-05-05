package com.wuav.client.gui.models;

import com.google.inject.Inject;
import com.wuav.client.be.Project;
import com.wuav.client.bll.services.interfaces.IProjectService;
import com.wuav.client.gui.dto.CreateProjectDTO;

import java.util.List;

public class ProjectModel implements IProjectModel{
    private IProjectService projectService;

    @Inject
    public ProjectModel(IProjectService projectService) {
        this.projectService = projectService;
    }

    @Override
    public List<Project> getProjectsByUserId(int userId) {
        return projectService.getProjectsByUserId(userId);
    }

    @Override
    public boolean createProject(int userId,CreateProjectDTO projectToCreate) {
       return projectService.createProject(userId,projectToCreate);
    }
}
