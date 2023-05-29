package com.wuav.client.cache;

import com.azure.storage.blob.BlobContainerClient;
import com.wuav.client.dal.blob.BlobStorageFactory;
import com.wuav.client.dal.blob.BlobStorageHelper;
import javafx.scene.image.Image;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The image cache
 */
public class ImageCache {
    private static Map<Integer, Image> imageCache = new ConcurrentHashMap<>();
    private static BlobStorageHelper blobStorageHelper = new BlobStorageHelper(BlobStorageFactory.getBlobContainerClient());

    /**
     * Loads an image from the cache or from blob storage
     *
     * @param containerClient the blob container client
     * @param imageUrl        the image url
     * @param imageId         the image id
     * @return the image
     * @throws Exception if the image could not be loaded
     */
    public static Image loadImage(BlobContainerClient containerClient, String imageUrl, int imageId) throws Exception {
        Image image = imageCache.get(imageId);

        if (image == null) {
            image = blobStorageHelper.downloadImageFromBlobStorage(containerClient, imageUrl);
            imageCache.put(imageId, image);
        }

        return image;
    }

    /**
     * Gets an image from the cache
     *
     * @param imageId the image id
     * @return the image
     */
    public static Image getImage(int imageId) {
        return imageCache.get(imageId);
    }

    /**
     * Removes an image from the cache
     *
     * @param imageId the image id
     */
    public static void removeImage(int imageId) {
        imageCache.remove(imageId);
    }
}
