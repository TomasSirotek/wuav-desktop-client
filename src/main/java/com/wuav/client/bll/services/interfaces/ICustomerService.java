package com.wuav.client.bll.services.interfaces;

import com.wuav.client.be.Customer;
import com.wuav.client.gui.dto.CustomerDTO;
import com.wuav.client.gui.dto.PutCustomerDTO;
import org.apache.ibatis.session.SqlSession;

public interface ICustomerService {

    Customer getCustomerById(int id);

    boolean updateCustomer(PutCustomerDTO customerDTO);

    boolean deleteCustomerById(int id);
}
