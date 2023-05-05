package com.wuav.client.cache;

import com.azure.storage.blob.BlobContainerClient;
import com.wuav.client.dal.blob.BlobStorageFactory;
import com.wuav.client.dal.blob.BlobStorageHelper;
import javafx.scene.image.Image;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ImageCache {
    private static Map<Integer, Image> imageCache = new ConcurrentHashMap<>();
    private static BlobStorageHelper blobStorageHelper = new BlobStorageHelper(BlobStorageFactory.getBlobContainerClient());

    public static Image loadImage(BlobContainerClient containerClient, String imageUrl, int imageId) {
        Image image = imageCache.get(imageId);

        if (image == null) {
            image = blobStorageHelper.downloadImageFromBlobStorage(containerClient, imageUrl);
            imageCache.put(imageId, image);
        }

        return image;
    }

    public static Image getImage(int imageId) {
        return imageCache.get(imageId);
    }
}
