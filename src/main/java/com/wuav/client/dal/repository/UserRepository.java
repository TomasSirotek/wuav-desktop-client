package com.wuav.client.dal.repository;


import com.wuav.client.be.user.AppUser;
import com.wuav.client.dal.interfaces.IUserRepository;
import com.wuav.client.dal.mappers.IUserMapper;
import com.wuav.client.dal.myBatis.MyBatisConnectionFactory;
import com.wuav.client.gui.dto.CreateUserDTO;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * UserRepository class.
 */
public class UserRepository implements IUserRepository {

    private Logger logger = LoggerFactory.getLogger(UserRepository.class);

    /**
     * Get all users.
     *
     * @return List<AppUser>
     */
    @Override
    public List<AppUser> getAllUsers() {
        List<AppUser> allUsers = new ArrayList<>();
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            IUserMapper mapper = session.getMapper(IUserMapper.class);
            allUsers = mapper.getAllUsers();
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }
        return allUsers;
    }

    /**
     * Get the user by project id.
     *
     * @param projectId the project id
     * @return AppUser
     */
    @Override
    public AppUser getUserByProjectId(int projectId) {
        AppUser fetchedUser = new AppUser();
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            IUserMapper mapper = session.getMapper(IUserMapper.class);
            fetchedUser = mapper.getUserByProjectId(projectId);
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }
        return fetchedUser;
    }


    /**
     * Get the user by email.
     *
     * @param email the email
     * @return AppUser
     */
    @Override
    public AppUser getUserByEmail(String email) {
        AppUser fetchedUser = new AppUser();
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            IUserMapper mapper = session.getMapper(IUserMapper.class);
            fetchedUser = mapper.getUserByEmail(email);
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }
        return fetchedUser;
    }


    /**
     * Create a new user.
     *
     * @param id the id
     * @return AppUser
     */
    @Override
    public AppUser getUserById(int id) {
        AppUser fetchedUser = new AppUser();
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            IUserMapper mapper = session.getMapper(IUserMapper.class);
            fetchedUser = mapper.getUserById(id);
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }
        return fetchedUser;
    }


    /**
     * Create a new user.
     *
     * @param userId the user id
     * @param roleId the role id
     * @return int affected rows
     */
    @Override
    public int addUserToRole(int userId, int roleId) {
        int finalAffectedRows = 0;
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            IUserMapper mapper = session.getMapper(IUserMapper.class);
            int affectedRows = mapper.addUserToRole(userId, roleId);
            session.commit();
            return affectedRows;
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }
        return finalAffectedRows;
    }

    /**
     * Update user by id.
     *
     * @param appUser the app user
     * @return boolean true if updated
     */
    @Override
    public boolean updateUserById(AppUser appUser) {
        int affectedRows = 0;

        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            IUserMapper mapper = session.getMapper(IUserMapper.class);
            affectedRows = mapper.updateUser(
                    appUser.getId(),
                    appUser.getName(),
                    appUser.getEmail()
            );
            session.commit();

            return affectedRows > 0;
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }

        return false;
    }

    /**
     * Change user password hash.
     *
     * @param id              the id
     * @param newPasswordHash the new password hash
     * @return boolean true if updated
     */
    @Override
    public boolean changeUserPasswordHash(int id, String newPasswordHash) {
        int affectedRows = 0;

        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            IUserMapper mapper = session.getMapper(IUserMapper.class);
            affectedRows = mapper.updateUserPasswordHash(
                    id,
                    newPasswordHash
            );
            session.commit();

            return affectedRows > 0;
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }

        return false;
    }

    /**
     * Create a new user.
     *
     * @param createUserDTO the create user dto
     * @return int affected rows
     */
    @Override
    public int createUser(CreateUserDTO createUserDTO) {
        int returnedId = 0;
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            IUserMapper mapper = session.getMapper(IUserMapper.class);
            int affectedRows = mapper.createUser(
                    createUserDTO.id(),
                    createUserDTO.name(),
                    createUserDTO.email(),
                    createUserDTO.password()
            );
            session.commit();
            returnedId = affectedRows > 0 ? affectedRows : 0;
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }
        return returnedId;
    }

    /**
     * Remove user from role.
     *
     * @param userId the user id
     * @return int affected rows
     */
    @Override
    public int removeUserFromRole(int userId) {
        int finalAffectedRows = 0;
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            IUserMapper mapper = session.getMapper(IUserMapper.class);
            int affectedRows = mapper.removeUserFromRole(userId);
            session.commit();
            return affectedRows;
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }
        return finalAffectedRows;
    }

    /**
     * Delete user.
     *
     * @param value the value
     * @return boolean true if deleted
     */
    @Override
    public boolean deleteUser(AppUser value) {
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            IUserMapper mapper = session.getMapper(IUserMapper.class);
            int affectedRows = mapper.deleteUser(value.getId());
            session.commit();
            return affectedRows > 0;
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }
        return false;
    }

}