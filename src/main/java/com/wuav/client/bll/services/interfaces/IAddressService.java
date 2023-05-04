package com.wuav.client.bll.services.interfaces;

import com.wuav.client.be.Address;
import com.wuav.client.gui.dto.AddressDTO;

public interface IAddressService {
    int createAddress(AddressDTO addressDTO);

    Address getAddressById(int id);
}
