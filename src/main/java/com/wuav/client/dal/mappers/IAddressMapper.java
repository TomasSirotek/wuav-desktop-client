package com.wuav.client.dal.mappers;

import com.wuav.client.be.Address;
import org.apache.ibatis.annotations.Param;

/**
 * Interface for AddressMapper
 */
public interface IAddressMapper {

    /**
     * Gets address by id.
     *
     * @param id the id
     * @return the address by id
     */
    Address getAddressById(@Param("id") int id);

    /**
     * Creates address.
     *
     * @param id      the id
     * @param street  the street
     * @param city    the city
     * @param zipCode the zip code
     * @return the int if > 0 then success else fail
     */
    int createAddress(@Param("id") int id, @Param("street") String street, @Param("city") String city, @Param("zipCode") String zipCode);

    /**
     * Updates address.
     *
     * @param id      the id
     * @param street  the street
     * @param city    the city
     * @param zipCode the zip code
     * @return the int if > 0 then success else fail
     */
    int updateAddress(@Param("addressId") int id, @Param("street") String street, @Param("city") String city, @Param("zipCode") String zipCode);

    /**
     * Deletes address.
     *
     * @param id the id
     * @return the int if > 0 then success else fail
     */
    int deleteAddress(@Param("addressId") int id);
}
