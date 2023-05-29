package com.wuav.client.dal.interfaces;

import com.wuav.client.be.Address;
import com.wuav.client.gui.dto.AddressDTO;
import com.wuav.client.gui.dto.PutAddressDTO;
import org.apache.ibatis.session.SqlSession;

/**
 * Interface for the AddressRepository class.
 */
public interface IAddressRepository {

    /**
     * Creates a new address.
     *
     * @param session    The session.
     * @param addressDTO The address to create.
     * @return True if the address was created successfully, false otherwise.
     * @throws Exception If an error occurs.
     */
    boolean createAddress(SqlSession session, AddressDTO addressDTO) throws Exception;

    /**
     * Gets an address by its id.
     *
     * @param id The id of the address to get.
     * @return The address with the given id.
     */
    Address getAddressById(int id);

    /**
     * Updates an address.
     *
     * @param addressDTO The address to update.
     * @return True if the address was updated successfully, false otherwise.
     */
    boolean updateAddress(PutAddressDTO addressDTO);

    /**
     * Deletes an address.
     *
     * @param session The session.
     * @param id      The id of the address to delete.
     * @return True if the address was deleted successfully, false otherwise.
     */
    boolean deleteAddressById(SqlSession session, int id);
}

