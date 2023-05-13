package com.wuav.client.bll.services;

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
    public UserService(IUserRepository userRepository, IRoleService roleService, ICryptoEngine cryptoEngine, IEmailEngine emailEngine, IEmailSender emailSender) {
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
        AppUser appUser = userRepository.getUserById(id);
        System.out.println(appUser.toString());
        return appUser;
    }


    // THIS MIGHT BE REDUNDANT

    @Override
    public int createCustomer(AppUser appUser) {
        int result = 0;
        // create user
        var roleId = 300;

        result = userRepository.createCustomer(appUser);

        if(result > 0){
            result = userRepository.addUserToRole(appUser.getId(),roleId);
            // add user to role
        }

        return result;
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
    public int createUser(String name, String email, String role) {

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
                    if(userRoleResult > 0){
                        return userResult;
                    }
                }
            }
        }
        return 0;
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
    public boolean sendRecoveryEmail(String email) {
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

                 boolean emailSent = emailSender.sendEmail("felipe.orn25@ethereal.email", "Hello!", emailBody, false, null);
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
