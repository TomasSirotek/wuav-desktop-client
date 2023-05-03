package com.wuav.client.dal.blob;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.wuav.client.be.CustomImage;
import com.wuav.client.bll.utilities.UniqueIdGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class BlobStorageHelper {
    private final BlobContainerClient containerClient;

    public BlobStorageHelper(BlobContainerClient containerClient) {
        this.containerClient = containerClient;
    }

    public CustomImage uploadImageToBlobStorage(File imageFile) {
        String extension = getFileExtension(imageFile.getName());
        String uniqueName = UUID.randomUUID().toString() + "." + extension;
        BlobClient blobClient = containerClient.getBlobClient(uniqueName);

        try (InputStream inputStream = new FileInputStream(imageFile)) {
            blobClient.upload(inputStream, imageFile.length(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        var imageId = UniqueIdGenerator.generateUniqueId();

        return new CustomImage(imageId,extension,blobClient.getBlobUrl());

        //return blobClient.getBlobUrl();
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        return (lastDotIndex == -1) ? "" : fileName.substring(lastDotIndex + 1);
    }
}
