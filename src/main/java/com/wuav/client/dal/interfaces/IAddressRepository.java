package com.wuav.client.dal.interfaces;

import com.wuav.client.be.Address;
import com.wuav.client.gui.dto.AddressDTO;
import com.wuav.client.gui.dto.PutAddressDTO;
import org.apache.ibatis.session.SqlSession;

public interface IAddressRepository {

    boolean createAddress(SqlSession session, AddressDTO addressDTO) throws Exception;

    Address getAddressById(int id);

    boolean updateAddress(PutAddressDTO addressDTO);

    boolean deleteAddressById(SqlSession session, int id);
}

