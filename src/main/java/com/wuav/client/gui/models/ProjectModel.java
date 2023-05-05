package com.wuav.client.gui.models;

import com.azure.storage.blob.BlobContainerClient;
import com.google.inject.Inject;
import com.wuav.client.be.CustomImage;
import com.wuav.client.be.Project;
import com.wuav.client.bll.services.interfaces.IProjectService;
import com.wuav.client.cache.ImageCache;
import com.wuav.client.dal.blob.BlobStorageFactory;
import com.wuav.client.gui.dto.CreateProjectDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectModel implements IProjectModel{
    private IProjectService projectService;

    private Map<Integer, List<Project>> projectsCache = new HashMap<>();

    private final int ALL_PROJECTS_KEY = -1;

    @Inject
    public ProjectModel(IProjectService projectService) {
        this.projectService = projectService;
    }

    @Override
    public List<Project> getProjectsByUserId(int userId) {
        List<Project> projects = projectsCache.get(userId);

        if (projects == null) {
            projects = projectService.getProjectsByUserId(userId);
          //  cacheProjectsImages(projects);
            projectsCache.put(userId, projects);
        }

        return projects;
    }

    @Override
    public List<Project> getAllProjects() {
        List<Project> projects = projectsCache.get(ALL_PROJECTS_KEY);

        if (projects == null) {
            projects = projectService.getAllProjects();
            projectsCache.put(ALL_PROJECTS_KEY, projects);
        }

        return projects;
    }


    private void cacheProjectsImages(List<Project> projects) {
        BlobContainerClient blobContainerClient = BlobStorageFactory.getBlobContainerClient();

        for (Project project : projects) {
            for (CustomImage image : project.getProjectImages()) {
                ImageCache.loadImage(blobContainerClient, image.getImageUrl(), image.getId());
            }
        }
    }

    @Override
    public boolean createProject(int userId,CreateProjectDTO projectToCreate) {
       return projectService.createProject(userId,projectToCreate);
    }


}
