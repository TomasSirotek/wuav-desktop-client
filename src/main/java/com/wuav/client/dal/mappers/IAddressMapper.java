package com.wuav.client.dal.mappers;

import com.wuav.client.be.Address;
import org.apache.ibatis.annotations.Param;

public interface IAddressMapper {

    Address getAddressById(@Param("id")int id);

    int createAddress(@Param("id")int id,@Param("street")String street,@Param("city") String city,@Param("zipCode") String zipCode);

    int updateAddress(@Param("addressId") int id, @Param("street") String street, @Param("city") String city,@Param("zipCode") String zipCode);

    int deleteAddress(@Param("addressId") int id);
}
