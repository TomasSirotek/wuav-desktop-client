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

public class UserModel implements IUserModel{

    private final IUserService userService;

    private ObservableList<AppUser> allUsersObservableList;

    @Inject
    public UserModel(IUserService userService) {
        this.userService = userService;
        this.allUsersObservableList = getAllUsers();
    }

    @Override
    public ObservableList<AppUser> getAllUsers() {
        return FXCollections.observableArrayList(userService.getAllUsers());
    }

    @Override
    public boolean createUser(String name,String email,String role) {
        return userService.createUser(name,email,role);
    }

    @Override
    public AppUser getUserByEmail(String customerName) {
        return allUsersObservableList
                .stream()
                .filter(appUser -> appUser.getEmail().equals(customerName))
                .findFirst().orElse(null);
    }


    @Override
    public boolean updateUserById(AppUser appUser) {
        return userService.updateUserById(appUser);
    }

    @Override
    public boolean updateUserRole(int id, String appRole) {
        return userService.updateUserRole(id,appRole);
    }

    @Override
    public boolean sendRecoveryEmail(String email) throws GeneralSecurityException, IOException {
        return userService.sendRecoveryEmail(email);
    }

    @Override
    public boolean deleteUser(AppUser value) {
        return userService.deleteUser(value);
    }

    @Override
    public AppUser getUserByProjectId(int projectId) {
        return userService.getUserByProjectId(projectId);
    }

    @Override
    public boolean sendEmailWithAttachement(AppUser appUser, Project project, ByteArrayOutputStream value, String fileName) throws GeneralSecurityException, IOException {
       return userService.sendEmailWithAttachement(appUser,project,value,fileName);
    }

    @Override
    public List<AppUser> searchUsers(String query) {
        return allUsersObservableList.stream()
                .filter(user -> user.getName().toLowerCase().contains(query.toLowerCase()) // filter by name
                        || user.getEmail().toLowerCase().contains(query.toLowerCase()) // or by email
                        || String.valueOf(user.getId()).equals(query)) // or by id
                .collect(Collectors.toList()); // collect the matching users
    }
}
