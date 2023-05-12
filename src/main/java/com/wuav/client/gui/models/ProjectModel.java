package com.wuav.client.gui.models;

import com.azure.storage.blob.BlobContainerClient;
import com.google.inject.Inject;
import com.wuav.client.be.CustomImage;
import com.wuav.client.be.Project;
import com.wuav.client.bll.services.interfaces.IProjectService;
import com.wuav.client.cache.ImageCache;
import com.wuav.client.dal.blob.BlobStorageFactory;
import com.wuav.client.gui.dto.CreateProjectDTO;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ProjectModel implements IProjectModel{
    private IProjectService projectService;

    private final Map<Integer, List<Project>> projectsCache = Collections.synchronizedMap(new HashMap<>());
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
           // cacheProjectsImages(projects); // MOVE BACK
            projectsCache.put(userId, projects);
        }

        return projects;
    }

    @Override
    public List<Project> getCachedProjectsByUserId(int userId) {
        return projectsCache.get(userId);
    }

    @Override
    public void updateCacheForUser(int userId, Project newProject) {
        List<Project> projects = projectsCache.get(userId);
        if (projects != null) {
            projects.add(newProject);
        } else {
            projects = new ArrayList<>();
            projects.add(newProject);
            projectsCache.put(userId, projects);
        }
      //  cacheProjectImages(newProject);
    }

    @Override
    public void updateProjectsCache(int userId, List<Project> updatedProjects) {
        projectsCache.put(userId, updatedProjects);
    }

    @Override
    public List<Project> getAllProjects() {
        List<Project> projects = projectsCache.get(ALL_PROJECTS_KEY);

        if (projects == null) {
            projects = projectService.getAllProjects();
          //  cacheProjectsImages(projects);
            projectsCache.put(ALL_PROJECTS_KEY, projects);
        }

        return projects;
    }

    @Override
    public Project getProjectById(int projectId) {
        return projectService.getProjectById(projectId);
    }

    private void cacheProjectsImages(List<Project> projects) {
        BlobContainerClient blobContainerClient = BlobStorageFactory.getBlobContainerClient();

        // Create an ExecutorService with a fixed thread pool size
        int threadPoolSize = 16;
        ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);

        // Create a list to store Future objects for each task
        List<Future<Void>> futures = new ArrayList<>();

        for (Project project : projects) {
            for (CustomImage image : project.getProjectImages()) {
                // Submit the loadImage task to the ExecutorService
                Future<Void> future = executorService.submit(() -> {
                    ImageCache.loadImage(blobContainerClient, image.getImageUrl(), image.getId());
                    return null;
                });
                // Add the future to the list
                futures.add(future);
            }
        }

        // Wait for all tasks to complete
        for (Future<Void> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        // Shutdown the ExecutorService
        executorService.shutdown();
    }

    private void cacheProjectImages(Project project) {
        BlobContainerClient blobContainerClient = BlobStorageFactory.getBlobContainerClient();

        for (CustomImage image : project.getProjectImages()) {
            ImageCache.loadImage(blobContainerClient, image.getImageUrl(), image.getId());
        }
    }

    @Override
    public boolean createProject(int userId,CreateProjectDTO projectToCreate) {
       return projectService.createProject(userId,projectToCreate);
    }


}
