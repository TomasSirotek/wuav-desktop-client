package com.wuav.client.bll.services;

import com.google.inject.Inject;
import com.wuav.client.be.Project;
import com.wuav.client.bll.services.interfaces.IProjectService;
import com.wuav.client.bll.utilities.UniqueIdGenerator;
import com.wuav.client.dal.interfaces.IProjectRepository;
import javafx.fxml.FXML;

import java.util.List;

public class ProjectService implements IProjectService {

    private IProjectRepository projectRepository;


    @Inject
    public ProjectService(IProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Project createProjectByName(int userId,String name) {
        // generate new int UUID for project
        int id = UniqueIdGenerator.generateUniqueId();
        String status = "ACTIVE";
        return projectRepository.createProjectByName(userId,id,name,status);
    }

    @Override
    public List<Project> getProjectByUserId(int userId) {
        return projectRepository.getAllProjectsByUserId(userId);
    }
}
