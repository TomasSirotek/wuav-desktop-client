package com.wuav.client.be;

/**
 * Class for Address
 */
public class Address {

    private int id;
    private String street;
    private String city;
    private String zipCode;

    /**
     * Constructor
     *
     * @param id      address id
     * @param street  street
     * @param city    city
     * @param zipCode zip code
     */
    public Address(int id, String street, String city, String zipCode) {
        this.id = id;
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
    }

    /**
     * Method to get the address id
     *
     * @return int address id
     */
    public int getId() {
        return id;
    }

    /**
     * Method to set the address id
     *
     * @param id address id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Method to get the street
     *
     * @return String street
     */
    public String getStreet() {
        return street;
    }

    /**
     * Method to set the street
     *
     * @param street street
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * Method to get the city
     *
     * @return String city
     */
    public String getCity() {
        return city;
    }

    /**
     * Method to set the city
     *
     * @param city city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Method to get the zip code
     *
     * @return String zip code
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * Method to set the zip code
     *
     * @param zipCode zip code
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * Method to get the address as a string
     *
     * @return String address
     */
    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", zipCode='" + zipCode + '\'' +
                '}';
    }
}
