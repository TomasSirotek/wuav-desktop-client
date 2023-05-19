package com.wuav.client.gui.utils.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.wuav.client.bll.utilities.UniqueIdGenerator;
import com.wuav.client.gui.dto.ImageDTO;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ImageOperationFacade implements IImageOperationService<ImageDTO> {

    private final int userId;
    private Timeline imageFetchTimeline;

    private final String BASE_URL = "http://localhost:5000/api/users/";

    private List<ImageDTO> storedFetchedImages = List.of();


    @Inject
    public ImageOperationFacade(int userId) {
        this.userId = userId;
    }


    public interface ImageFetchCallback {
        void onImagesFetched(List<Image> images);
    }
    @Override
    public void startImageFetch(ImageFetchCallback callback) {
        AtomicBoolean fetched = new AtomicBoolean(false);
        imageFetchTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, e -> {
                    if (!fetched.get()) {
                        List<ImageDTO> images = fetchImagesFromServer(userId);
                        if (!images.isEmpty()) {
                            List<Image> fetchedImages = new ArrayList<>();
                            for (ImageDTO imageDTO : images) {
                                Image image = new Image(imageDTO.getFile().toURI().toString());
                                fetchedImages.add(image);
                            }
                            storedFetchedImages = images;
                            callback.onImagesFetched(fetchedImages);
                            fetched.set(true);
                        }
                    }
                }),
                new KeyFrame(Duration.seconds(5))
        );

        imageFetchTimeline.setCycleCount(Animation.INDEFINITE);
        imageFetchTimeline.play();
    }

    @Override
    public List<ImageDTO> getStoredFetchedImages() {
        return storedFetchedImages;
    }
    @Override
    public void stopImageFetch() {
        if (imageFetchTimeline != null) {
            imageFetchTimeline.stop();
        }
    }

    @Override
    public void removeImagesFromServer() {
        try {
            URL url = new URL(BASE_URL + userId + "/temp-images");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.connect();
            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                System.out.println("Images removed from server");
            } else {
                System.out.println("Error removing images from server: HTTP status code " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ImageDTO> fetchImagesFromServer(int userId) {
        List<ImageDTO> imagesFromApp = new ArrayList<>();
        try {
            URL url = new URL(BASE_URL + userId + "/temp-images");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }
                String jsonResponse = responseBuilder.toString();

                // Deserialize JSON response into a list of strings
                Gson gson = new Gson();
                TypeToken<List<String>> token = new TypeToken<List<String>>() {};
                List<String> base64Images = gson.fromJson(jsonResponse, token.getType());

                int fileIndex = 0;
                for (String base64Image : base64Images) {
                    byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
                    Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"), "tempImages");
                    Files.createDirectories(tempDir);

                    File tempFile = tempDir.resolve("tempImage" + fileIndex + ".png").toFile();
                    try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                        fos.write(decodedBytes);
                    }

                    // Create a new ImageDTO instance and set its properties
                    ImageDTO imageDTO = new ImageDTO();
                    imageDTO.setId(UniqueIdGenerator.generateUniqueId());
                    imageDTO.setFile(tempFile);
                    imageDTO.setMain(false); // You can set this property based on your needs

                    imagesFromApp.add(imageDTO);
                    fileIndex++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imagesFromApp;
    }
}
