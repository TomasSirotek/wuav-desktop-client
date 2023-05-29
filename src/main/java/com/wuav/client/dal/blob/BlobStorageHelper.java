package com.wuav.client.dal.blob;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.wuav.client.be.CustomImage;
import com.wuav.client.bll.utilities.UniqueIdGenerator;
import javafx.scene.image.Image;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.util.UUID;

/**
 * The blob storage helper
 */
public class BlobStorageHelper {
    private final BlobContainerClient containerClient;

    /**
     * Instantiates a new Blob storage helper
     *
     * @param containerClient the blob container client
     */
    public BlobStorageHelper(BlobContainerClient containerClient) {
        this.containerClient = containerClient;
    }

    /**
     * Uploads an image to blob storage
     *
     * @param imageFile the image file
     * @return the image url
     * @throws IOException if the image could not be uploaded
     */
    public CustomImage uploadImageToBlobStorage(File imageFile) throws IOException {

        String extension = getFileExtension(imageFile.getName());
        String uniqueName = UUID.randomUUID().toString() + "." + extension;
        BlobClient blobClient = containerClient.getBlobClient(uniqueName);

        try (InputStream inputStream = new FileInputStream(imageFile)) {
            blobClient.upload(inputStream, imageFile.length(), true);
        }
        var imageId = UniqueIdGenerator.generateUniqueId();

        return new CustomImage(imageId, extension, blobClient.getBlobUrl());

    }

    /**
     * Downloads an image from blob storage
     *
     * @param containerClient the blob container client
     * @param blobUrl         the blob url
     * @return the image
     * @throws Exception if the image could not be downloaded
     */
    public Image downloadImageFromBlobStorage(BlobContainerClient containerClient, String blobUrl) throws Exception {
        URL url;
        try {
            url = new URL(blobUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new Exception("Invalid image url", e);
        }

        String blobName = url.getPath().substring(url.getPath().lastIndexOf('/') + 1);
        BlobClient blobClient = containerClient.getBlobClient(blobName);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        blobClient.download(outputStream);

        byte[] imageBytes = outputStream.toByteArray();
        return new Image(new ByteArrayInputStream(imageBytes));
    }

    /**
     * Deletes an image from blob storage
     *
     * @param imageUrl the image url
     * @return true if the image was deleted, false otherwise
     * @throws Exception if the image could not be deleted
     */
    public boolean deleteImageIfExist(String imageUrl) throws Exception {
        try {
            URL url = new URL(imageUrl);
            String blobName = url.getPath().substring(url.getPath().lastIndexOf('/') + 1);
            BlobClient blobClient = containerClient.getBlobClient(blobName);
            return blobClient.deleteIfExists();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new Exception("Invalid image url");
        }
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        return (lastDotIndex == -1) ? "" : fileName.substring(lastDotIndex + 1);
    }
}
