package com.wuav.client.dal.interfaces;

import com.wuav.client.be.Address;
import com.wuav.client.gui.dto.AddressDTO;
import com.wuav.client.gui.dto.PutAddressDTO;

public interface IAddressRepository {

    int createAddress(AddressDTO addressDTO);

    Address getAddressById(int id);

    boolean updateAddress(PutAddressDTO addressDTO);

    boolean deleteAddressById(int id);
}

