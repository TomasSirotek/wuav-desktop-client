package com.wuav.client.gui.models.user;

import com.wuav.client.be.Project;
import com.wuav.client.be.user.AppUser;
import javafx.collections.ObservableList;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

/**
 * The  User model.
 */
public interface IUserModel {

    /**
     * Gets all users.
     *
     * @return the all users
     */
    ObservableList<AppUser> getAllUsers();

    /**
     * Create user
     *
     * @param name  userName
     * @param email userEmail
     * @param role  userRole
     * @return the boolean if the user was created
     */

    boolean createUser(String name, String email, String role);


    /**
     * Get user by email
     *
     * @param userEmail
     * @return the user
     */
    AppUser getUserByEmail(String userEmail);

    /**
     * Update user by id
     *
     * @param appUser the user to update
     * @return the boolean if the user was updated
     */
    boolean updateUserById(AppUser appUser);

    /**
     * Update user role
     *
     * @param id      the user id
     * @param appRole the user role
     * @return the boolean if the user role was updated
     */
    boolean updateUserRole(int id, String appRole);

    /**
     * Send recovery email
     *
     * @param email the user email
     * @return the boolean if the email was sent
     * @throws GeneralSecurityException if credentials are invalid
     * @throws IOException              if the email was not sent
     */
    boolean sendRecoveryEmail(String email) throws GeneralSecurityException, IOException;

    /**
     * Deletes user
     *
     * @param value the user to delete
     * @return the boolean if the user was deleted
     */
    boolean deleteUser(AppUser value);

    /**
     * Gets user by project id
     *
     * @param projectId
     * @return the user
     */
    AppUser getUserByProjectId(int projectId);


    /**
     * Send email with attachment
     *
     * @param appUser the user to send the email to
     * @param project the project to send the email about
     * @param value   the attachment
     * @param text    the email text
     * @return the boolean if the email was sent
     * @throws GeneralSecurityException if credentials are invalid
     * @throws IOException              if the email was not sent
     */
    boolean sendEmailWithAttachement(AppUser appUser, Project project, ByteArrayOutputStream value, String text) throws GeneralSecurityException, IOException;

    /**
     * Search users
     *
     * @param query the query to search
     * @return the list of users
     */
    List<AppUser> searchUsers(String query);
}
