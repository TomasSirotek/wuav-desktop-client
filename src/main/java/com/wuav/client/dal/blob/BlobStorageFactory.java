package com.wuav.client.dal.blob;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.wuav.client.dal.myBatis.MyBatisConnectionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BlobStorageFactory {
    private static final String CONFIG_FILE_NAME = System.getenv("CONFIG_BLOB");
    private static BlobContainerClient blobContainerClient;

    static {
        try {
            Properties properties = new Properties();
            try (InputStream inputStream = BlobStorageFactory.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME)) {
                properties.load(inputStream);
            }

            String connectionString = properties.getProperty("azure.connectionString");
            String containerName = properties.getProperty("azure.containerName");

            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(connectionString)
                    .buildClient();

            blobContainerClient = blobServiceClient.getBlobContainerClient(containerName);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not load config file! Check if the file exists and the path is correct.");
        }
    }

    public static BlobContainerClient getBlobContainerClient() {
        return blobContainerClient;
    }
}


