package com.wuav.client.gui.dto;

public record CustomerDTO(int id,String name,String email,String phone,String customerType,AddressDTO address) {
    @Override
    public String toString() {
        return "CustomerDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", customerType='" + customerType + '\'' +
                ", address=" + address +
                '}';
    }
}
