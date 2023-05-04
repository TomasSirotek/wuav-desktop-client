package com.wuav.client.dal.interfaces;

import com.wuav.client.be.Address;
import com.wuav.client.gui.dto.AddressDTO;

public interface IAddressRepository {

    int createAddress(AddressDTO addressDTO);

    Address getAddressById(int id);
}
