package com.wuav.client.gui.controllers;

import com.google.gson.*;
import com.wuav.client.gui.controllers.abstractController.RootController;
import com.wuav.client.gui.utils.AlertHelper;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;



public class ChatController extends RootController implements Initializable {
    @FXML
    private ListView chatListView;
    @FXML
    private MFXButton handleSend;
    @FXML
    private MFXTextField queryField;

    private String loadingMessage;

    private final String API_URL = "https://free.churchless.tech/v1/chat/completions";
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setListView();
        handleSend.setOnAction(event -> {
            handleAPIChatSend();
        });
    }

    private void handleAPIChatSend() {
        String prompt = queryField.getText();
        if (!prompt.isEmpty()) {
            chatListView.getItems().add("You: " + prompt);
            queryField.clear();
            loadingMessage = "Chat: Loading...";
            chatListView.getItems().add(loadingMessage);
            sendAsyncRequest(prompt);
        }
    }

    private void sendAsyncRequest(String prompt) {
        new Thread(() -> {
            try {
                URL url = new URL(API_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json; utf-8");

                String jsonInputString = String.format("{\"messages\":[{\"role\":\"user\",\"content\":\"%s\"}]}", prompt);

                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) { // success
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String content = in.readLine();

                    // close connections
                    in.close();
                    connection.disconnect();

                    // parse json and get the assistant's message
                    JsonObject jsonObject = new JsonParser().parse(content).getAsJsonObject();
                    String assistantMessage = jsonObject.getAsJsonArray("choices").get(0).getAsJsonObject()
                            .getAsJsonObject("message").get("content").getAsString();


                    // update the UI on the JavaFX Application Thread
                    Platform.runLater(() -> {
                        // Remove "Loading..." message
                        chatListView.getItems().remove(loadingMessage);
                        // Add the response message
                        chatListView.getItems().add("Assistant: " + assistantMessage);
                    });
                } else {
                    AlertHelper.showDefaultAlert("Something went wrong. Please try again later.", Alert.AlertType.ERROR);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }


    private void setListView() {
        chatListView.setPadding(new Insets(0, 20, 20, 0));

        chatListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> listView) {
                ListCell<String> cell = new ListCell<String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            // Create a Label to hold the text
                            Label label = new Label(item);

                            // Control text wrapping
                            label.setWrapText(true);
                            label.setMaxWidth(chatListView.getWidth() - 20);  // subtract padding

                            // Add padding
                            setPadding(new Insets(10, 10, 10, 10));

                            setGraphic(label);
                        }
                    }
                };
                return cell;
            }
        });


    }



}
