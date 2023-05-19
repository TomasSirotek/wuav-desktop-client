package com.wuav.client.bll.services;

import com.google.inject.Inject;
import com.wuav.client.be.Address;
import com.wuav.client.bll.services.interfaces.IAddressService;
import com.wuav.client.dal.interfaces.IAddressRepository;
import com.wuav.client.gui.dto.PutAddressDTO;

public class AddressService implements IAddressService {

    private final IAddressRepository addressRepository;

    @Inject
    public AddressService(IAddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Address getAddressById(int id) {
        return addressRepository.getAddressById(id);
    }

    @Override
    public boolean updateAddress(PutAddressDTO addressDTO) {
        return addressRepository.updateAddress(addressDTO);
    }
}
