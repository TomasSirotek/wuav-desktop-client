package com.wuav.client.dal.blob;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.wuav.client.be.CustomImage;
import com.wuav.client.bll.utilities.UniqueIdGenerator;

import java.io.*;
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

    }

    // this is for testiiong now will be propably different
    public byte[] downloadImageFromBlobStorage(String blobName, String downloadFilePath){
        BlobClient blobClient = containerClient.getBlobClient(blobName);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        blobClient.download(outputStream);

        byte[] imageBytes = outputStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);

        return inputStream.readAllBytes();
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        return (lastDotIndex == -1) ? "" : fileName.substring(lastDotIndex + 1);
    }
}
