package com.wuav.client.bll.services;

import com.google.inject.Inject;
import com.wuav.client.be.Customer;
import com.wuav.client.bll.services.interfaces.ICustomerService;
import com.wuav.client.dal.interfaces.ICustomerRepository;
import com.wuav.client.gui.dto.CustomerDTO;
import com.wuav.client.gui.dto.PutCustomerDTO;
import org.apache.ibatis.session.SqlSession;

public class CustomerService implements ICustomerService {

    private ICustomerRepository customerRepository;

    @Inject
    public CustomerService(ICustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer getCustomerById(int id) {
        return customerRepository.getCustomerById(id);
    }

    @Override
    public boolean updateCustomer(PutCustomerDTO customerDTO) {
        return customerRepository.updateCustomer(customerDTO);
    }

    @Override
    public boolean deleteCustomerById(int id) {
        return customerRepository.deleteCustomerById(id);
    }
}
