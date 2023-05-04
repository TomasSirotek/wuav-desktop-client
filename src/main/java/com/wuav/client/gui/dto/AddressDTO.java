package com.wuav.client.gui.dto;

public record AddressDTO(int id,String street, String city, String zipCode) {

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
