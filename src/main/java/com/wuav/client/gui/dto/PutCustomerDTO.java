package com.wuav.client.gui.dto;

import com.wuav.client.be.Address;

public record PutCustomerDTO(int id,String name, String email, String phoneNumber, String type, PutAddressDTO addressDTO) {

}
