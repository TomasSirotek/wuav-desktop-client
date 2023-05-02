package com.wuav.client.be;

import com.wuav.client.bll.helpers.Status;

import java.util.Date;
import java.util.List;

public class Project {

    private int id;

    private String name;

    private String description;

    private Date createdAt;

    private String status;  // active, completed, deleted

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

//    public List<CustomImage> getAttachedImagesUrl() {
//        return attachedImagesUrl;
//    }
//
//    public void setAttachedImagesUrl(List<CustomImage> attachedImagesUrl) {
//        this.attachedImagesUrl = attachedImagesUrl;
//    }


    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", status='" + status + '\'' +
                '}';
    }
}
