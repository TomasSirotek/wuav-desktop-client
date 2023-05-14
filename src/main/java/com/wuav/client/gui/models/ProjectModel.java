package com.wuav.client.gui.models;

import com.azure.storage.blob.BlobContainerClient;
import com.google.inject.Inject;
import com.wuav.client.be.CustomImage;
import com.wuav.client.be.Customer;
import com.wuav.client.be.Project;
import com.wuav.client.be.user.AppUser;
import com.wuav.client.bll.services.interfaces.IProjectService;
import com.wuav.client.cache.ImageCache;
import com.wuav.client.dal.blob.BlobStorageFactory;
import com.wuav.client.gui.dto.CreateProjectDTO;
import com.wuav.client.gui.dto.PutCustomerDTO;
import com.wuav.client.gui.models.user.IUserModel;
import javafx.scene.image.Image;

import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ProjectModel implements IProjectModel{
    private IProjectService projectService;

    private final IUserModel userModel;

    private final Map<Integer, List<Project>> projectsCache = Collections.synchronizedMap(new HashMap<>());
    private final int ALL_PROJECTS_KEY = -1;

    @Inject
    public ProjectModel(IProjectService projectService, IUserModel userModel) {
        this.projectService = projectService;
        this.userModel = userModel;
    }

    @Override
    public List<Project> getProjectsByUserId(int userId) {
        List<Project> projects = projectsCache.get(userId);

        if (projects == null) {
            projects = projectService.getProjectsByUserId(userId);
            // cacheProjectsImages(projects);  // remove for speed lulw
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

    @Override
    public boolean deleteProject(Project project) {
        AppUser user = userModel.getUserByProjectId(project.getId());
        boolean result = projectService.deleteProject(project);
        if (result) {
            // If the project is successfully deleted from the database, remove it from the cache
            removeProject(user.getId(), project.getId());
        }

        return result;

    }

    public void removeProject(int userId, int projectId) {
        List<Project> userProjects = projectsCache.get(userId);

        if (userProjects != null) {
            userProjects.removeIf(project -> project.getId() == projectId);
        }
    }


    @Override
    public Image reuploadImage(int projectId, int id, File selectedImageFile) {
        CustomImage updatedImage = projectService.reuploadImage(projectId,id, selectedImageFile);
        updatedImage.setMainImage(true);
        AppUser user = userModel.getUserByProjectId(projectId);
        Image image = null;

        if (updatedImage != null && user != null) {
            var projects = getProjectsByUserId(user.getId());

            // Replace the image in the projects list
            for (Project project : projects) {
                List<CustomImage> projectImages = project.getProjectImages();
                for (int i = 0; i < projectImages.size(); i++) {
                    if (projectImages.get(i).getId() == id) {
                        projectImages.set(i, updatedImage);
                        break;
                    }
                }
            }

            // Remove the old image from the cache
            ImageCache.removeImage(id);

            // Cache the project images
            cacheProjectsImages(projects);

            // Update the cache
            projectsCache.put(user.getId(), projects);

            // Retrieve the uploaded image
            image = ImageCache.getImage(updatedImage.getId());


            cacheProjectImages(getProjectById(projectId));
        }

        return image;
    }

    @Override
    public String updateNotes(int projectId, String content) {
        String updatedNotes = projectService.updateNotes(projectId,content);

        if(updatedNotes != null){
            // Retrieve the project owner
            AppUser user = userModel.getUserByProjectId(projectId);

            if (user != null) {
                // Fetch the updated project from the database
                Project updatedProject = projectService.getProjectById(projectId);

                // Update the cache with the updated project
                List<Project> projects = projectsCache.get(user.getId());
                for (int i = 0; i < projects.size(); i++) {
                    if (projects.get(i).getId() == projectId) {
                        projects.set(i, updatedProject);
                        break;
                    }
                }
                projectsCache.put(user.getId(), projects);
            }
        }

        return updatedNotes;
    }

    @Override
    public Customer updateCustomer(PutCustomerDTO customerDTO) {
        // update customer
        return projectService.updateCustomer(customerDTO);
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
