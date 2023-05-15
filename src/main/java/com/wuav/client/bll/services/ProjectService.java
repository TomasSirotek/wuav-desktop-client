package com.wuav.client.bll.services;

import com.azure.storage.blob.BlobContainerClient;
import com.google.inject.Inject;
import com.wuav.client.be.CustomImage;
import com.wuav.client.be.Customer;
import com.wuav.client.be.Project;
import com.wuav.client.be.device.Device;
import com.wuav.client.bll.services.interfaces.IAddressService;
import com.wuav.client.bll.services.interfaces.ICustomerService;
import com.wuav.client.bll.services.interfaces.IProjectService;
import com.wuav.client.dal.blob.BlobStorageFactory;
import com.wuav.client.dal.blob.BlobStorageHelper;
import com.wuav.client.dal.interfaces.IDeviceRepository;
import com.wuav.client.dal.interfaces.IImageRepository;
import com.wuav.client.dal.interfaces.IProjectRepository;
import com.wuav.client.gui.dto.CreateProjectDTO;
import com.wuav.client.gui.dto.ImageDTO;
import com.wuav.client.gui.dto.PutCustomerDTO;

import java.io.File;
import java.util.List;

public class ProjectService implements IProjectService {

    private final IProjectRepository projectRepository;
    private final IAddressService addressService;

    private final ICustomerService customerService;

    private final IImageRepository imageRepository;

    private final IDeviceRepository deviceRepository;


