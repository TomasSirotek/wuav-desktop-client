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

/**
 * The implementation of the user service
 */

public class UserService implements IUserService {

    private final IUserRepository userRepository;
    private final IRoleService roleService;
    private final ICryptoEngine cryptoEngine;

    private final IEmailEngine emailEngine;

    private final IEmailSender emailSender;

    /**
     * Constructor
     *
     * @param userRepository the user repository
     * @param roleService    the role service
     * @param cryptoEngine   the crypto engine
     * @param emailEngine    the email engine
     * @param emailSender    the email sender
     */
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


    /**
     * Gets the user with the given email
     *
     * @param email the email of the user
     * @return the user
     */
    @Override
    public AppUser getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    /**
     * Gets all users
     *
     * @return the list of users
     */
    @Override
    public List<AppUser> getAllUsers() {
        return userRepository.getAllUsers();
    }


    /**
     * Gets user by id
     *
     * @param id the id of the user
     * @return the user
     */
    @Override
    public AppUser getUserById(int id) {
        return userRepository.getUserById(id);
    }

    /**
     * Updates the user with the given id
     *
     * @param appUser the user to update
     * @return boolean if the user was updated
     */
    @Override
    public boolean updateUserById(AppUser appUser) {
        boolean isUpdated;

        isUpdated = userRepository.updateUserById(appUser);

        if (isUpdated) {
            AppUser updatedUser = getUserById(appUser.getId());
            CurrentUser.getInstance().setLoggedUser(updatedUser);
        }

        return isUpdated;
    }

    /**
     * Updates the user password hash
     *
     * @param id              the userId to update
     * @param newPasswordHash the new password hash
     * @return boolean if the user password hash was updated
     */
    @Override
    public boolean changeUserPasswordHash(int id, String newPasswordHash) {
        return userRepository.changeUserPasswordHash(id, newPasswordHash);
    }

    /**
     * Creates user with the given name, email and role
     *
     * @param name  the name of the user
     * @param email the email of the user
     * @param role  the role of the user
     * @return boolean if the user was created
     */
    @Override
    public boolean createUser(String name, String email, String role) {
        boolean finalResult = false;
        AppUser existingUser = getUserByEmail(email);

        if (existingUser == null) {
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
            if (userResult > 0) {
                AppRole appRole = roleService.getRoleByName(role);
                if (appRole != null) {
                    int roleId = appRole.getId();
                    int userRoleResult = userRepository.addUserToRole(userId, roleId);
                    if (userRoleResult > 0) {
                        try {
                            finalResult = sendRecoveryEmail(email);
                        } catch (GeneralSecurityException | IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }

        }
        return finalResult;
    }

    /**
     * Updates user role
     *
     * @param id      the id of the user
     * @param appRole the role of the user
     * @return boolean if the user role was updated
     */
    @Override
    public boolean updateUserRole(int id, String appRole) {
        AppRole role = roleService.getRoleByName(appRole);
        if (role != null) {
            int roleId = role.getId();
            // remove user from role and add to new role
            int result = userRepository.removeUserFromRole(id);
            if (result > 0) {
                return userRepository.addUserToRole(id, roleId) > 0;
            }
        }
        return false;
    }

    /**
     * Sends email to the customer with the given email
     *
     * @param email the email of the recipient
     * @return
     * @throws GeneralSecurityException
     * @throws IOException
     */
    @Override
    public boolean sendRecoveryEmail(String email) throws GeneralSecurityException, IOException {
        boolean isSent = false;
        // here implement the email sending logic and return true if email is sent successfully
        String generatedPassword = generateRandomNumberAsString(6);

        // hash the password
        String newPasswordHash = cryptoEngine.Hash(generatedPassword);
        // get user by email and update the password hash
        AppUser appUser = getUserByEmail(email);
        if (appUser != null) {
            // update user password hash
            var isChanged = changeUserPasswordHash(appUser.getId(), newPasswordHash);
            if (isChanged) {
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
        }
        return isSent;
    }

    /**
     * Deletes user
     *
     * @param value the user to delete
     * @return boolean if the user was deleted
     */
    @Override
    public boolean deleteUser(AppUser value) {
        return userRepository.deleteUser(value);
    }

    /**
     * Gets user by project id
     *
     * @param projectId the id of the project
     * @return the user
     */
    @Override
    public AppUser getUserByProjectId(int projectId) {
        return userRepository.getUserByProjectId(projectId);
    }

    /**
     * Sends email with attachment
     *
     * @param appUser  the user to send the email to
     * @param project  the project to send the email about
     * @param value    the attachment
     * @param fileName the name of the attachment
     * @return boolean if the email was sent
     * @throws GeneralSecurityException
     * @throws IOException
     */
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
        if (emailSent) isSent = true;
        if (!emailSent) isSent = false;

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
