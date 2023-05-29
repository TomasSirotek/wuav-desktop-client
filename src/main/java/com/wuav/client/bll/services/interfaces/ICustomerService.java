package com.wuav.client.bll.services.interfaces;

import com.wuav.client.gui.dto.PutCustomerDTO;

/**
 * The interface for the customer service
 */
public interface ICustomerService {

    /**
     * Updates the customer with the given customerDTO
     *
     * @param customerDTO the customer to update
     * @return boolean if the customer was updated
     */
    boolean updateCustomer(PutCustomerDTO customerDTO);

}
