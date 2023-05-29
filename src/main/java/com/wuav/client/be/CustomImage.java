package com.wuav.client.be;

/**
 * Class for CustomImage
 */
public class CustomImage {

    private int id;
    private String imageType;

    private String imageUrl;

    private boolean isMainImage;

    /**
     * Constructor
     *
     * @param id        image id
     * @param imageType image type
     * @param imageUrl  image url
     */

    public CustomImage(int id, String imageType, String imageUrl) {
        this.id = id;
        this.imageType = imageType;
        this.imageUrl = imageUrl;
    }

    /**
     * Constructor
     */
    public CustomImage() {
    }

    /**
     * Method to get the image id
     *
     * @return image id
     */
    public int getId() {
        return id;
    }


    /**
     * Method to set the image id
     *
     * @param id image id
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * Method to get the image type
     *
     * @return image type
     */
    public String getImageType() {
        return imageType;
    }


    /**
     * Method to get the image URL
     *
     * @return imageUrl
     */

    public String getImageUrl() {
        return imageUrl;
    }


    /**
     * Method to get the isMainImage value
     *
     * @return isMainImage
     */


    public boolean isMainImage() {
        return isMainImage;
    }

    /**
     * Method to set the mainImage value
     *
     * @param mainImage mainImage value
     */
    public void setMainImage(boolean mainImage) {
        isMainImage = mainImage;
    }


    /**
     * Method to get the custom image as a string
     *
     * @return String custom image
     */
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
