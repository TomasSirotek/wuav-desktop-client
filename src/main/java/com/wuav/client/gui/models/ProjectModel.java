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
import com.wuav.client.gui.entities.DashboardData;
import com.wuav.client.gui.models.user.IUserModel;
import javafx.scene.image.Image;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

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
    public List<Project> getAllProjects() throws Exception {
        List<Project> projects = projectsCache.get(ALL_PROJECTS_KEY);

        if (projects == null) {
            projects = projectService.getAllProjects();
            cacheProjectsImages(projects);
            projectsCache.put(ALL_PROJECTS_KEY, projects);
        }

        return projects;
    }
    @Override
    public List<Project> getProjectsByUserId(int userId) throws Exception {
        List<Project> projects = projectsCache.get(userId);

        if (projects == null) {
            projects = projectService.getProjectsByUserId(userId);
            cacheProjectsImages(projects);
            projectsCache.put(userId, projects);
        }
        return projects;
    }

    @Override
    public Project getProjectById(int projectId) throws Exception {return projectService.getProjectById(projectId);}

    @Override
    public List<Project> getCachedProjectsByUserId(int userId) {
        return projectsCache.get(userId);
    }


    @Override
    public DashboardData getTechnicianDashboardData(int id) {
        List<Project> technicianProjects = projectsCache.getOrDefault(id, new ArrayList<>());

        int totalProjects = technicianProjects.size();

        int totalDeviceUser = technicianProjects.stream()
                .mapToInt(project -> project.getDevices().size()).sum();

        // amount of non-main images uploaded
        int amountOfPlansUploaded = technicianProjects.stream()
                .mapToInt(project -> (int) project.getProjectImages().stream().filter(customImage -> !customImage.isMainImage()).count())
                .sum();

        List<Customer> recentCustomers = technicianProjects.stream()
                .filter(project -> project.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isEqual(LocalDate.now()))
                .map(Project::getCustomer)
                .limit(4)
                .collect(Collectors.toList());

        return new DashboardData(
                totalProjects,
                totalDeviceUser,
                recentCustomers,
                amountOfPlansUploaded
        );
    }
    @Override
    public DashboardData getAdminDashboardData(int id) {
        int totalProjects = projectsCache.values().stream()
                .mapToInt(List::size)
                .sum();
        int totalDeviceUser = projectsCache.values().stream()
                .flatMap(Collection::stream)
                .mapToInt(project -> project.getDevices().size()).sum();

        // amount of non-main images uploaded
        int amountOfPlansUploaded = projectsCache.values().stream()
                .flatMap(Collection::stream)
                .mapToInt(project -> (int) project.getProjectImages().stream().filter(customImage -> !customImage.isMainImage()).count())
                .sum();

        List<Customer> recentCustomers = projectsCache.values().stream()
                .flatMap(Collection::stream)
                .filter(project -> project.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isEqual(LocalDate.now()))
                .map(Project::getCustomer)
                .limit(4)
                .collect(Collectors.toList());


        return new DashboardData(
                totalProjects,
                totalDeviceUser,
                recentCustomers,
                amountOfPlansUploaded
        );
    }

    @Override
    public List<Project> searchProject(String query) {
        return projectsCache.values().stream()
                .flatMap(Collection::stream)
                .filter(project -> project.getName().toLowerCase().contains(query.toLowerCase())
                        || project.getDescription().toLowerCase().contains(query.toLowerCase())
                        || String.valueOf(project.getId()).equals(query))
                .collect(Collectors.toList());
    }

    @Override
    public String updateProjectName(int projectId, String newName) throws Exception {
        String updatedName = projectService.updateProjectName(projectId,newName);

        if(updatedName.isEmpty()){
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

        return updatedName;
    }

    @Override
    public boolean createProject(int userId,CreateProjectDTO projectToCreate) throws Exception {
        boolean result =  projectService.createProject(userId,projectToCreate);
        if (result) {
            // If the project is successfully created, invalidate the cache entries
            projectsCache.put(userId, projectService.getProjectsByUserId(userId)); // update cache for specific user
            projectsCache.put(ALL_PROJECTS_KEY, projectService.getAllProjects()); // update cache for all projects
            cacheProjectImages(projectService.getProjectById(projectToCreate.id()));
        }
        return result;
    }

    @Override
    public Customer updateCustomer(PutCustomerDTO customerDTO) {
        return projectService.updateCustomer(customerDTO);
    }

    @Override
    public void updateCacheForUser(int userId, Project newProject) throws Exception {
        List<Project> projects = projectsCache.get(userId);
        if (projects != null) {
            projects.add(newProject);
        } else {
            projects = new ArrayList<>();
            projects.add(newProject);
            projectsCache.put(userId, projects);
        }
        cacheProjectImages(newProject);
    }

    @Override
    public void updateProjectsCache(int userId, List<Project> updatedProjects) {
        projectsCache.put(userId, updatedProjects);
    }

    @Override
    public boolean deleteProject(Project project) throws Exception {
        AppUser user = userModel.getUserByProjectId(project.getId());
        boolean result = projectService.deleteProject(project);
        if (result) {
            // If the project is successfully deleted, invalidate the cache entries
            projectsCache.remove(user.getId()); // remove projects for specific user
            projectsCache.remove(ALL_PROJECTS_KEY); // remove all projects
        }
        return result;
    }

    private void removeProject(int userId, int projectId) {
        List<Project> userProjects = projectsCache.get(userId);
        if (userProjects != null)  userProjects.removeIf(project -> project.getId() == projectId);
    }


    @Override
    public Image reuploadImage(int projectId, int id, File selectedImageFile) throws Exception {
        Optional<CustomImage> updatedImage = projectService.reuploadImage(projectId,id, selectedImageFile);
        updatedImage.get().setMainImage(true);
        AppUser user = userModel.getUserByProjectId(projectId);
        Image image = null;

        if (updatedImage != null && user != null) {
            var projects = projectService.getProjectsByUserId(user.getId());

            // Replace the image in the projects list
            for (Project project : projects) {
                List<CustomImage> projectImages = project.getProjectImages();
                for (int i = 0; i < projectImages.size(); i++) {
                    if (projectImages.get(i).getId() == id) {
                        projectImages.set(i, updatedImage.get());
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
            image = ImageCache.getImage(updatedImage.get().getId());


            cacheProjectImages(getProjectById(projectId));
        }

        return image;
    }


    @Override
    public String updateNotes(int projectId, String content) throws Exception {
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

    private void cacheProjectsImages(List<Project> projects) {
        BlobContainerClient blobContainerClient = BlobStorageFactory.getBlobContainerClient();

        int numberOfThreads = 12; // for now just for I/O putting a bit more threads
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        try {
            projects.stream()
                    .flatMap(project -> project.getProjectImages().stream())
                    .map(image -> executorService.submit(() -> {
                        ImageCache.loadImage(blobContainerClient, image.getImageUrl(), image.getId());
                        return null;
                    }))
                    .forEach(future -> {
                        try {
                            future.get();
                        } catch (InterruptedException | ExecutionException e) {
                            Thread.currentThread().interrupt();
                            e.printStackTrace();
                        }
                    });
        } finally {
            executorService.shutdown();
        }
    }

    private void cacheProjectImages(Project project) throws Exception {
        BlobContainerClient blobContainerClient = BlobStorageFactory.getBlobContainerClient();

        for (CustomImage image : project.getProjectImages()) {
            ImageCache.loadImage(blobContainerClient, image.getImageUrl(), image.getId());
        }
    }
}
