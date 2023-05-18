package com.wuav.client.dal.repository;

import com.wuav.client.be.Customer;
import com.wuav.client.dal.interfaces.ICustomerRepository;
import com.wuav.client.dal.mappers.ICustomerMapper;
import com.wuav.client.dal.myBatis.MyBatisConnectionFactory;
import com.wuav.client.gui.dto.CustomerDTO;
import com.wuav.client.gui.dto.PutCustomerDTO;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomerRepository implements ICustomerRepository {

    static Logger logger = LoggerFactory.getLogger(AddressRepository.class);

    @Override
    public Customer getCustomerById(int id) {
        Customer customer = null;
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            ICustomerMapper mapper = session.getMapper(ICustomerMapper.class);
            customer = mapper.getCustomerById(id);
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }
        return customer;
    }

    @Override
    public int createCustomer(SqlSession session,CustomerDTO customerDTO) throws Exception {
        try {
            ICustomerMapper mapper = session.getMapper(ICustomerMapper.class);
            var affectedRows = mapper.createCustomer(
                    customerDTO.id(),
                    customerDTO.name(),
                    customerDTO.email(),
                    customerDTO.phone(),
                    customerDTO.address().id(),
                    customerDTO.customerType()
            );
            return affectedRows;
        } catch (PersistenceException ex) {
            logger.error("An error occurred mapping tables", ex);
            throw new Exception(ex);
        }
    }

    @Override
    public boolean updateCustomer(PutCustomerDTO customerDTO) {
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            ICustomerMapper mapper = session.getMapper(ICustomerMapper.class);
            var affectedRows = mapper.updateCustomer(
                    customerDTO.id(),
                    customerDTO.name(),
                    customerDTO.email(),
                    customerDTO.phoneNumber(),
                    customerDTO.type()
            );
            session.commit();
            return affectedRows > 0;
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }
        return false;
    }

    @Override
    public boolean deleteCustomerById(SqlSession session, int id) throws Exception {
        try {
            ICustomerMapper mapper = session.getMapper(ICustomerMapper.class);
            int affectedRows = mapper.deleteCustomer(
                    id
            );
            return affectedRows > 0;
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
            throw new Exception(ex);
        }
    }


}
