package com.wuav.client.dal.interfaces;

import com.wuav.client.be.user.AppUser;
import com.wuav.client.gui.dto.CreateUserDTO;

import java.util.List;

/**
 * Interface for UserRepository
 */
public interface IUserRepository {

    /**
     * Gets all users.
     *
     * @return the all users
     */
    List<AppUser> getAllUsers();

    /**
     * Gets user by id.
     *
     * @param id the id
     * @return the user by id
     */
    AppUser getUserById(int id);

    /**
     * Gets user by email.
     *
     * @param email the email
     * @return the user by email
     */
    AppUser getUserByEmail(String email);


    /**
     * Adds user to role
     *
     * @param userId the user id
     * @param roleId the role id
     * @return the int if > 0 then success else fail
     */
    int addUserToRole(int userId, int roleId);

    /**
     * Updates user by id.
     *
     * @param appUser the app user
     * @return the boolean
     */
    boolean updateUserById(AppUser appUser);

    /**
     * Change user password hash.
     *
     * @param id              the id
     * @param newPasswordHash the new password hash
     * @return the boolean
     */
    boolean changeUserPasswordHash(int id, String newPasswordHash);

    /**
     * Creates user.
     *
     * @param createUserDTO the create user dto
     * @return the int
     */
    int createUser(CreateUserDTO createUserDTO);

    /**
     * Removes user from role.
     *
     * @param userId the user id
     * @return the int
     */
    int removeUserFromRole(int userId);

    /**
     * Delete user.
     *
     * @param value the value
     * @return the boolean
     */
    boolean deleteUser(AppUser value);

    /**
     * Gets user by project id.
     *
     * @param projectId the project id
     * @return the user by project id
     */
    AppUser getUserByProjectId(int projectId);
}