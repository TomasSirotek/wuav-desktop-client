package com.wuav.client.bll.services.interfaces;

import com.wuav.client.be.Project;
import com.wuav.client.be.user.AppUser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

/**
 * The interface for the user service
 */

public interface IUserService {
    /**
     * Gets the user with the given email
     *
     * @param email the email of the user
     * @return the user
     */
    AppUser getUserByEmail(String email);

    /**
     * Gets all users
     *
     * @return a list of users
     */
    List<AppUser> getAllUsers();

    /**
     * Gets the user with the given id
     *
     * @param id the id of the user
     * @return the user
     */
    AppUser getUserById(int id);

    /**
     * Updates a user
     *
     * @param appUser the user to update
     * @return boolean if the user was updated
     */
    boolean updateUserById(AppUser appUser);

    /**
     * Updates user password hash
     *
     * @param id              the userId to update
     * @param newPasswordHash the new password hash
     * @return boolean if the user password hash was updated
     */
    boolean changeUserPasswordHash(int id, String newPasswordHash);

    /**
     * Creates a user
     *
     * @param name  the name of the user
     * @param email the email of the user
     * @param role  the role of the user
     * @return boolean if the user was created
     */
    boolean createUser(String name, String email, String role);

    /**
     * Updates a user role
     *
     * @param id      the id of the user
     * @param appRole the role of the user
     * @return boolean if the user was updated
     */
    boolean updateUserRole(int id, String appRole);

    /**
     * Sends recovery email
     *
     * @param email the email of the recipient
     * @return boolean if the email was sent
     * @throws GeneralSecurityException
     * @throws IOException
     */
    boolean sendRecoveryEmail(String email) throws GeneralSecurityException, IOException;

    /**
     * Deletes a user
     *
     * @param value the user to delete
     * @return boolean if the user was deleted
     */
    boolean deleteUser(AppUser value);

    /**
     * Gets the user with the given project id
     *
     * @param projectId the id of the project
     * @return the user
     */

    AppUser getUserByProjectId(int projectId);

    /**
     * Sends email with attachement
     *
     * @param appUser  the user to send the email to
     * @param project  the project to send the email about
     * @param value    the attachement
     * @param fileName the name of the attachement
     * @return boolean if the email was sent
     * @throws GeneralSecurityException
     * @throws IOException
     */
    boolean sendEmailWithAttachement(AppUser appUser, Project project, ByteArrayOutputStream value, String fileName) throws GeneralSecurityException, IOException;
}
