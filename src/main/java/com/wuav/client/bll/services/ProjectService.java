package com.wuav.client.bll.services;

import com.azure.storage.blob.BlobContainerClient;
import com.google.inject.Inject;
import com.wuav.client.be.Address;
import com.wuav.client.be.CustomImage;
import com.wuav.client.be.Project;
import com.wuav.client.bll.services.interfaces.IAddressService;
import com.wuav.client.bll.services.interfaces.IProjectService;
import com.wuav.client.dal.blob.BlobStorageFactory;
import com.wuav.client.dal.blob.BlobStorageHelper;
import com.wuav.client.dal.interfaces.IImageRepository;
import com.wuav.client.dal.interfaces.IProjectRepository;
import com.wuav.client.dal.repository.AddressRepository;
import com.wuav.client.dal.repository.ImageRepository;
import com.wuav.client.dal.repository.ProjectRepository;
import com.wuav.client.gui.dto.AddressDTO;
import com.wuav.client.gui.dto.CreateProjectDTO;

import java.io.File;
import java.util.List;

public class ProjectService implements IProjectService {

    private final IProjectRepository projectRepository;
    private final IAddressService addressService;
    private final IImageRepository imageRepository;

    @Inject
    public ProjectService(IProjectRepository projectRepository, IAddressService addressService, IImageRepository imageRepository){
        this.projectRepository = projectRepository;
        this.addressService = addressService;
        this.imageRepository = imageRepository;
    }

    public static void main(String[] args) {
        ProjectService projectService = new ProjectService(new ProjectRepository(), new AddressService(new AddressRepository()), new ImageRepository());

        projectService.createProject(10,null);
    }
    @Override
    public List<Project> getProjectByUserId(int userId) {
        return projectRepository.getAllProjectsByUserId(userId);
    }

    @Override
    public boolean createProject(int userId, CreateProjectDTO projectToCreate) {

        // create address and retreive the id
        // test
      //  AddressDTO addressDTO = new AddressDTO(100, "FROM SERVICE", "test", "test");

        int createdAddressResult = addressService.createAddress(projectToCreate.customer().address());

        if(createdAddressResult > 0) {
            System.out.println("Address created successfully");
            Address fetchedAddress = addressService.getAddressById(projectToCreate.customer().address().id());
            System.out.println("retrieved from db " + fetchedAddress);

            if(fetchedAddress != null){
                // create customer and retreive the id
            }
        }



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
