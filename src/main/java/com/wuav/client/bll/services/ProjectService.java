package com.wuav.client.bll.services;

import com.azure.storage.blob.BlobContainerClient;
import com.google.inject.Inject;
import com.wuav.client.be.Address;
import com.wuav.client.be.CustomImage;
import com.wuav.client.be.Customer;
import com.wuav.client.be.Project;
import com.wuav.client.bll.services.interfaces.IAddressService;
import com.wuav.client.bll.services.interfaces.ICustomerService;
import com.wuav.client.bll.services.interfaces.IProjectService;
import com.wuav.client.dal.blob.BlobStorageFactory;
import com.wuav.client.dal.blob.BlobStorageHelper;
import com.wuav.client.dal.interfaces.IImageRepository;
import com.wuav.client.dal.interfaces.IProjectRepository;
import com.wuav.client.dal.repository.AddressRepository;
import com.wuav.client.dal.repository.CustomerRepository;
import com.wuav.client.dal.repository.ImageRepository;
import com.wuav.client.dal.repository.ProjectRepository;
import com.wuav.client.gui.dto.AddressDTO;
import com.wuav.client.gui.dto.CreateProjectDTO;
import com.wuav.client.gui.dto.CustomerDTO;
import com.wuav.client.gui.dto.ImageDTO;

import java.io.File;
import java.util.List;

public class ProjectService implements IProjectService {

    private final IProjectRepository projectRepository;
    private final IAddressService addressService;

    private final ICustomerService customerService;

    private final IImageRepository imageRepository;


    @Inject
    public ProjectService(IProjectRepository projectRepository, IAddressService addressService, IImageRepository imageRepository,ICustomerService customerService) {
        this.projectRepository = projectRepository;
        this.addressService = addressService;
        this.imageRepository = imageRepository;
        this.customerService = customerService;
    }


    @Override
    public List<Project> getProjectsByUserId(int userId) {
        return projectRepository.getAllProjectsByUserId(userId);
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.getAllProjects();
    }


    @Override
    public boolean createProject(int userId, CreateProjectDTO projectToCreate) {
        try {
            return tryCreateProject(userId, projectToCreate);
        } catch (Exception e) {
            System.err.println("Error creating project: " + e.getMessage());
            return false;
        }
    }


    // THIS HAS TO BE REFACTORED AND INCLUDE ROLL BACKS
    private boolean tryCreateProject(int userId, CreateProjectDTO projectToCreate) throws Exception {
        // Create address and retrieve the id
        int createdAddressResult = addressService.createAddress(projectToCreate.customer().address());
        if (createdAddressResult <= 0) {
            throw new Exception("Failed to create address");
        }
        System.out.println("Address created successfully");

        // Create customer and retrieve the id
        int createdCustomerResult = customerService.createCustomer(projectToCreate.customer());
        if (createdCustomerResult <= 0) {
            throw new Exception("Failed to create customer");
        }
        System.out.println("Customer created successfully");

        // Create project and retrieve the id
        int createdProjectResult = projectRepository.createProject(projectToCreate);
        if (createdProjectResult <= 0) {
            throw new Exception("Failed to create project");
        }
        System.out.println("Project created successfully");

        // Add project to user
        int isProjectAddedToUser = projectRepository.addProjectToUser(userId, projectToCreate.id());
        if (isProjectAddedToUser <= 0) {
            throw new Exception("Failed to add project to user");
        }
        System.out.println("Project added to user successfully");

        // Create image in Azure Blob Storage and database, then add it to the project
        // ...
        for (ImageDTO imageDTO : projectToCreate.images()) {
            // Upload image to Azure Blob Storage
            CustomImage customImage = uploadImage(imageDTO.getFile());
            System.out.println("Image uploaded to Blob Storage successfully");

            // Save image information in the image table
            CustomImage createdImageResult = imageRepository.createImage(
                    customImage.getId(),
                    customImage.getImageType(),
                    customImage.getImageUrl()
            );
            if (createdImageResult == null) {
                throw new Exception("Failed to save image to the image table");
            }
            System.out.println("Image saved to the image table successfully");

            // Add image to the project_image table
            boolean isImageAddedToProject = imageRepository.addImageToProject(projectToCreate.id(), createdImageResult.getId(),imageDTO.isMain());
            if (!isImageAddedToProject) {
                throw new Exception("Failed to add image to the project_image table");
            }
            System.out.println("Image added to the project_image table successfully");
        }

        return true;
    }

    private CustomImage uploadImage(File file) {
        BlobContainerClient blobContainerClient = BlobStorageFactory.getBlobContainerClient();
        BlobStorageHelper blobStorageHelper = new BlobStorageHelper(blobContainerClient);

        return blobStorageHelper.uploadImageToBlobStorage(file);
    }



}
