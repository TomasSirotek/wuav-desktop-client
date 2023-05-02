package com.wuav.client.dal.repository;


import com.wuav.client.be.Project;
import com.wuav.client.be.user.AppUser;
import com.wuav.client.dal.interfaces.IUserRepository;
import com.wuav.client.dal.mappers.UserMapper;
import com.wuav.client.dal.myBatis.MyBatisConnectionFactory;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class UserRepository implements IUserRepository {

    Logger logger = LoggerFactory.getLogger(UserRepository.class);

    public List<AppUser> getAllUsers() {
        List<AppUser> allUsers = new ArrayList<>();
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            allUsers = mapper.getAllUsers();
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }
        return allUsers;
    }


    @Override
    public AppUser getUserByEmail(String email) {
        AppUser fetchedUser = new AppUser();
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            fetchedUser = mapper.getUserByEmail(email);
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }
        return fetchedUser;
    }

    @Override
    public AppUser getUserById(int id) {
        AppUser fetchedUser = new AppUser();
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            fetchedUser = mapper.getUserById(id);
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }
        return fetchedUser;
    }

    @Override
    public int createCustomer(AppUser appUser) {
        int returnedId = 0;
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            int affectedRows = mapper.createCustomer(
                    appUser
            );
            session.commit();
            returnedId = affectedRows > 0 ?appUser.getId() : 0;
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }
        return returnedId;
    }

    @Override
    public int addUserToRole(int userId, int roleId) {
        int finalAffectedRows = 0;
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            int affectedRows = mapper.addUserToRole(userId,roleId);
            session.commit();
            return affectedRows;
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }
        return finalAffectedRows;
    }
}