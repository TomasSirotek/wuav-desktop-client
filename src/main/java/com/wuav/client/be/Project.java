package com.wuav.client.be;

import com.wuav.client.be.device.Device;

import java.util.Date;
import java.util.List;

public class Project {

    private int id;

    private String name;

    private String description;

    private Date createdAt;

    private Customer customer;

    private List<CustomImage> projectImages;

    private List<Device> devices;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }


    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }


    public List<CustomImage> getProjectImages() {
        return projectImages;
    }

    public void setProjectImages(List<CustomImage> projectImages) {
        this.projectImages = projectImages;
    }

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

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }
}
