package com.wuav.client.dal.mappers;

import com.wuav.client.be.user.AppUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * interface IUserMapper
 **/
public interface IUserMapper {

    /**
     * Gets all users
     *
     * @return List<AppUser>
     */
    List<AppUser> getAllUsers();

    /**
     * Gets user by id
     *
     * @param id id
     * @return AppUser
     */
    AppUser getUserById(@Param("userId") int id);

    /**
     * Gets user by email
     *
     * @param email email
     * @return AppUser
     */
    AppUser getUserByEmail(@Param("email") String email);

    /**
     * Adds User to Role
     *
     * @param userId user id
     * @param roleId role id
     * @return int > 0 if success else 0 (fail)
     */
    int addUserToRole(@Param("userId") int userId, @Param("roleId") int roleId);

    /**
     * Updates user
     *
     * @param userId   user id
     * @param userName userName
     * @param email    email
     * @return int > 0 if success else 0 (fail)
     */
    int updateUser(@Param("userId") int userId, @Param("name") String userName, @Param("email") String email);

    /**
     * Updates user password hash
     *
     * @param id              user id
     * @param newPasswordHash password hash
     * @return int > 0 if success else 0 (fail)
     */
    int updateUserPasswordHash(@Param("userId") int id, @Param("passwordHash") String newPasswordHash);

    /**
     * Creates user
     *
     * @param id       user id
     * @param name     userName
     * @param email    email
     * @param password password
     * @return int > 0 if success else 0 (fail)
     */
    int createUser(@Param("userId") int id, @Param("userName") String name, @Param("email") String email, @Param("passwordHash") String password);

    /**
     * Removes user from role
     *
     * @param userId user id
     * @return int > 0 if success else 0 (fail)
     */
    int removeUserFromRole(@Param("userId") int userId);

    /**
     * Deletes user
     *
     * @param id user id
     * @return int > 0 if success else 0 (fail)
     */
    int deleteUser(@Param("userId") int id);

    /**
     * Gets user by projectId
     *
     * @param projectId project id
     * @return AppUser
     */
    AppUser getUserByProjectId(@Param("projectId") int projectId);
}