    @Inject
    public ProjectService(IProjectRepository projectRepository, IAddressService addressService, IImageRepository imageRepository, ICustomerService customerService, IDeviceRepository deviceRepository) {
        this.projectRepository = projectRepository;
        this.addressService = addressService;
        this.imageRepository = imageRepository;
        this.customerService = customerService;
        this.deviceRepository = deviceRepository;
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

    @Override
    public Project getProjectById(int projectId) {
        return projectRepository.getProjectById(projectId);
    }

    @Override
    public boolean deleteProject(Project project) {
        // Ensure all operations are atomic to maintain data integrity
        try {

            // 1. Delete all images from blob storage
            for (CustomImage image : project.getProjectImages()) {
                String imageUrl = image.getImageUrl();
                boolean isDeleted = deleteIfExists(imageUrl);
                if (!isDeleted) {
                   throw new RuntimeException("Failed to delete image from blob storage: " + imageUrl);
               }
            }

            // 2. Delete all images from database (including from the join table due to cascade delete)
            for (CustomImage image : project.getProjectImages()) {
                boolean imageDeleted = imageRepository.deleteImageById(image.getId());
                if (!imageDeleted) {
                    throw new RuntimeException("Failed to delete image from database: " + image.getId());
                }
            }

            // 4. Delete project from database
            boolean projectDeleted = projectRepository.deleteProjectById(project.getId());
            if (!projectDeleted) {
                throw new RuntimeException("Failed to delete project from database: " + project.getId());
            }


            // 3. Delete customer from database (assuming a customer is linked to a project)
            boolean customerDeleted = customerService.deleteCustomerById(project.getCustomer().getId());
            if (!customerDeleted) {
                throw new RuntimeException("Failed to delete customer from database for project: " + project.getId());
            }


            // 5. Delete address from database (assuming an address is linked to a project)
            boolean addressDeleted = addressService.deleteAddressById(project.getCustomer().getAddress().getId());
            if (!addressDeleted) {
                throw new RuntimeException("Failed to delete address from database for project: " + project.getId());
            }


            // If all steps are successful, return true
            return true;
        } catch (Exception e) {
            // Log error and return false
            System.err.println(e.getMessage());
            return false;
        }
    }

    @Override
    public CustomImage reuploadImage(int projectId,int imageId, File selectedImageFile) {
        // 1. retrieve the main image
        CustomImage mainImage = imageRepository.getImageById(imageId); // this retrieves the main image

        // 2. delete blob from the container by image URL (main image)
        boolean deletedBlob = deleteIfExists(mainImage.getImageUrl());

        if(deletedBlob){
            // 3. Upload the new image
            CustomImage newCustomImage = uploadImage(projectId, selectedImageFile);

            if(newCustomImage != null){
                // 4. Update the image information in the image table
                boolean updatedImage = imageRepository.updateImage(
                        mainImage.getId(),
                        newCustomImage.getImageType(),
                        newCustomImage.getImageUrl()
                );

                if(updatedImage){
                    return imageRepository.getImageById(mainImage.getId());
                }
            }
        }
        return null;
    }

    @Override
    public String updateNotes(int projectId, String content) {
        // try to update the notes for the project and return
        var result = projectRepository.updateNotes(projectId, content);
        if(result){
            return getProjectById(projectId).getDescription();
        }
        return "";
    }

    @Override
    public Customer updateCustomer(PutCustomerDTO customerDTO) {
        // get the customer from the database
       boolean result = addressService.updateAddress(customerDTO.addressDTO());
       if(result){
           boolean projectCustomer = customerService.updateCustomer(customerDTO);
           if(projectCustomer){
                return customerService.getCustomerById(customerDTO.id());
           }
       }
        return null;
    }


    // THIS HAS TO BE REFACTORED AND INCLUDE ROLL BACKS
    private boolean tryCreateProject(int userId, CreateProjectDTO projectToCreate) throws Exception {
        // Create address and retrieve the id
        int createdAddressResult = addressService.createAddress(projectToCreate.customer().address());
        if (createdAddressResult <= 0) {
            throw new Exception("Failed to create address");
        }

        // Create customer and retrieve the id
        int createdCustomerResult = customerService.createCustomer(projectToCreate.customer());
        if (createdCustomerResult <= 0) {
            throw new Exception("Failed to create customer");
        }

        // Create project and retrieve the id
        int createdProjectResult = projectRepository.createProject(projectToCreate);
        if (createdProjectResult <= 0) {
            throw new Exception("Failed to create project");
        }

        // Add project to user
        int isProjectAddedToUser = projectRepository.addProjectToUser(userId, projectToCreate.id());
        if (isProjectAddedToUser <= 0) {
            throw new Exception("Failed to add project to user");
        }

        // add devices to project
        for(Device device : projectToCreate.selectedDevices()){
            int isDeviceAddedToProject = deviceRepository.addDeviceToProject(projectToCreate.id(), device.getId());
            if (isDeviceAddedToProject <= 0) {
                throw new Exception("Failed to add device to project");
            }
        }

        // Create image in Azure Blob Storage and database, then add it to the project
        // ...
        for (ImageDTO imageDTO : projectToCreate.images()) {
            // Upload image to Azure Blob Storage
            CustomImage customImage = uploadImage(projectToCreate.id(), imageDTO.getFile());

            // Save image information in the image table
            CustomImage createdImageResult = imageRepository.createImage(
                    customImage.getId(),
                    customImage.getImageType(),
                    customImage.getImageUrl()
            );
            if (createdImageResult == null) {
                throw new Exception("Failed to save image to the image table");
            }

            // Add image to the project_image table
            boolean isImageAddedToProject = imageRepository.addImageToProject(projectToCreate.id(), createdImageResult.getId(),imageDTO.isMain());
            if (!isImageAddedToProject) {
                throw new Exception("Failed to add image to the project_image table");
            }
        }

        return true;
    }

    private CustomImage uploadImage(int projectId, File file) {
        BlobContainerClient blobContainerClient = BlobStorageFactory.getBlobContainerClient();
        BlobStorageHelper blobStorageHelper = new BlobStorageHelper(blobContainerClient);

        return blobStorageHelper.uploadImageToBlobStorage(projectId,file);
    }

    private boolean deleteIfExists(String imageUrl) {
        BlobContainerClient blobContainerClient = BlobStorageFactory.getBlobContainerClient();
        BlobStorageHelper blobStorageHelper = new BlobStorageHelper(blobContainerClient);

        return blobStorageHelper.deleteImageIfExist(imageUrl);
    }




}
