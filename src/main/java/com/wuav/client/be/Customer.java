package com.wuav.client.be;

/**
 * Class for Customer
 */
public class Customer {
    private int id;
    private String name;

    private String email;

    private String phoneNumber;

    private String type;

    private Address address;

    /**
     * Constructor
     *
     * @param id          customer id
     * @param name        customer name
     * @param email       customer email
     * @param phoneNumber customer phone number
     * @param type        customer type
     */
    public Customer(int id, String name, String email, String phoneNumber, String type) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.type = type;
    }


    /**
     * Method to get the customer id
     *
     * @return customer id
     */
    public int getId() {
        return id;
    }

    /**
     * Method to set the customer id
     *
     * @param id customer id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Method to get the customer name
     *
     * @return customer name
     */
    public String getName() {
        return name;
    }

    /**
     * Method to set the customer name
     *
     * @param name customer name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method to get the customer email
     *
     * @return customer email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Method to get the customer phone number
     *
     * @return customer phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Method to set the customer email
     *
     * @param email customer email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Method to get the customer type
     *
     * @return customer type
     */
    public String getType() {
        return type;
    }

    /**
     * Method to set the customer type
     *
     * @param type customer type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Method to get the customer address
     *
     * @return customer Address
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Method to set the customer address
     *
     * @param address customer address
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * Method to get the customer as a string
     *
     * @return String customer
     */
    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", type='" + type + '\'' +
                ", address=" + address +
                '}';
    }
}
