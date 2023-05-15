package com.wuav.client.be;

public class CustomImage {

    private int id;
    private String imageType;

    private String imageUrl;

    private boolean isMainImage;

    public CustomImage(int id, String imageType, String imageUrl) {
        this.id = id;
        this.imageType = imageType;
        this.imageUrl = imageUrl;
    }

    public CustomImage(){
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

    public boolean isMainImage() {
        return isMainImage;
    }

    public void setMainImage(boolean mainImage) {
        isMainImage = mainImage;
    }


    @Override
    public String toString() {
        return "CustomImage{" +
                "id=" + id +
                ", imageType='" + imageType + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", isMainImage=" + isMainImage +
                '}';
    }
}
