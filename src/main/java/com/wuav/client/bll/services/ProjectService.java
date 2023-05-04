package com.wuav.client.bll.services;

import com.azure.storage.blob.BlobContainerClient;
import com.google.inject.Inject;
import com.wuav.client.be.CustomImage;
import com.wuav.client.be.Project;
import com.wuav.client.bll.services.interfaces.IProjectService;
import com.wuav.client.bll.utilities.UniqueIdGenerator;
import com.wuav.client.dal.blob.BlobStorageFactory;
import com.wuav.client.dal.blob.BlobStorageHelper;
import com.wuav.client.dal.interfaces.IImageRepository;
import com.wuav.client.dal.interfaces.IProjectRepository;
import com.wuav.client.gui.dto.CreateProjectDTO;

import java.io.File;
import java.util.List;

public class ProjectService implements IProjectService {

    private final IProjectRepository projectRepository;

    private final IImageRepository imageRepository;

    @Inject
    public ProjectService(IProjectRepository projectRepository, IImageRepository imageRepository){
        this.projectRepository = projectRepository;
        this.imageRepository = imageRepository;
    }

//    @Override
//    public Project createProjectByName(int userId,String name) {
//        // generate new int UUID for project
//        int id = UniqueIdGenerator.generateUniqueId();
//        String status = "ACTIVE";
//        return projectRepository.createProjectByName(userId,id,name,status);
//    }

    @Override
    public List<Project> getProjectByUserId(int userId) {
        return projectRepository.getAllProjectsByUserId(userId);
    }

    @Override
    public boolean createProject(int userId, CreateProjectDTO projectToCreate) {

        // create address and retreive the id



        // create customer and retreive the id

        // create project and retreive the id









       //  CustomImage customImage = uploadImage(file);
//        if (customImage == null) {
//            return false;
//        }
//
//        CustomImage imageInserted = insertImage(customImage);
//
//        if (imageInserted == null) {
//            return false;
//        }
//
//        if (!addImageToProject(projectId, customImage.getId(), isMainImage)) {
//            return false;
//        }
//
//        return updateProject(projectId, description) != null;

        return false;
    }

    private CustomImage uploadImage(File file) {
        BlobContainerClient blobContainerClient =  BlobStorageFactory.getBlobContainerClient();
         BlobStorageHelper blobStorageHelper = new BlobStorageHelper(blobContainerClient);

        return blobStorageHelper.uploadImageToBlobStorage(file);
    }

    private CustomImage insertImage(CustomImage customImage) {
        return imageRepository.createImage(customImage.getId(), customImage.getImageType(), customImage.getImageUrl());
    }

    private boolean addImageToProject(int projectId, int imageId, boolean isMainImage) {
        return imageRepository.addImageToProject(projectId, imageId, isMainImage);
    }

    private Project updateProject(int projectId, String description) {
        return projectRepository.updateProject(projectId, description);
    }



}
