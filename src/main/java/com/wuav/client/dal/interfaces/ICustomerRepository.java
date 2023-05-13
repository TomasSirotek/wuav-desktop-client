package com.wuav.client.dal.interfaces;

import com.wuav.client.be.Customer;
import com.wuav.client.gui.dto.CustomerDTO;
import com.wuav.client.gui.dto.PutCustomerDTO;

public interface ICustomerRepository {
    Customer getCustomerById(int id);

    int createCustomer(CustomerDTO customerDTO);

    boolean updateCustomer(PutCustomerDTO customerDTO);

    boolean deleteCustomerById(int id);
}
