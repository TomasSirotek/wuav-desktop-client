package com.wuav.client.bll.services.interfaces;

import com.wuav.client.be.Customer;
import com.wuav.client.gui.dto.CustomerDTO;

public interface ICustomerService {

    Customer getCustomerById(int id);

    int createCustomer(CustomerDTO customerDTO);
}
