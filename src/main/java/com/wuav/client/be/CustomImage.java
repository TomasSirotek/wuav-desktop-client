package com.wuav.client.be;

public class CustomImage {

    private int id;
    private String imageType;

    private String imageUrl;

    public CustomImage(int id, String imageType, String imageUrl) {
        this.id = id;
        this.imageType = imageType;
        this.imageUrl = imageUrl;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
