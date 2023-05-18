package com.wuav.client.dal.interfaces;

import com.wuav.client.be.Customer;
import com.wuav.client.gui.dto.CustomerDTO;
import com.wuav.client.gui.dto.PutCustomerDTO;
import org.apache.ibatis.session.SqlSession;

public interface ICustomerRepository {
    Customer getCustomerById(int id);

    int createCustomer(SqlSession session,CustomerDTO customerDTO) throws Exception;

    boolean updateCustomer(PutCustomerDTO customerDTO);

    boolean deleteCustomerById(int id);
}
