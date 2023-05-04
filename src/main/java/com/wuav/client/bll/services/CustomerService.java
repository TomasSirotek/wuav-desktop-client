package com.wuav.client.bll.services;

import com.google.inject.Inject;
import com.wuav.client.be.Customer;
import com.wuav.client.bll.services.interfaces.ICustomerService;
import com.wuav.client.dal.interfaces.ICustomerRepository;
import com.wuav.client.dal.repository.CustomerRepository;
import com.wuav.client.gui.dto.CustomerDTO;

public class CustomerService implements ICustomerService {

    private ICustomerRepository customerRepository;


        public static void main(String[] args) {
//        var addressDTO = new AddressDTO(1, "street", "city", "zipCode");
//        var customerDTO = new CustomerDTO(1, "name", "email@hmail.com", "10234024","private", addressDTO);
//        var customerRepository = new CustomerRepository();
//       int result =  customerRepository.createCustomer(customerDTO);
//        System.out.println(result);

//      var customerService = new CustomerService(new CustomerRepository());
//        var customer = customerService.getCustomerById(1);
//        System.out.println(customer);

    }

    @Inject
    public CustomerService(ICustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer getCustomerById(int id) {
        return customerRepository.getCustomerById(id);
    }

    @Override
    public int createCustomer(CustomerDTO customerDTO) {
        int result = customerRepository.createCustomer(customerDTO);
        return result > 0 ? result : -1;
    }
}
