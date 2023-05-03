package com.wuav.client.gui.models;

import com.google.inject.Inject;
import com.wuav.client.be.Project;
import com.wuav.client.bll.services.interfaces.IProjectService;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.File;
import java.util.List;

public class ProjectModel implements IProjectModel{


    private IProjectService projectService;


    @Inject
    public ProjectModel(IProjectService projectService) {
        this.projectService = projectService;
    }

    @Override
    public Project createProjectByName(int userId,String name) {
        return projectService.createProjectByName(userId,name);
    }

    @Override
    public List<Project> getProjectByUserId(int userId) {
        return projectService.getProjectByUserId(userId);
    }

    @Override
    public boolean uploadImageWithDescription(int userId, int projectId, File file, String description, boolean isMainImage) {
        return projectService.uploadImageWithDescription(userId,projectId,file,description,isMainImage);
    }



}
