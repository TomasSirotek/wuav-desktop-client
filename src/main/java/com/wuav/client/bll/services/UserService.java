package com.wuav.client.bll.services;

import com.wuav.client.be.Project;
import com.wuav.client.be.user.AppRole;
import com.wuav.client.be.user.AppUser;
import com.wuav.client.bll.services.interfaces.IRoleService;
import com.wuav.client.bll.services.interfaces.IUserService;
import com.wuav.client.bll.utilities.UniqueIdGenerator;
import com.wuav.client.bll.utilities.email.IEmailSender;
import com.wuav.client.bll.utilities.engines.IEmailEngine;
import com.wuav.client.bll.utilities.engines.cryptoEngine.ICryptoEngine;
import com.wuav.client.dal.interfaces.IUserRepository;
import com.google.inject.Inject;
import com.wuav.client.gui.dto.CreateUserDTO;
import com.wuav.client.gui.models.user.CurrentUser;
import com.wuav.client.gui.utils.enums.EmailSubjectType;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class UserService implements IUserService {

    private final IUserRepository userRepository;
    private final IRoleService roleService;
    private final ICryptoEngine cryptoEngine;

    private final IEmailEngine emailEngine;

    private final IEmailSender emailSender;

    @Inject
    public UserService(IUserRepository userRepository,
                       IRoleService roleService,
                       ICryptoEngine cryptoEngine,
                       IEmailEngine emailEngine,
                       IEmailSender emailSender) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.cryptoEngine = cryptoEngine;
        this.emailEngine = emailEngine;
        this.emailSender = emailSender;
    }


    @Override
    public AppUser getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    @Override
    public List<AppUser> getAllUsers() {
        return userRepository.getAllUsers();
    }


    @Override
    public AppUser getUserById(int id) {
        return userRepository.getUserById(id);
    }

    @Override
    public boolean updateUserById(AppUser appUser) {
        boolean isUpdated;

         isUpdated = userRepository.updateUserById(appUser);

        if(isUpdated){
            AppUser updatedUser = getUserById(appUser.getId());
            CurrentUser.getInstance().setLoggedUser(updatedUser);
        }

        return isUpdated;
    }

    @Override
    public boolean changeUserPasswordHash(int id, String newPasswordHash) {
        return userRepository.changeUserPasswordHash(id,newPasswordHash);
    }

    @Override
    public boolean createUser(String name, String email, String role) {
        boolean finalResult = false;
        AppUser existingUser = getUserByEmail(email);

        if(existingUser == null){
            // generate id
            int userId = UniqueIdGenerator.generateUniqueId();
            // generate new password
            String newPassword = generateRandomNumberAsString(8);
            // hash it
            String newPasswordHash = cryptoEngine.Hash(newPassword);

            // construct new user DTO
            CreateUserDTO createUserDTO = new CreateUserDTO(
                    userId,
                    name,
                    email,
                    newPasswordHash);

            int userResult = userRepository.createUser(createUserDTO);

            // if user is created successfully find role by role name and add user to role
            if(userResult > 0){
                AppRole appRole = roleService.getRoleByName(role);
                if(appRole != null){
                    int roleId = appRole.getId();
                    int userRoleResult = userRepository.addUserToRole(userId,roleId);
                    finalResult = userResult > 0 && userRoleResult > 0;
                    // logic is here still has to be fixe
                }
            }
            try {
                finalResult = sendRecoveryEmail(email);
            } catch (GeneralSecurityException | IOException e) {
                throw new RuntimeException(e);
            }
        }
        return finalResult;
    }

    @Override
    public boolean updateUserRole(int id, String appRole) {
        AppRole role = roleService.getRoleByName(appRole);
        if(role != null){
            int roleId = role.getId();
            // remove user from role and add to new role
           int result = userRepository.removeUserFromRole(id);
           if(result > 0){
                return userRepository.addUserToRole(id,roleId) > 0;
           }
        }
        return false;
    }

    @Override
    public boolean sendRecoveryEmail(String email) throws GeneralSecurityException, IOException {
        boolean isSent = false;
        // here implement the email sending logic and return true if email is sent successfully
        String generatedPassword = generateRandomNumberAsString(6);

        // hash the password
        String newPasswordHash = cryptoEngine.Hash(generatedPassword);
        // get user by email and update the password hash
        AppUser appUser = getUserByEmail(email);
        if(appUser != null){
            // update user password hash
             var isChanged = changeUserPasswordHash(appUser.getId(),newPasswordHash);
             if(isChanged){
                    // send email

                 String templateName = "email-template-confirm";
                 Map<String, Object> templateVariables = new HashMap<>();
                 templateVariables.put("newPassword", generatedPassword);

                 //Process the template and generate the email body
                 String emailBody = emailEngine.processTemplate(templateName, templateVariables);

                 boolean emailSent = emailSender.sendEmail(appUser.getEmail(), EmailSubjectType.NEW_PASSWORD.toString().toLowerCase(), emailBody, false, null);
                 if (emailSent) {
                     System.out.println("Email sent successfully");
                     isSent = true;
                 } else {
                     System.out.println("Email sending failed");
                     isSent = false;
                 }
             }
            // if it updated send email with generated password
        }
        return isSent;
    }

    @Override
    public boolean deleteUser(AppUser value) {
        return userRepository.deleteUser(value);
    }

    @Override
    public AppUser getUserByProjectId(int projectId) {
        return userRepository.getUserByProjectId(projectId);
    }

    @Override
    public boolean sendEmailWithAttachement(AppUser appUser, Project project, ByteArrayOutputStream value, String fileName) throws GeneralSecurityException, IOException {
        boolean isSent = false;

        // Define the template name and variables
        String templateName = "email-template";
        Map<String, Object> templateVariables = new HashMap<>();
        templateVariables.put("customerName", project.getCustomer().getName());
        templateVariables.put("technician", appUser.getName());
        templateVariables.put("technicianEmail", appUser.getEmail());
        templateVariables.put("installationDate", project.getCreatedAt());
        templateVariables.put("customerType", project.getCustomer().getType());

        // Process the template and generate the email body
        String emailBody = emailEngine.processTemplate(templateName, templateVariables);

        // Convert the ByteArrayOutputStream to byte[]
        byte[] pdfBytes = value.toByteArray();

        // Create a temporary File object
        File tempFile = File.createTempFile(fileName, ".pdf");
        tempFile.deleteOnExit();

        // Write the byte[] to the temporary file
        try (FileOutputStream fileOutputStream = new FileOutputStream(tempFile)) {
            fileOutputStream.write(pdfBytes);
        }

        boolean emailSent = emailSender.sendEmail(project.getCustomer().getEmail(), EmailSubjectType.PROJECT_REPORT.toString().toLowerCase(), emailBody, true, tempFile);
        if(emailSent) isSent = true;
        if(!emailSent) isSent = false;

        return isSent;
    }

    private String generateRandomNumberAsString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10);
            sb.append(digit);
        }

        return sb.toString();
    }

}
