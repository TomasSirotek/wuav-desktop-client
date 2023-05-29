package com.wuav.client.gui.models.user;


import com.wuav.client.be.Project;
import com.wuav.client.be.user.AppUser;
import com.wuav.client.bll.services.interfaces.IUserService;
import com.google.inject.Inject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The  User model.
 */
public class UserModel implements IUserModel {

    private final IUserService userService;

    private ObservableList<AppUser> allUsersObservableList;

    /**
     * Instantiates a new User model.
     *
     * @param userService the user service
     */
    @Inject
    public UserModel(IUserService userService) {
        this.userService = userService;
        this.allUsersObservableList = getAllUsers();
    }

    /**
     * Gets all users.
     *
     * @return the all users
     */
    @Override
    public ObservableList<AppUser> getAllUsers() {
        return FXCollections.observableArrayList(userService.getAllUsers());
    }

    /**
     * Create user
     *
     * @param name  userName
     * @param email userEmail
     * @param role  userRole
     * @return the boolean if the user was created
     */
    @Override
    public boolean createUser(String name, String email, String role) {
        return userService.createUser(name, email, role);
    }

    /**
     * Gets user by id.
     *
     * @param userEmail the user email
     * @return the user by id
     */
    @Override
    public AppUser getUserByEmail(String userEmail) {
        return allUsersObservableList
                .stream()
                .filter(appUser -> appUser.getEmail().equals(userEmail))
                .findFirst().orElse(null);
    }


    /**
     * Gets user by id.
     *
     * @param appUser the app user
     * @return the user by id
     */
    @Override
    public boolean updateUserById(AppUser appUser) {
        return userService.updateUserById(appUser);
    }

    /**
     * Update user role by role
     *
     * @param id      userId
     * @param appRole userRole
     * @return the boolean if the user role was updated
     */
    @Override
    public boolean updateUserRole(int id, String appRole) {
        return userService.updateUserRole(id, appRole);
    }

    /**
     * Send recovery email
     *
     * @param email userEmail
     * @return the boolean if the recovery email was sent
     * @throws GeneralSecurityException if the security is not valid
     * @throws IOException              if the input or output is not valid
     */
    @Override
    public boolean sendRecoveryEmail(String email) throws GeneralSecurityException, IOException {
        return userService.sendRecoveryEmail(email);
    }

    /**
     * Update user password
     *
     * @param appUser the app user
     * @return the boolean if the user password was updated
     */
    @Override
    public boolean deleteUser(AppUser appUser) {
        return userService.deleteUser(appUser);
    }

    /**
     * Gets user by project id.
     *
     * @param projectId the project id
     * @return the user by project id
     */
    @Override
    public AppUser getUserByProjectId(int projectId) {
        return userService.getUserByProjectId(projectId);
    }

    /**
     * Send email with attachement
     *
     * @param appUser  the app user
     * @param project  the project
     * @param value    the value
     * @param fileName the file name
     * @return the boolean if the email was sent with attachement
     * @throws GeneralSecurityException if the security is not valid
     * @throws IOException              if the input or output is not valid
     */
    @Override
    public boolean sendEmailWithAttachement(AppUser appUser, Project project, ByteArrayOutputStream value, String fileName) throws GeneralSecurityException, IOException {
        return userService.sendEmailWithAttachement(appUser, project, value, fileName);
    }

    /**
     * Search users list.
     *
     * @param query the query
     * @return the list
     */
    @Override
    public List<AppUser> searchUsers(String query) {
        return allUsersObservableList.stream()
                .filter(user -> user.getName().toLowerCase().contains(query.toLowerCase()) // filter by name
                        || user.getEmail().toLowerCase().contains(query.toLowerCase()) // or by email
                        || String.valueOf(user.getId()).equals(query)) // or by id
                .collect(Collectors.toList()); // collect the matching users
    }
}
