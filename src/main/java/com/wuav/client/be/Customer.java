package com.wuav.client.be;

public class Customer {
    private int id;
    private String name;

    private String email;
    private String phone;

    private CustomerType customerType; // one to many

    private Address address; // one to many

    public Customer(int id, String name, String email, String phone, CustomerType customerType, Address address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.customerType = customerType;
        this.address = address;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
