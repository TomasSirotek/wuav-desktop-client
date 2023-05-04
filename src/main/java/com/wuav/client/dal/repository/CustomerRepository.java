package com.wuav.client.dal.repository;

import com.wuav.client.be.Address;
import com.wuav.client.be.Customer;
import com.wuav.client.dal.interfaces.ICustomerRepository;
import com.wuav.client.dal.mappers.AddressMapper;
import com.wuav.client.dal.mappers.CustomerMapper;
import com.wuav.client.dal.myBatis.MyBatisConnectionFactory;
import com.wuav.client.gui.dto.AddressDTO;
import com.wuav.client.gui.dto.CustomerDTO;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomerRepository implements ICustomerRepository {

    static Logger logger = LoggerFactory.getLogger(AddressRepository.class);


    @Override
    public Customer getCustomerById(int id) {
        Customer customer = null;
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            CustomerMapper mapper = session.getMapper(CustomerMapper.class);
            customer = mapper.getCustomerById(id);
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }
        return customer;
    }


    @Override
    public int createCustomer(CustomerDTO customerDTO) {
        int affectedRowsResult = 0;
        try (SqlSession session = MyBatisConnectionFactory.getSqlSessionFactory().openSession()) {
            CustomerMapper mapper = session.getMapper(CustomerMapper.class);
            var affectedRows = mapper.createCustomer(
                    customerDTO.id(),
                    customerDTO.name(),
                    customerDTO.phone(),
                    customerDTO.email(),
                    customerDTO.address().id(),
                    customerDTO.customerType()
            );

            session.commit();
            affectedRowsResult = affectedRows > 0 ? 1 : 0;
        } catch (Exception ex) {
            logger.error("An error occurred mapping tables", ex);
        }
        return affectedRowsResult;
    }


}
