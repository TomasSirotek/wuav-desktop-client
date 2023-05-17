package com.wuav.client.bll.services;

import com.azure.storage.blob.BlobContainerClient;
import com.google.inject.Inject;
import com.wuav.client.be.CustomImage;
import com.wuav.client.be.Customer;
import com.wuav.client.be.Project;
import com.wuav.client.be.device.Device;
import com.wuav.client.bll.exeption.ProjectException;
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
import com.wuav.client.gui.entities.DashboardData;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    public List<Project> getAllProjects() throws Exception {
        return projectRepository.getAllProjects();
    }

    @Override
    public List<Project> getProjectsByUserId(int userId) throws Exception {
        return projectRepository.getAllProjectsByUserId(userId);
    }
    @Override
    public Project getProjectById(int projectId) throws Exception {
        return projectRepository.getProjectById(projectId);
    }
    @Override
    public boolean createProject(int userId, CreateProjectDTO projectToCreate) throws Exception {
        return tryCreateProject(userId, projectToCreate);
    }


    @Override
    public Optional<CustomImage> reuploadImage(int projectId,int imageId, File selectedImageFile) throws Exception {
            return Optional.ofNullable(imageRepository.getImageById(imageId))
                    .filter(image -> deleteIfExists(image.getImageUrl()))
                    .map(image -> uploadImage(projectId, selectedImageFile))
                    .filter(Objects::nonNull)
                    .filter(newImage -> imageRepository.updateImage(
                            imageId,
                            newImage.getImageType(),
                            newImage.getImageUrl()))
                    .flatMap(updated -> {
                        try {
                            return Optional.ofNullable(imageRepository.getImageById(imageId));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
    }

    @Override
    public String updateNotes(int projectId, String content) throws Exception {
        boolean result = projectRepository.updateNotes(projectId, content);
        if(!result) return "";
        return getProjectById(projectId).getDescription();
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

    @Override
    public DashboardData getDashboardData(int userId) {

        // here construct dashboard data
        return null;
    }


    // THIS HAS TO BE REFACTORED AND INCLUDE ROLL BACKS
    private boolean tryCreateProject(int userId, CreateProjectDTO projectToCreate) throws Exception {
        // Create address and retrieve the id
        boolean createdAddressResult = addressService.createAddress(projectToCreate.customer().address());
        if (!createdAddressResult) {
            throw new Exception("Failed to create address");
        }

        // Create customer and retrieve the id
        int createdCustomerResult = customerService.createCustomer(projectToCreate.customer());
        if (createdCustomerResult <= 0) {
            throw new Exception("Failed to create customer");
        }

        // Create project and retrieve the id
        boolean createdProjectResult = projectRepository.createProject(projectToCreate);
        if (!createdProjectResult ) {
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


    // THIS HAS TO BE REFACTORED AND INCLUDE ROLL BACKS
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

}
