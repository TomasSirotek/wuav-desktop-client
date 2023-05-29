package com.wuav.client.dal.repository;

import com.wuav.client.be.user.AppRole;
import com.wuav.client.dal.interfaces.IRoleRepository;
import com.wuav.client.dal.mappers.IRoleMapper;
import com.wuav.client.dal.myBatis.MyBatisConnectionFactory;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RoleRepository class.
 */
public class RoleRepository implements IRoleRepository {

    private Logger logger = LoggerFactory.getLogger(UserRepository.class);

    /**
     * Get the role by name.
     *
     * @param name the name
     * @return AppRole
     */
    @Override
    public AppRole getRoleByName(String name) {
        AppRole fetchedRole = new AppRole();
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            IRoleMapper mapper = session.getMapper(IRoleMapper.class);
            fetchedRole = mapper.getRoleByName(name);
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }
        return fetchedRole;
    }
}
