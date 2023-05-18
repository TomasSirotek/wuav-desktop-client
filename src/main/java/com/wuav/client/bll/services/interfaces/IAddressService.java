package com.wuav.client.bll.services.interfaces;

import com.wuav.client.be.Address;
import com.wuav.client.gui.dto.AddressDTO;
import com.wuav.client.gui.dto.PutAddressDTO;
import org.apache.ibatis.session.SqlSession;

public interface IAddressService {
    Address getAddressById(int id);

    boolean updateAddress(PutAddressDTO addressDTO);

}
