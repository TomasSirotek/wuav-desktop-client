package com.wuav.client.dal.mappers;

import com.wuav.client.be.Address;
import com.wuav.client.be.Customer;
import org.apache.ibatis.annotations.Param;

public interface CustomerMapper {
    Customer getCustomerById(@Param("id")int id);

    int createCustomer(@Param("id")int id,@Param("name")String name,@Param("email")String email,@Param("phoneNumber") String phoneNumber,@Param("addressId") int addressId,@Param("type")String type);

    int updateCustomer(@Param("customerId") int id,@Param("name") String name,@Param("email") String email,@Param("phoneNumber") String phoneNumber,@Param("type") String type);
}
