package com.wuav.client.dal.repository;

import com.wuav.client.be.Address;
import com.wuav.client.be.Customer;
import com.wuav.client.dal.mappers.IAddressMapper;
import com.wuav.client.dal.mappers.ICustomerMapper;
import com.wuav.client.dal.myBatis.MyBatisConnectionFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class CustomerRepositoryTest {

    private static final int EXISTING_CUSTOMER_ID = 2055967059;

    private CustomerRepository customerRepository;
    private SqlSessionFactory sqlSessionFactory;
    private SqlSession sqlSession;
    private ICustomerMapper iCustomerMapper;

    @BeforeEach
    void setUp() {
        customerRepository = new CustomerRepository();
        sqlSessionFactory = mock(SqlSessionFactory.class);
        sqlSession = mock(SqlSession.class);
        iCustomerMapper = mock(ICustomerMapper.class);
        doReturn(sqlSession).when(sqlSessionFactory).openSession();
        doReturn(iCustomerMapper).when(sqlSession).getMapper(IAddressMapper.class);
        MyBatisConnectionFactory.setSqlSessionFactory(sqlSessionFactory);
    }

    @Test
    void getCustomerById() {
    }

    @Test
    void createCustomer() {
    }

    @Test
    void updateCustomer() {
    }
}