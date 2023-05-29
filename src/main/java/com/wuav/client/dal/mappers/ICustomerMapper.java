package com.wuav.client.dal.mappers;

import com.wuav.client.be.Customer;
import org.apache.ibatis.annotations.Param;

/**
 * Interface for CustomerMapper
 */
public interface ICustomerMapper {

    /**
     * Gets customer by id.
     *
     * @param id the id
     * @return the customer by id
     */
    Customer getCustomerById(@Param("id") int id);

    /**
     * Creates customer.
     *
     * @param id          the id
     * @param name        the name
     * @param email       the email
     * @param phoneNumber the phone number
     * @param addressId   the address id
     * @param type        the type
     * @return the int if > 0 then success else fail
     */
    int createCustomer(@Param("id") int id, @Param("name") String name, @Param("email") String email, @Param("phoneNumber") String phoneNumber, @Param("addressId") int addressId, @Param("type") String type);

    /**
     * Updates customer.
     *
     * @param id          the id
     * @param name        the name
     * @param email       the email
     * @param phoneNumber the phone number
     * @param type        the type
     * @return the int if > 0 then success else fail
     */
    int updateCustomer(@Param("customerId") int id, @Param("name") String name, @Param("email") String email, @Param("phoneNumber") String phoneNumber, @Param("type") String type);

    /**
     * Deletes customer.
     *
     * @param id the customer id
     * @return the int if > 0 then success else fail
     */
    int deleteCustomer(@Param("customerId") int id);
}
