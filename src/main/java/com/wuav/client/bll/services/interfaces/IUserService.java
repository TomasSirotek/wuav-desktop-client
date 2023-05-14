package com.wuav.client.bll.services.interfaces;

import com.wuav.client.be.Project;
import com.wuav.client.be.user.AppUser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface IUserService {


    AppUser getUserByEmail(String email);

    List<AppUser> getAllUsers();

    AppUser getUserById(int id);

    int createCustomer(AppUser appUser);

    boolean updateUserById(AppUser appUser);

    boolean changeUserPasswordHash(int id, String newPasswordHash);

    int createUser(String name, String email, String role);

    boolean updateUserRole(int id, String appRole);

    boolean sendRecoveryEmail(String email) throws GeneralSecurityException, IOException;

    boolean deleteUser(AppUser value);

    AppUser getUserByProjectId(int projectId);

    boolean sendEmailWithAttachement(AppUser appUser,Project project, ByteArrayOutputStream value) throws GeneralSecurityException, IOException;
}
