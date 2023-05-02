package com.wuav.client.be;

import com.wuav.client.bll.helpers.Status;

import java.util.Date;
import java.util.List;

public class Project {

    private int id;

    private String name;

    private String description;

    private Customer customer;

    private Date createdAt;

    private String mainImageURL;

    private List<CustomImage> attachedImagesUrl;

    private Status status;  // active, completed, deleted

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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }


    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getMainImageURL() {
        return mainImageURL;
    }

    public void setMainImageURL(String mainImageURL) {
        this.mainImageURL = mainImageURL;
    }

    public List<CustomImage> getAttachedImagesUrl() {
        return attachedImagesUrl;
    }

    public void setAttachedImagesUrl(List<CustomImage> attachedImagesUrl) {
        this.attachedImagesUrl = attachedImagesUrl;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
