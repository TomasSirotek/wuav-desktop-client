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

import java.io.File;
import java.util.List;

public class ProjectService implements IProjectService {

    private final IProjectRepository projectRepository;
    private final IAddressService addressService;

    private final ICustomerService customerService;

    private final IImageRepository imageRepository;

    // testing main

//    public static void main(String[] args) {
//        var projectService = new ProjectService(new ProjectRepository(), new AddressService(new AddressRepository()), new ImageRepository(), new CustomerService(new CustomerRepository()));
//
//
//        AddressDTO addressDTO2 = new AddressDTO(63234400, "New customer address", "Bulgaria", "63330");
//
//
//        // create new customer
//        CustomerDTO customerDTO = new CustomerDTO(324234, "tomasek","retardosss@hotmail.com","40303","business",addressDTO2);
//
//        CreateProjectDTO  testProjectDto = new CreateProjectDTO(202343400,"Project_test","new testing",null,customerDTO,null);
//
//
//        var resutl = projectService.createProject(2,testProjectDto);
//        System.out.println(resutl);
//
//    }



    @Inject
    public ProjectService(IProjectRepository projectRepository, IAddressService addressService, IImageRepository imageRepository,ICustomerService customerService) {
        this.projectRepository = projectRepository;
        this.addressService = addressService;
        this.imageRepository = imageRepository;
        this.customerService = customerService;
    }


    @Override
    public List<Project> getProjectByUserId(int userId) {
        return projectRepository.getAllProjectsByUserId(userId);
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

        return true;
    }





//    @Override
//    public boolean createProject(int userId, CreateProjectDTO projectToCreate) {
//
//        // create address and retreive the id
//        // test
//      //  AddressDTO addressDTO = new AddressDTO(100, "FROM SERVICE", "test", "test");
//
//        int createdAddressResult = addressService.createAddress(projectToCreate.customer().address());
//
//        if(createdAddressResult > 0) {
//            System.out.println("Address created successfully");
//            Address fetchedAddress = addressService.getAddressById(projectToCreate.customer().address().id());
//            System.out.println("retrieved from db " + fetchedAddress);
//
//            if(fetchedAddress != null){
//                // create customer and retreive the id
//
//               int createdCustomerResult = customerService.createCustomer(projectToCreate.customer());
//               if(createdCustomerResult > 0 ){
//                   System.out.println("Customer created successfully");
//                   Customer customer = customerService.getCustomerById(projectToCreate.customer().id());
//                   if(customer != null){
//                       System.out.println(customer);
//                          // create project and retreive the id
//
//                       int createdProjectResult = projectRepository.createProject(projectToCreate);
//                       if(createdProjectResult > 0 ){
//                           // return project by id
//                           Project project = projectRepository.getProjectById(projectToCreate.id());
//                           // if user is okay then add project to user
//                            if(project != null){
//                                 System.out.println("Project created successfully");
//                                 System.out.println(project);
//                                 // add project to user
//                                 int isProjectAddedToUser = projectRepository.addProjectToUser(userId,project.getId());
//                                 if(isProjectAddedToUser > 0){
//                                      System.out.println("Project added to user successfully");
//                                      // create image to azure blob storage
//
//                                     // if its successfull then create image in database
//
//                                     // if its successfull then add image to project
//
//
//                                      return true;
//                                 }
//                            }
//                       }
//
//
//
//
//                   }
//               }
//            }
//        }



       // STEPS
        // create address
        // create customer
        // create project
        // add project to user
        // create image
        // add image to project









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

//        return false;
//    }

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
