package com.wuav.client.dal.interfaces;

import com.wuav.client.be.Customer;
import com.wuav.client.gui.dto.CustomerDTO;
import com.wuav.client.gui.dto.PutCustomerDTO;
import org.apache.ibatis.session.SqlSession;

/**
 * Interface for the CustomerRepository class.
 */
public interface ICustomerRepository {

    /**
     * Gets a customer by its id.
     *
     * @param id The id of the customer to get.
     * @return The customer with the given id.
     */
    Customer getCustomerById(int id);

    /**
     * Creates a new customer.
     *
     * @param session     The session.
     * @param customerDTO The customer to create.
     * @return True if the customer was created successfully, false otherwise.
     * @throws Exception If an error occurs.
     */

    int createCustomer(SqlSession session, CustomerDTO customerDTO) throws Exception;

    /**
     * Updates a customer.
     *
     * @param customerDTO The customer to update.
     * @return True if the customer was updated successfully, false otherwise.
     */
    boolean updateCustomer(PutCustomerDTO customerDTO);

    /**
     * Deletes a customer.
     *
     * @param session The session.
     * @param id      The id of the customer to delete.
     * @return True if the customer was deleted successfully, false otherwise.
     * @throws Exception If an error occurs.
     */

    boolean deleteCustomerById(SqlSession session, int id) throws Exception;
}
