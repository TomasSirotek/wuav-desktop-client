package com.wuav.client.be;

import com.wuav.client.bll.helpers.Status;

import java.util.Date;
import java.util.List;

public class Project {

    private int id;

    private String name;

    private String description;

    private Date createdAt;

    private CustomImage attachedMainImage;

   // private List<CustomImage> attachedImagesUrl;


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

    public CustomImage getAttachedMainImage() {
        return attachedMainImage;
    }

    public void setAttachedMainImage(CustomImage attachedMainImage) {
        this.attachedMainImage = attachedMainImage;
    }

//    public List<CustomImage> getAttachedImagesUrl() {
//        return attachedImagesUrl;
//    }
//
//    public void setAttachedImagesUrl(List<CustomImage> attachedImagesUrl) {
//        this.attachedImagesUrl = attachedImagesUrl;
//    }



}
