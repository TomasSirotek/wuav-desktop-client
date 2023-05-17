package com.wuav.client.gui.models.user;

import com.wuav.client.be.Project;
import com.wuav.client.be.user.AppUser;
import javafx.collections.ObservableList;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;

public interface IUserModel {

    ObservableList<AppUser> getAllUsers();


    boolean createUser(String name,String email,String role);


    AppUser getUserByEmail(String customerEmail);

    boolean updateUserById(AppUser appUser);

    boolean updateUserRole(int id, String appRole);

    boolean sendRecoveryEmail(String email) throws GeneralSecurityException, IOException;

    boolean deleteUser(AppUser value);

    AppUser getUserByProjectId(int projectId);


    boolean sendEmailWithAttachement(AppUser appUser, Project project, ByteArrayOutputStream value, String text) throws GeneralSecurityException, IOException;
}
