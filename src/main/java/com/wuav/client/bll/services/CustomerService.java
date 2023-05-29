package com.wuav.client.bll.services;

import com.google.inject.Inject;
import com.wuav.client.be.Customer;
import com.wuav.client.bll.services.interfaces.ICustomerService;
import com.wuav.client.dal.interfaces.ICustomerRepository;
import com.wuav.client.gui.dto.PutCustomerDTO;

public class CustomerService implements ICustomerService {

    private ICustomerRepository customerRepository;

    @Inject
    public CustomerService(ICustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public boolean updateCustomer(PutCustomerDTO customerDTO) {
        return customerRepository.updateCustomer(customerDTO);
    }

}
