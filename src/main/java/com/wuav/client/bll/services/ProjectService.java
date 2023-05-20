package com.wuav.client.bll.services;

import com.azure.storage.blob.BlobContainerClient;
import com.google.inject.Inject;
import com.wuav.client.be.CustomImage;
import com.wuav.client.be.Customer;
import com.wuav.client.be.Project;
import com.wuav.client.be.device.Device;
import com.wuav.client.bll.services.interfaces.IProjectService;
import com.wuav.client.dal.blob.BlobStorageFactory;
import com.wuav.client.dal.blob.BlobStorageHelper;
import com.wuav.client.dal.interfaces.*;
import com.wuav.client.dal.myBatis.MyBatisConnectionFactory;
import com.wuav.client.gui.dto.CreateProjectDTO;
import com.wuav.client.gui.dto.ImageDTO;
import com.wuav.client.gui.dto.PutCustomerDTO;
import com.wuav.client.gui.entities.DashboardData;
import org.apache.ibatis.session.SqlSession;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ProjectService implements IProjectService {

    private final IProjectRepository projectRepository;
    private final IAddressRepository addressRepository;

    private final ICustomerRepository customerRepository;

    private final IImageRepository imageRepository;

    private final IDeviceRepository deviceRepository;


    @Inject
    public ProjectService(IProjectRepository projectRepository, IAddressRepository addressRepository, IImageRepository imageRepository, ICustomerRepository customerRepository, IDeviceRepository deviceRepository) {
        this.projectRepository = projectRepository;
        this.addressRepository = addressRepository;
        this.imageRepository = imageRepository;
        this.customerRepository = customerRepository;
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
                    .filter(image -> {
                        try {
                            return deleteIfExists(image.getImageUrl());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .map(image -> {
                        try {
                            return uploadImage(selectedImageFile);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
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
       boolean result = addressRepository.updateAddress(customerDTO.addressDTO());
       if(result){
           boolean projectCustomer = customerRepository.updateCustomer(customerDTO);
           if(projectCustomer){
                return customerRepository.getCustomerById(customerDTO.id());
           }
       }
        return null;
    }

    @Override
    public DashboardData getDashboardData(int userId) {

        // here construct dashboard data
        return null;
    }

    @Override
    public String updateProjectName(int projectId, String newName) throws Exception {
        boolean result = projectRepository.updateProjectName(projectId, newName);
        if(!result) return "";
        return getProjectById(projectId).getDescription();
    }

    private boolean tryCreateProject(int userId, CreateProjectDTO projectToCreate) throws Exception {
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            try {
                // Create address and retrieve the id
                boolean createdAddressResult = addressRepository.createAddress(session, projectToCreate.customer().address());
                if (!createdAddressResult) throw new Exception("Failed to create address");


                // Create customer and retrieve the id
                int createdCustomerResult = customerRepository.createCustomer(session, projectToCreate.customer());
                if (createdCustomerResult <= 0) throw new Exception("Failed to create customer");


                // Create project and retrieve the id
                boolean createdProjectResult = projectRepository.createProject(session, projectToCreate);
                if (!createdProjectResult) throw new Exception("Failed to create project");


                // Add project to user
                int isProjectAddedToUser = projectRepository.addProjectToUser(session, userId, projectToCreate.id());
                if (isProjectAddedToUser <= 0) throw new Exception("Failed to add project to user");


                // add devices to project
                for(Device device : projectToCreate.selectedDevices()){
                    int isDeviceAddedToProject = deviceRepository.addDeviceToProject(session, projectToCreate.id(), device.getId());
                    if (isDeviceAddedToProject <= 0) throw new Exception("Failed to add device to project");
                }

                // Create image in Azure Blob Storage and database, then add it to the project
                uploadAndCreateImage(session,projectToCreate);

                // If no exceptions were thrown, all operations were successful. We can commit the transaction.
                session.commit();
                return true;
            } catch (Exception e) {
                // An exception was thrown during one of the operations. We roll back the transaction.
                session.rollback();
                throw e;
            }
        }
    }

    private void uploadAndCreateImage(SqlSession session,CreateProjectDTO projectToCreate) throws Exception {
        for (ImageDTO imageDTO : projectToCreate.images()) {
            // Upload image to Azure Blob Storage
            CustomImage customImage = uploadImage(imageDTO.getFile());

            // Save image information in the image table
            boolean createdImageResult = imageRepository.createImage(
                    session,
                    customImage.getId(),
                    customImage.getImageType(),
                    customImage.getImageUrl()
            );
            if (!createdImageResult) throw new Exception("Failed to save image to the image table");

            // Add image to the project_image table
            boolean isImageAddedToProject = imageRepository.addImageToProject(session,projectToCreate.id(), customImage.getId(), imageDTO.isMain());
            if (!isImageAddedToProject) throw new Exception("Failed to add image to the project_image table");
        }
    }

    private CustomImage uploadImage(File file) throws IOException {
        BlobContainerClient blobContainerClient = BlobStorageFactory.getBlobContainerClient();
        BlobStorageHelper blobStorageHelper = new BlobStorageHelper(blobContainerClient);

        return blobStorageHelper.uploadImageToBlobStorage(file);
    }

    private boolean deleteIfExists(String imageUrl) throws Exception {
        BlobContainerClient blobContainerClient = BlobStorageFactory.getBlobContainerClient();
        BlobStorageHelper blobStorageHelper = new BlobStorageHelper(blobContainerClient);

        return blobStorageHelper.deleteImageIfExist(imageUrl);
    }

    @Override
    public boolean deleteProject(Project project) throws Exception {
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
        try {
            // 1. Delete all images from blob storage
            for (CustomImage image : project.getProjectImages()) {
                String imageUrl = image.getImageUrl();
                boolean isDeleted = deleteIfExists(imageUrl);
                if (!isDeleted) throw new RuntimeException("Failed to delete image from blob storage: " + imageUrl);
            }

            // 2. Delete all images from database (including from the join table due to cascade delete)
            for (CustomImage image : project.getProjectImages()) {
                boolean imageDeleted = imageRepository.deleteImageById(session,image.getId());
                if (!imageDeleted) throw new RuntimeException("Failed to delete image from database: " + image.getId());
            }

            // 4. Delete project from database
            boolean projectDeleted = projectRepository.deleteProjectById(session,project.getId());
            if (!projectDeleted) throw new RuntimeException("Failed to delete project from database: " + project.getId());

            // 3. Delete customer from database (assuming a customer is linked to a project)
            boolean customerDeleted = customerRepository.deleteCustomerById(session,project.getCustomer().getId());
            if (!customerDeleted) throw new RuntimeException("Failed to delete customer from database for project: " + project.getId());


            // 5. Delete address from database (assuming an address is linked to a project)
            boolean addressDeleted = addressRepository.deleteAddressById(session,project.getCustomer().getAddress().getId());
            if (!addressDeleted) throw new RuntimeException("Failed to delete address from database for project: " + project.getId());
            // If all steps are successful, return true
            session.commit();
            return true;
        } catch (Exception e) {
            // If any step fails, roll back the transaction
            session.rollback();
            throw e;
        }
        }
    }

}
