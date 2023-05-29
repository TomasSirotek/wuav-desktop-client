package com.wuav.client.gui.dto;


/**
 * The record PutCustomerDTO.
 */
public record PutCustomerDTO(int id,String name, String email, String phoneNumber, String type, PutAddressDTO addressDTO) {

}
