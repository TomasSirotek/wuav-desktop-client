package com.wuav.client.dal.blob;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.wuav.client.be.CustomImage;
import com.wuav.client.bll.utilities.UniqueIdGenerator;
import net.coobird.thumbnailator.Thumbnails;
import javafx.scene.image.Image;

import javax.imageio.*;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.util.UUID;

public class BlobStorageHelper {
    private final BlobContainerClient containerClient;

    public BlobStorageHelper(BlobContainerClient containerClient) {
        this.containerClient = containerClient;
    }

    public CustomImage uploadImageToBlobStorage(File imageFile) {
      //  File compressedImageFile = compressAndResizeImage(imageFile);

        String extension = getFileExtension(imageFile.getName());
        String uniqueName = UUID.randomUUID().toString() + "." + extension;
        BlobClient blobClient = containerClient.getBlobClient(uniqueName);

        try (InputStream inputStream = new FileInputStream(imageFile)) {
            blobClient.upload(inputStream, imageFile.length(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        try (InputStream inputStream = new FileInputStream(compressedImageFile)) {
//            blobClient.upload(inputStream, compressedImageFile.length(), true);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        var imageId = UniqueIdGenerator.generateUniqueId();

        return new CustomImage(imageId,extension,blobClient.getBlobUrl());

    }

    public Image downloadImageFromBlobStorage(BlobContainerClient containerClient, String blobUrl) {
        URL url;
        try {
            url = new URL(blobUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

        String blobName = url.getPath().substring(url.getPath().lastIndexOf('/') + 1);
        BlobClient blobClient = containerClient.getBlobClient(blobName);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        blobClient.download(outputStream);

        byte[] imageBytes = outputStream.toByteArray();
        return new Image(new ByteArrayInputStream(imageBytes));
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        return (lastDotIndex == -1) ? "" : fileName.substring(lastDotIndex + 1);
    }



    private File compressAndResizeImage(File imageFile) {
        File output = new File(imageFile.getParent(), "compressed_" + imageFile.getName());

        try {
            Thumbnails.of(imageFile)
                    .size(800, 800) // Adjust the width and height to desired values
                    .outputFormat("png") // Set the output format to PNG
                    .toFile(output);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return output;
    }

}
