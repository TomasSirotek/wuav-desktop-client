package com.wuav.client.dal.interfaces;

import com.wuav.client.be.Customer;
import com.wuav.client.gui.dto.CustomerDTO;

public interface ICustomerRepository {
    Customer getCustomerById(int id);

    int createCustomer(CustomerDTO customerDTO);
}
