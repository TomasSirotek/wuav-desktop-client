package com.wuav.client.bll.services;

import com.google.inject.Inject;
import com.wuav.client.be.Address;
import com.wuav.client.bll.services.interfaces.IAddressService;
import com.wuav.client.dal.interfaces.IAddressRepository;
import com.wuav.client.dal.repository.AddressRepository;
import com.wuav.client.gui.dto.AddressDTO;

public class AddressService implements IAddressService {

    private final IAddressRepository addressRepository;

//    public static void main(String[] args) {
//        AddressService addressService = new AddressService(new AddressRepository());
//        AddressDTO addressDTO = new AddressDTO(3, "test", "test", "test");
//
//        int result = addressService.createAddress(addressDTO);
//        System.out.println(result);
//
//        Address address = addressService.getAddressById(3); // should return 1
//        System.out.println(address);
//
//    }

    @Inject
    public AddressService(IAddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public int createAddress(AddressDTO addressDTO) {
        int result = addressRepository.createAddress(addressDTO);
        return result > 0 ? result : -1;
    }

    @Override
    public Address getAddressById(int id) {
        return addressRepository.getAddressById(id);
    }
}
