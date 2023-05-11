package com.wuav.client.dal.repository;

import com.wuav.client.be.user.AppRole;
import com.wuav.client.be.user.AppUser;
import com.wuav.client.dal.interfaces.IRoleRepository;
import com.wuav.client.dal.mappers.RoleMapper;
import com.wuav.client.dal.mappers.UserMapper;
import com.wuav.client.dal.myBatis.MyBatisConnectionFactory;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RoleRepository implements IRoleRepository {

    Logger logger = LoggerFactory.getLogger(UserRepository.class);

    @Override
    public AppRole getRoleByName(String name) {
        AppRole fetchedRole = new AppRole();
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            RoleMapper mapper = session.getMapper(RoleMapper.class);
            fetchedRole = mapper.getRoleByName(name);
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }
        return fetchedRole;
    }
}
