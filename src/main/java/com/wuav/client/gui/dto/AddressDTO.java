package com.wuav.client.gui.dto;

/**
 * The record AddressDTO.
 */
public record AddressDTO(int id,String street, String city, String zipCode) {

    /**
     * To string
     * @return the string
     */
    @Override
    public String toString() {
        return "AddressDTO{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", zipCode='" + zipCode + '\'' +
                '}';
    }



}
