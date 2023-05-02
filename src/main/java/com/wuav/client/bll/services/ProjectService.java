package com.wuav.client.bll.services;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.google.inject.Inject;
import com.wuav.client.be.Project;
import com.wuav.client.be.user.AppUser;
import com.wuav.client.bll.services.interfaces.IProjectService;
import com.wuav.client.bll.utilities.UniqueIdGenerator;
import com.wuav.client.dal.blob.BlobStorageFactory;
import com.wuav.client.dal.blob.BlobStorageHelper;
import com.wuav.client.dal.interfaces.IProjectRepository;
import javafx.fxml.FXML;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public class ProjectService implements IProjectService {

    private IProjectRepository projectRepository;

    private final BlobContainerClient blobContainerClient;

    private BlobStorageHelper blobStorageHelper;

    @Inject
    public ProjectService(IProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
        this.blobContainerClient = BlobStorageFactory.getBlobContainerClient();
        blobStorageHelper=  new BlobStorageHelper(blobContainerClient);
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

    @Override
    public boolean uploadImageWithDescription(int userId, int projectId, File file, String description, boolean isMainImage) {
       // upload the image to the blob storage
        String blobUrl = blobStorageHelper.uploadImageToBlobStorage(file);

        if(blobUrl == null || blobUrl.isEmpty() || blobUrl.isBlank()){
            return false;
        }
        Project project = projectRepository.updateProjectWithImage(userId,projectId,blobUrl,description,isMainImage);

        if(project == null){
            return false;
        }
        return true;
    }


}
