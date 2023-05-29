package com.wuav.client.be;

import com.wuav.client.be.device.Device;

import java.util.Date;
import java.util.List;

/**
 * Class for Project
 */

public class Project {

    private int id;

    private String name;

    private String description;

    private Date createdAt;

    private Customer customer;

    private List<CustomImage> projectImages;

    private List<Device> devices;


    /**
     * Method to get the project id
     *
     * @return id project id
     */
    public int getId() {
        return id;
    }

    /**
     * Method to set the project id
     *
     * @param id project id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Method to get the project name
     *
     * @return name project name
     */
    public String getName() {
        return name;
    }

    /**
     * Method to set the project name
     *
     * @param name project name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method to get the project description
     *
     * @return description project description
     */
    public String getDescription() {
        return description;
    }


    /**
     * Method to set the project description
     *
     * @param description project description
     */
    public void setDescription(String description) {
        this.description = description;
    }


    /**
     * Method to get the project creation date
     *
     * @return createdAt project creation date
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Method to get the project Customer
     *
     * @return customer project customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Method to set the project Customer
     *
     * @param customer project customer
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * Method to get the project devices
     *
     * @return devices list
     */
    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    /**
     * Method to get the project images
     *
     * @return projectImages
     */
    public List<CustomImage> getProjectImages() {
        return projectImages;
    }


    /**
     * Method to get the project as a string
     *
     * @return String project
     */
    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", customer=" + customer +
                ", projectImages=" + projectImages +
                '}';
    }
}
